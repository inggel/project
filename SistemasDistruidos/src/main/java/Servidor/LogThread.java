package Servidor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LogThread implements Runnable {
    private List<String> comandos;
    private DatagramPacket receivePacket;
    private DatagramSocket serverSocket;
            
    public LogThread(List<String> comandos, DatagramPacket receivePacket, DatagramSocket serverSocket){
        this.comandos = new ArrayList<>();
        this.receivePacket = receivePacket;
        this.serverSocket = serverSocket;
        if(comandos != null && !comandos.isEmpty()){
            System.out.println("Log iniciado.");
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
                    File file = new File("./properties/log.properties");
                    if(!file.exists()){
                       file.createNewFile();
                    }
                    
                    FileOutputStream fileout = new FileOutputStream(
                                    "./properties/log.properties");
                    Properties prop = ManFileLog.getProp();
                    prop.put("comando", comandos.toString());

                    prop.store(fileout, "Comentario 1");
                    fileout.flush();
                    
                    sendData = resp.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                    serverSocket.send(sendPacket);
                    break;
                } catch(Exception ex){
                    ex.printStackTrace();
                }
                
                System.out.println("log: "+comandos.toString());
                //break;
            }
        }
    }
    
}
