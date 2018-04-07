package Servidor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class RecebeThread implements Runnable{
    private byte[] receiveData = new byte[1024];
    private List<String> comandos;
    private DatagramSocket serverSocket;
    private DatagramPacket receivePacket;
    
    public RecebeThread(DatagramSocket serverSocket){
        this.comandos = new ArrayList<>();
        this.serverSocket = serverSocket;
        this.receivePacket = new DatagramPacket(this.receiveData, this.receiveData.length);
    }
    
    @Override
    public void run() {
        try{
            serverSocket.receive(this.receivePacket);
            String sentence = new String(this.receivePacket.getData());
            comandos.add(sentence);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<String> getComandos() {
        return comandos;
    }

    public void setComandos(List<String> comandos) {
        this.comandos = comandos;
    }
    
}
