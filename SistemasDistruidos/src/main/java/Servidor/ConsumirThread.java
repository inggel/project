package Servidor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
                    String co = "" + cmd.charAt(0);
                    
                    // Comando que e para exibir o menu novamente ao cliente nao precisa ser processado
                    if(!co.contains("6")){
                        procTrd = new ProcessaThread(cmd, receivePacket, serverSocket, crud);
                    }
                    
                    /* Comando que e para sair, exibir o menu novamente e 
                    listar ao cliente nao precisa ser processado*/
                    if(!co.contains("7") && !co.contains("6") && !co.contains("5") && !co.contains("4")){
                        logTrd = new LogThread(cmd);
                    }
                    
                    c.remove();
                    
                    if(procTrd != null){
                        this.executor.execute(procTrd);
                    }
                    
                    if(logTrd != null){
                        this.executor.execute(logTrd);
                    }
                }
                break;
            }
        }
    }
}
