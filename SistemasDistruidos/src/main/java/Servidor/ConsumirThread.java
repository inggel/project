package Servidor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumirThread implements Runnable {
    private List<String> comandos;
    private ExecutorService executor;
    private DatagramSocket serverSocket;
    private DatagramPacket receivePacket;
    private CRUD crud;
    
    public ConsumirThread(String comando, DatagramPacket receivePacket, 
            DatagramSocket serverSocket, CRUD crud){
        this.receivePacket = receivePacket;
        this.serverSocket = serverSocket;
        comandos = new ArrayList<>();
        comandos.add(comando);
        this.executor = Executors.newCachedThreadPool();
        this.crud = crud;
    }
    
    @Override
    public void run() {
        LogThread logTrd = null;
        ProcessaThread procTrd = null;
        while(true){
            if(comandos != null && !comandos.isEmpty()){
                for(Iterator<String> c = comandos.listIterator(); c.hasNext();){
                    String cmd = c.next();
                    procTrd = new ProcessaThread(cmd, receivePacket, serverSocket, crud);
                    if(!cmd.contains("7")){
                        logTrd = new LogThread(cmd, receivePacket, serverSocket);
                    }
                    c.remove();
                    if(procTrd != null)
                        this.executor.execute(procTrd);
                    
                    if(logTrd != null){
                        this.executor.execute(logTrd);
                    }   
                }
                break;
            }
        }
    }
}
