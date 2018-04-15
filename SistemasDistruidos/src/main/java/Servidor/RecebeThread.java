package Servidor;

import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecebeThread implements Runnable{
    private DatagramSocket serverSocket;
    private ExecutorService executor;
    CRUD crud;
    
    public RecebeThread(DatagramSocket serverSocket, CRUD crud){
        this.serverSocket = serverSocket;
        this.executor = Executors.newCachedThreadPool();
        this.crud = crud;
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
                    ConsumirThread conTrd = new ConsumirThread(comando, receivePacket, serverSocket, crud);
                    executor.execute(conTrd);
                }
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }    
}
