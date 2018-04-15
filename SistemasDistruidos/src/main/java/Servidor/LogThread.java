package Servidor;

import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LogThread implements Runnable {
    private List<String> comandos;
    private DatagramPacket receivePacket;
    private DatagramSocket serverSocket;
            
    public LogThread(String comando, DatagramPacket receivePacket, DatagramSocket serverSocket){
        this.comandos = new ArrayList<>();
        this.receivePacket = receivePacket;
        this.serverSocket = serverSocket;
        this.comandos.add(comando);
    }
    
    @Override
    public void run() {
        while(true){
            if(comandos != null && !comandos.isEmpty()){
                
                try{ 
                    FileOutputStream fileout = new FileOutputStream(
                                    "./properties/log.properties", true);
                    Properties prop = ManFileLog.getProp();
                    comandos.forEach((_item) -> {
                        prop.put("comando", comandos.toString()
                                 .replaceAll("\u0000", "") /* removes NUL chars */
                                .replaceAll("\\u0000", "") /* removes backslash+u0000 */);
                    });
                    
                    prop.store(fileout, "Log dos comandos enviados pelo cliente");
                    fileout.flush();
                    
                } catch(Exception ex){
                    ex.printStackTrace();
                }
                
                System.out.println("log: "+comandos.toString());
                break;
            }
        }
    }
    
}
