package Servidor;

import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ProcessaThread implements Runnable{
    private Queue<String> comandos = new LinkedList<>();
    private DatagramPacket receivePacket;
    private DatagramSocket serverSocket;
    private String busca;
    private ArrayList<String> lista =  new ArrayList<>();
    private boolean cria, deleta, atualiza;
    private CRUD crud;
    private List<String> inst = new ArrayList<>();

    public ProcessaThread(){
        // ctor
    }
    
    @Override
    public void run() {
        byte[] sendData = new byte[1401];
        String dados="";
        
        try{
            Iterator<String> cmd = comandos.iterator();
                
            while(cmd.hasNext()){
                String c = cmd.next();
                sendData = c.getBytes();
                
                /* Caso o comando do cliente seja 7 envia para o cliente 7 para encerrar */
                if((""+c.charAt(0)).contains("7")){
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, getReceivePacket().getAddress(), getReceivePacket().getPort());
                    getServerSocket().send(sendPacket);
                } else {
                    
                    inst = Arrays.asList(c.split(" ")); 
                   
                    switch(inst.get(0).replaceAll("\u0000", "").replaceAll("\\u0000", "").charAt(0)){
                        /* Criar um dado no map */
                        case '1':
                            cria = getCrud().create(new BigInteger(inst.get(1).replaceAll("\u0000", "").
                                    replaceAll("\\u0000", "")), valor(inst));
                            if(cria)
                                dados = "Criado com sucesso!\n";
                            else
                                dados = "Não foi possivel completar a operacao\n";
                            break;
                        
                        /* Deletar um dado no map */
                        case '2':
                            deleta = getCrud().delete(new BigInteger(inst.get(1).replaceAll("\u0000", "").
                                    replaceAll("\\u0000", "")));
                            if(deleta)
                                dados = "Deletado com sucesso!\n";
                            else
                                dados = "Não foi possivel completar a operacao\n";
                            break;

                        /* Atualizar um dado no map */
                        case '3':
                            atualiza = getCrud().update(new BigInteger(inst.get(1).replaceAll("\u0000", "").
                                    replaceAll("\\u0000", "")), valor(inst));
                             if(atualiza)
                                dados = "Atualizado com sucesso!\n";
                             else
                                dados = "Não foi possivel completar a operacao\n";
                            break;

                        /* Busca um dado no map */
                        case '4':
                            busca = getCrud().search(new BigInteger(inst.get(1).replaceAll("\u0000", "").replaceAll("\\u0000", "")));
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

                        default:
                            break;
                    }
                        
                    sendData = dados.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, getReceivePacket().getAddress(), getReceivePacket().getPort());
                    getServerSocket().send(sendPacket);
                }
                
                cmd.remove();
                break;
            }
        } catch(Exception e){
            e.printStackTrace();
            e.getMessage();
        }   
    }
    
    /* Substitui os lixos tragos junto com os dados e concatena as strings passadas*/
    public String valor(List<String> inst){
        String valor = "";
        
        for(int i=2; i < inst.size(); i++){
            valor += inst.get(i)+" ";
        }
        
        return valor.replaceAll("\u0000", "").replaceAll("\\u0000", "");
    }
    
    /* Metodo utilizado pelo log para carregar os dados no map de dados */
    public CRUD processaComando(List<String> inst, CRUD crud){
        String dados = "";
        String c = inst.get(1).replaceAll("\u0000", "")
                        .replaceAll("\\u0000", "")
                        .replaceAll("\\]","");
        
        inst = Arrays.asList(c.split(" "));
        
        switch(inst.get(0).replaceAll("\u0000", "").replaceAll("\\u0000", "").charAt(0)){
            case '1':
                cria = crud.create(new BigInteger(inst.get(1).replaceAll("\u0000", "")
                        .replaceAll("\\u0000", "")
                        .replaceAll("\\]","")), valor(inst));
                if(cria)
                    dados = "Criado com sucesso!\n";
                else
                    dados = "Não foi possivel completar a operacao\n";
                break;

            case '2':
                deleta = crud.delete(new BigInteger(inst.get(1).replaceAll("\u0000", "")
                        .replaceAll("\\u0000", "")
                        .replaceAll("\\]","")));
                if(deleta)
                    dados = "Deletado com sucesso!\n";
                else
                    dados = "Não foi possivel completar a operacao\n";
                break;

            case '3':
                atualiza = crud.update(new BigInteger(inst.get(1).replaceAll("\u0000", "")
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
    
}
