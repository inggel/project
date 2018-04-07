package Cliente;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ExibeComandosThread implements Runnable {
    
    public ExibeComandosThread () {
    }

    @Override
    public void run() {
        DatagramSocket clientSocket;
        try {
            byte[] receiveData = new byte[1024];
            clientSocket = new DatagramSocket();
        
            //Recebe
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData());
            
            System.out.println("Resposta: " + modifiedSentence);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
