package Cliente;

import Servidor.UDPServer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;

public class ExibeComandosThread implements Runnable {
    
    public ExibeComandosThread () {
    }

    @Override
    public void run() {
        DatagramSocket clientSocket;
        while(true){
            try {
                byte[] receiveData = new byte[1024];
                Properties prop = UDPServer.getProp();
                clientSocket = new DatagramSocket();
                String port = prop.getProperty("prop.server.port");
                InetAddress IPAddress = InetAddress.getByName(prop.getProperty("prop.server.host"));

                //Recebe
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length, IPAddress, Integer.parseInt(port));
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
