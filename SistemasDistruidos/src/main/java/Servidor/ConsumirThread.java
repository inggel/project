package Servidor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumirThread implements Runnable {
    private List<String> comandos;
    private ExecutorService executor;
    private DatagramSocket serverSocket;
    private DatagramPacket receivePacket;
    
    public ConsumirThread(List<String> comandos, DatagramPacket receivePacket, DatagramSocket serverSocket){
        this.receivePacket = receivePacket;
        this.serverSocket = serverSocket;
        this.comandos = new ArrayList<>();
        if(comandos != null && !comandos.isEmpty()){
            this.comandos.addAll(comandos);
            this.executor = Executors.newCachedThreadPool();
        }
    }
    
    @Override
    public void run() {
        while(true){
            if(comandos != null && !comandos.isEmpty()){
                LogThread logTrd = new LogThread(this.comandos, receivePacket, serverSocket);
                ProcessaThread procTrd = new ProcessaThread(this.comandos);

                //this.executor.execute(procTrd);
                System.out.println("iniciando log");
                this.executor.execute(logTrd);
                break;
            }
        }
    }
    
}
