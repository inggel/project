package Servidor;

import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProcessaThread implements Runnable{
    private List<String> comandos;
    private DatagramPacket receivePacket;
    private DatagramSocket serverSocket;
    private String busca;
    private ArrayList<String> lista =  new ArrayList<>();
    private boolean cria, deleta, atualiza;
    private CRUD crud;

    public ProcessaThread(String comando, DatagramPacket receivePacket, 
            DatagramSocket serverSocket, CRUD crud){
        this.comandos = new ArrayList<>();
        this.comandos.add(comando);
        this.receivePacket = receivePacket;
        this.serverSocket = serverSocket;
        this.crud = crud;
    }
    
    @Override
    public void run() {
        byte[] sendData = new byte[1400];
        String dados="";
        
        try{
            for(String c : comandos){
                sendData = c.getBytes();
                
                if(c.contains("7")){
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                    serverSocket.send(sendPacket);
                } else{
                    List<String> inst = new ArrayList<>();
                    inst = Arrays.asList(c.split(" ")); 
                    //colocar a função de subs caracteres especiais
//                    if(inst.get(0).contains("4")){
//                        busca = crud.search(new BigInteger(inst.get(1).replaceAll("\u0000", "").replaceAll("\\u0000", "")));
//                             if(busca != null && !busca.isEmpty())
//                                dados = busca+"\n"; 
//                    }
//                    // aqui esta dando erro porque ele não passa o valor da chave correta para a funcao delete
//                    // switch nao esta entrando no update, delete e busca
//                    if(inst.get(0).contains("2")){
//                        deleta = crud.delete(new BigInteger(inst.get(1)), inst.get(2));
//                            if(deleta)
//                                dados = "Deletado com sucesso!\n";
//                    }
                    
                    switch(inst.get(0).replaceAll("\u0000", "").replaceAll("\\u0000", "").charAt(0)){
                        case '1':
                            cria = crud.create(new BigInteger(inst.get(1).replaceAll("\u0000", "").replaceAll("\\u0000", "")), inst.get(2).replaceAll("\u0000", "").replaceAll("\\u0000", ""));
                            if(cria){
                                dados = "Criado com sucesso!\n";
                            }
                            break;

                        case '2':
                            deleta = crud.delete(new BigInteger(inst.get(1).replaceAll("\u0000", "").replaceAll("\\u0000", "")), inst.get(2).replaceAll("\u0000", "").replaceAll("\\u0000", ""));
                            if(deleta)
                                dados = "Deletado com sucesso!\n";
                            break;

                        case '3':
                            atualiza = crud.update(new BigInteger(inst.get(1).replaceAll("\u0000", "").replaceAll("\\u0000", "")), inst.get(2).replaceAll("\u0000", "").replaceAll("\\u0000", ""));
                             if(atualiza)
                                dados = "Atualizado com sucesso!\n";
                            break;


                        case '4':
                            busca = crud.search(new BigInteger(inst.get(1).replaceAll("\u0000", "").replaceAll("\\u0000", "")));
                             if(busca != null && !busca.isEmpty())
                                dados = busca+"\n";
                            break;

                        case '5':
                            lista = crud.read();
                            break;

                        default:
                            break;
                    }
                        
                    dados += "Digite a opcao: ";
                    sendData = dados.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                    serverSocket.send(sendPacket);
                }
                break;
            }
        } catch(Exception e){
            e.printStackTrace();
            e.getMessage();
        }   
    }
    
}
