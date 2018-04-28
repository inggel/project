package Servidor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecebeThread implements Runnable{
    private DatagramSocket serverSocket;
    private ExecutorService executor;
    private CRUD crud;
    private ConsumirThread conTrd;
    
    public RecebeThread(DatagramSocket serverSocket, CRUD crud){
        this.serverSocket = serverSocket;
        this.executor = Executors.newCachedThreadPool();
        this.crud = crud;
        conTrd = new ConsumirThread();
    }
    
    @Override
    public void run() {        
        while(true){
            try{
                byte[] receiveData = new byte[1401];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                
                serverSocket.receive(receivePacket);
                String comando="";
                comando = new String(receivePacket.getData(), 0, receivePacket.getLength());
               
                // Cria thread de consumir da fila e enviar para log e processador do comando.
                if(!comando.isEmpty() && !(comando.equalsIgnoreCase(""))){
                    conTrd.setReceivePacket(receivePacket);
                    conTrd.setServerSocket(serverSocket);
                    conTrd.setCrud(crud);
                    conTrd.addComando(comando);
                    
                    executor.execute(conTrd);
                }
                                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }    
}
