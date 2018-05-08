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
                byte[] receiveData = new byte[1401];
                
                //Recebe
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String co = ""+modifiedSentence.charAt(0);
                
                if(co.contains("7")){
                    System.out.println("Encerrando!");
                    break;
                }
                
                if(!modifiedSentence.isEmpty()){
                    System.out.println("sv:> " + modifiedSentence);
                    System.out.print("Digite a opção: ");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
