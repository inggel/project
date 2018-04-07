package Servidor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class LogThread implements Runnable {
    private List<String> comandos;
    private DatagramPacket receivePacket;
    private DatagramSocket serverSocket;
            
    public LogThread(List<String> comandos, DatagramPacket receivePacket, DatagramSocket serverSocket){
        this.comandos = new ArrayList<>();
        this.receivePacket = receivePacket;
        this.serverSocket = serverSocket;
        if(comandos != null && !comandos.isEmpty()){
            System.out.println("Log iniciado");
            this.comandos.addAll(comandos);
        }
    }
    
    @Override
    public void run() {
        while(true){
            if(comandos != null && !comandos.isEmpty()){
                byte[] sendData = new byte[1024];
                String resp = "recebi";
                try{
                    sendData = resp.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                    serverSocket.send(sendPacket);
                    break;
                } catch(Exception ex){
                    ex.printStackTrace();
                }
                
                System.out.println("log: "+comandos.toString());
                break;
            }
        }
    }
    
}
