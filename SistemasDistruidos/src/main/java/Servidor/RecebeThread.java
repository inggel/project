package Servidor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class RecebeThread implements Runnable{
    private DatagramSocket serverSocket;
    private CRUD crud;
    private ConsumirThread conTrd;
    
    public RecebeThread(ConsumirThread conTrd, DatagramSocket serverSocket, CRUD crud){
        this.serverSocket = serverSocket;
        this.crud = crud;
        this.conTrd = conTrd;
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
                    receivePacket = null;
                }
                                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }    
}
