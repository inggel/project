package Cliente;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ExibeComandosThread implements Runnable {
    
    public ExibeComandosThread () {
    }

    @Override
    public void run() {
        DatagramSocket clientSocket;
        while(true){
            try {
                byte[] receiveData = new byte[1024];
                clientSocket = new DatagramSocket();

                //Recebe
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String modifiedSentence = new String(receivePacket.getData());
                
                if(!modifiedSentence.isEmpty()){
                    System.out.println("sv:> " + modifiedSentence);
                    break;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
