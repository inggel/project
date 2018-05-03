package Servidor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumirThread implements Runnable {
    private Queue<String> comandos = new LinkedList<String>();
    private ExecutorService executor;
    private DatagramSocket serverSocket;
    private DatagramPacket receivePacket;
    private CRUD crud;
    LogThread logTrd;
    ProcessaThread procTrd;
    
    public ConsumirThread(){
        this.executor = Executors.newCachedThreadPool();
        logTrd = new LogThread();
        procTrd  = new ProcessaThread();
    }
        
    @Override
    public void run() {
        while(true){
            if(getComandos() != null && !comandos.isEmpty()){
                
                Iterator<String> c = getComandos().iterator();
                
                while(c.hasNext()){
                    String cmd = c.next();
                    String co = "" + cmd.charAt(0);
                    
                    // Comando que e para exibir o menu novamente ao cliente nao precisa ser processado
                    if(!co.contains("6")){
                        procTrd.setReceivePacket(receivePacket);
                        procTrd.setServerSocket(serverSocket);
                        procTrd.setCrud(crud);
                        procTrd.addComando(cmd);
                    }
                    
                    /* Comando que e para sair, exibir o menu novamente e 
                    listar ao cliente nao precisa ser processado*/
                    if(!co.contains("7") && !co.contains("6") && !co.contains("5") && !co.contains("4")){
                        logTrd.addComando(cmd);
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

    public Queue<String> getComandos() {
        return comandos;
    }

    public void setComandos(Queue<String> comandos) {
        this.comandos = comandos;
    }
    
    public DatagramSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(DatagramSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public DatagramPacket getReceivePacket() {
        return receivePacket;
    }

    public void setReceivePacket(DatagramPacket receivePacket) {
        this.receivePacket = receivePacket;
    }

    public CRUD getCrud() {
        return crud;
    }

    public void setCrud(CRUD crud) {
        this.crud = crud;
    }
        
    public void addComando(String comando){
        comandos.add(comando);
    }
}
