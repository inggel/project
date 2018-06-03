package Servidor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsumirThread implements Runnable {
    private BlockingQueue<String> comandos = new LinkedBlockingQueue<String>();
    private DatagramSocket serverSocket;
    private DatagramPacket receivePacket;
    private CRUD crud;
    private LogThread logTrd;
    private ProcessaThread procTrd;
    
    public ConsumirThread(LogThread logTrd, ProcessaThread procTrd){
        this.logTrd = logTrd;
        this.procTrd = procTrd;
    }
        
    @Override
    public void run() {
        while(true){
            String cmd;
            try {
                cmd = comandos.take();
            } catch (InterruptedException ex) {
                continue;
            }
            String co = "" + cmd.charAt(0);

                // Comando que e para exibir o menu novamente ao cliente nao precisa ser processado
                if(!co.equalsIgnoreCase("6")){
                    procTrd.setReceivePacket(receivePacket);
                    procTrd.setServerSocket(serverSocket);
                    procTrd.setCrud(crud);
                    procTrd.addComando(cmd);
                    receivePacket = null;
                }
                
                /* Comando que e para sair, exibir o menu novamente e 
                listar ao cliente nao precisa ser processado*/
                if(!co.equalsIgnoreCase("8") && !co.equalsIgnoreCase("6") 
                        && !co.equalsIgnoreCase("5") && !co.equalsIgnoreCase("4")){
                    logTrd.addComando(cmd);
                }
            
            
        }
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
        getComandos().add(comando);
    }
    
    public BlockingQueue<String> getComandos() {
        return comandos;
    }
    
    public void setComandos(BlockingQueue<String> comandos) {
        this.comandos = comandos;
    }
}
