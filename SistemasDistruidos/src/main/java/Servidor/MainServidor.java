package Servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;

public class MainServidor {
    public static void main(String args[]) throws Exception {
        
        Properties prop = UDPServer.getProp();
        String porta = prop.getProperty("prop.server.port");
        
        DatagramSocket serverSocket = new DatagramSocket(Integer.parseInt(porta));
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
         BufferedReader fromServer =
                new BufferedReader(new InputStreamReader(System.in));
         
         System.out.println("Servidor iniciado!");
        while(true)
           {
              DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
              serverSocket.receive(receivePacket);
              String sentence = new String( receivePacket.getData());
              System.out.println("FROM CLIENT: " + sentence);
              InetAddress IPAddress = receivePacket.getAddress();
              int port = receivePacket.getPort();
               System.out.print("SERVER: ");
              String capitalizedSentence = fromServer.readLine();
              sendData = capitalizedSentence.getBytes();
              DatagramPacket sendPacket =
              new DatagramPacket(sendData, sendData.length, IPAddress, port);
              serverSocket.send(sendPacket);

           }
      }
}
