package Servidor;

import io.grpc.SistemasDistruidos.message.ComandResponse;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProcessaThread implements Runnable{
    private BlockingQueue<String> comandos = new LinkedBlockingQueue<String>();
    private DatagramPacket receivePacket;
    private DatagramSocket serverSocket;
    private String busca;
    private ArrayList<String> lista =  new ArrayList<String>();
    private boolean cria, deleta, atualiza;
    private CRUD crud;
    private List<String> inst = new ArrayList<String>();
    private List<MonitorObject> monitorObject = new ArrayList<MonitorObject>();
    private io.grpc.stub.StreamObserver<ComandResponse> responseObserverGrpc;
    private String monChave = "";

    public ProcessaThread(){
        // ctor
    }
    
    @Override
    public void run() {
        byte[] sendData = new byte[1401];
        
        while(true){
            String dados="";
            Iterator<String> cmd = comandos.iterator();
            try{

                while(cmd.hasNext()){
                    String c = cmd.next();
                    sendData = c.getBytes();

                    /* Caso o comando do cliente seja 7 envia para o cliente 7 para encerrar */
                    if((""+c.charAt(0)).contains("8")){
                        if(receivePacket != null){
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, getReceivePacket().getAddress(), getReceivePacket().getPort());
                            getServerSocket().send(sendPacket);
                        }
                    } else {

                        inst = Arrays.asList(c.split(" ")); 

                        switch(inst.get(0).replaceAll("\\u0000", "").charAt(0)){
                            /* Criar um dado no map */
                            case '1':
                                cria = getCrud().create(new BigInteger(inst.get(1).
                                        replaceAll("\\u0000", "")), valor(inst));
                                if(cria)
                                    dados = "Criado com sucesso!\n";
                                else
                                    dados = "Não foi possivel completar a operacao\n";
                                break;

                            /* Deletar um dado no map */
                            case '2':
                                deleta = getCrud().delete(new BigInteger(inst.get(1).
                                        replaceAll("\\u0000", "")));
                                if(deleta)
                                    dados = "Deletado com sucesso!\n";
                                else
                                    dados = "Não foi possivel completar a operacao\n";
                                break;

                            /* Atualizar um dado no map */
                            case '3':
                                atualiza = getCrud().update(new BigInteger(inst.get(1).
                                        replaceAll("\\u0000", "")), valor(inst));
                                 if(atualiza)
                                    dados = "Atualizado com sucesso!\n";
                                 else
                                    dados = "Não foi possivel completar a operacao\n";
                                break;

                            /* Busca um dado no map */
                            case '4':
                                busca = getCrud().search(new BigInteger(inst.get(1).replaceAll("\\u0000", "")));
                                 if(busca != null && !busca.isEmpty())
                                    dados = busca+"\n";
                                 else
                                     dados = "Não existe essa chave\n";
                                break;

                            /* Lista todos os dados do map */
                            case '5':
                                lista = getCrud().read();
                                 if(lista != null && !lista.isEmpty())
                                    dados = lista+"\n";
                                 else
                                     dados = "Não há registro de dados\n";
                                break;
                                
                            case '7':
                                monChave = inst.get(1);
                                dados += "Monitorando\n";
                                break;

                            default:
                                break;
                        }                      
                                                          
                        // resposta ao udp
                        if(receivePacket != null){                            
                            sendData = dados.getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, getReceivePacket().getAddress(), getReceivePacket().getPort());
                            getServerSocket().send(sendPacket);
                            
                            if(inst.get(0).equalsIgnoreCase("7")){
                                // Cria um objeto e add a chave e para quem mandar.
                                MonitorObject monObj = new MonitorObject();
                                monObj.setMonChave(monChave);
                                monObj.setPacoteUdp(sendPacket);
                                monObj.setSocketUdp(serverSocket);
                                monitorObject.add(monObj);
                            }
                        }
                        
                        // Parte de Monitoramento add no list
                        if(monitorObject.size() > 0 && !inst.get(0).equalsIgnoreCase("5") && !inst.get(0).equalsIgnoreCase("7")){
                            for(MonitorObject mo : monitorObject){
                                //Udp sender
                                if(mo.getSocketUdp() != null && mo.getMonChave().equalsIgnoreCase(inst.get(1)) 
                                        && receivePacket != null){
                                    
                                    dados = "Ow callback da chave cara - " +mo.getMonChave()+ " Comandos: " + inst +"\n";
                                    sendData = dados.getBytes();
                                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, getReceivePacket().getAddress(), getReceivePacket().getPort());
                                    DatagramSocket ss = mo.getSocketUdp();
                                    ss.send(sendPacket);
                                }
                                
                                // Grpc sender
                                if(mo.getResponseObserverGrpc() != null && mo.getMonChave().equalsIgnoreCase(inst.get(1))
                                        && !inst.get(0).equalsIgnoreCase("5") && !inst.get(0).equalsIgnoreCase("7")){
                                    
                                    try{
                                        dados = "Ow callback da chave cara - " +mo.getMonChave()+ "Comandos: " + inst +"\n";
                                        ComandResponse rspGrpc = ComandResponse.newBuilder().setCmd(dados + " ").build();
                                        mo.getResponseObserverGrpc().onNext(rspGrpc);
                                    } catch(Exception e){}
                                }
                            }
                        }
                        
                        // Resposta ao grpc         
                        if(responseObserverGrpc != null){
                            
                            if(inst.get(0).equalsIgnoreCase("7")){
                                // Adiciona no array o grpc e a chave que estao monitorando
                                MonitorObject monObj = new MonitorObject();
                                monObj.setMonChave(monChave);
                                monObj.setResponseObserverGrpc(responseObserverGrpc);
                                monitorObject.add(monObj);
                            }
                            
                            ComandResponse rspGrpc = ComandResponse.newBuilder().setCmd(dados + " ").build();
                            this.responseObserverGrpc.onNext(rspGrpc);
                        }
                        
                    }

                    cmd.remove();
                    receivePacket = null;
                    responseObserverGrpc = null;
                }
            } catch(Exception e){
            }   
        }
    }
    
    /* Substitui os lixos tragos junto com os dados e concatena as strings passadas*/
    public String valor(List<String> inst){
        String valor = "";
        
        for(int i=2; i < inst.size(); i++){
            valor += inst.get(i)+" ";
        }
        
        return valor.replaceAll("\\u0000", "");
    }
    
    /* Metodo utilizado pelo log para carregar os dados no map de dados */
    public CRUD processaComando(List<String> inst, CRUD crud){
        String dados = "";
        String c = inst.get(1)
                        .replaceAll("\\u0000", "")
                        .replaceAll("\\]","");
        
        inst = Arrays.asList(c.split(" "));
        
        switch(inst.get(0).replaceAll("\\u0000", "").charAt(0)){
            case '1':
                cria = crud.create(new BigInteger(inst.get(1)
                        .replaceAll("\\u0000", "")
                        .replaceAll("\\]","")), valor(inst));
                if(cria)
                    dados = "Criado com sucesso!\n";
                else
                    dados = "Não foi possivel completar a operacao\n";
                break;

            case '2':
                deleta = crud.delete(new BigInteger(inst.get(1)
                        .replaceAll("\\u0000", "")
                        .replaceAll("\\]","")));
                if(deleta)
                    dados = "Deletado com sucesso!\n";
                else
                    dados = "Não foi possivel completar a operacao\n";
                break;

            case '3':
                atualiza = crud.update(new BigInteger(inst.get(1)
                        .replaceAll("\\u0000", "")
                        .replaceAll("\\]","")), valor(inst));
                 if(atualiza)
                    dados = "Atualizado com sucesso!\n";
                 else
                    dados = "Não foi possivel completar a operacao\n";
                break;
            default:
                break;
        }
        return crud;
    }
    
    public void addComando(String comando){
        comandos.add(comando);
    }

    public DatagramPacket getReceivePacket() {
        return receivePacket;
    }

    public void setReceivePacket(DatagramPacket receivePacket) {
        this.receivePacket = receivePacket;
    }

    public DatagramSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(DatagramSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public CRUD getCrud() {
        return crud;
    }

    public void setCrud(CRUD crud) {
        this.crud = crud;
    }

    public io.grpc.stub.StreamObserver<ComandResponse> getResponseObserverGrpc() {
        return responseObserverGrpc;
    }

    public void setResponseObserverGrpc(io.grpc.stub.StreamObserver<ComandResponse> responseObserverGrpc) {
        this.responseObserverGrpc = responseObserverGrpc;
    }
    
}
