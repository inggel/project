package Cliente;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ExibeComandosThread implements Runnable {
    private DatagramSocket clientSocket;
    
    public ExibeComandosThread (DatagramSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        while(true){
            try {
                byte[] receiveData = new byte[1024];

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
