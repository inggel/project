package Cliente;

import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.Scanner;
import Servidor.UDPServer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MainCliente
{
   public static void main(String args[]) throws Exception {
       
      Properties prop = UDPServer.getProp();
      String porta = prop.getProperty("prop.server.port");
      BufferedReader inFromUser =
         new BufferedReader(new InputStreamReader(System.in));
      DatagramSocket clientSocket = new DatagramSocket();
      InetAddress IPAddress = InetAddress.getByName(prop.getProperty("prop.server.host"));
      byte[] sendData = new byte[1024];
      byte[] receiveData = new byte[1024];
      boolean sair = true;
      Scanner sc = new Scanner(System.in);
      int opcao = -1;
      ExecutorService executor = Executors.newSingleThreadExecutor();
            
      while(sair){
            System.out.println("---- Sistemas Distruibuidos ----");
           
            System.out.print("CLIENT: ");
            //String sentence = inFromUser.readLine();
            //sendData = sentence.getBytes();
            executor.submit(() ->{
                ComandosClienteThread cmdcli = new ComandosClienteThread();
                System.out.println("fim: " + new String(cmdcli.getSendData()));
            });
            
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, Integer.parseInt(porta));
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData());
            System.out.println("FROM SERVER: " + modifiedSentence);
      }
      executor.shutdownNow();
      clientSocket.close();
   }
}