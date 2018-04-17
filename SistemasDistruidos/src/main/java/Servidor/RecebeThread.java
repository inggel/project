package Servidor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class RecebeThread implements Runnable{
    private DatagramSocket serverSocket;
    private ExecutorService executor;
    CRUD crud;
    AtomicInteger seq;
    
    
    public RecebeThread(DatagramSocket serverSocket, CRUD crud, AtomicInteger seq){
        this.serverSocket = serverSocket;
        this.executor = Executors.newCachedThreadPool();
        this.crud = crud;
        this.seq = seq;
    }
    
    @Override
    public void run() {        
        while(true){
            try{
                byte[] receiveData = new byte[1400];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                
                serverSocket.receive(receivePacket);
                String comando="";
                comando = new String(receivePacket.getData());
               
                if(!comando.isEmpty() && !(comando.equalsIgnoreCase(""))){
                    ConsumirThread conTrd = new ConsumirThread(comando, receivePacket, serverSocket, crud, seq);
                    executor.execute(conTrd);
                }
                
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }    
}
