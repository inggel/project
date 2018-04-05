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
      DatagramSocket clientSocket = new DatagramSocket();
      InetAddress IPAddress = InetAddress.getByName(prop.getProperty("prop.server.host"));
      byte[] sendData = new byte[1024];
      byte[] receiveData = new byte[1024];
      boolean sair = true;
      Scanner sc = new Scanner(System.in);
      int opcao = -1;
      ExecutorService executor = Executors.newCachedThreadPool();
            
      while(sair){
            System.out.println("---- Sistemas Distruibuidos ----");
            System.out.print("CLIENT: ");
            
            ComandosClienteThread cmdcli = new ComandosClienteThread();
            executor.execute(cmdcli);
            
            //Envia
            DatagramPacket sendPacket = new DatagramPacket(cmdcli.getSendData(), cmdcli.getSendData().length, IPAddress, Integer.parseInt(porta));
            clientSocket.send(sendPacket);
            
            //Recebe
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData());
            
            ExibeComandosThread exibCmd = new ExibeComandosThread(modifiedSentence);
            executor.execute(exibCmd);
      }
      executor.shutdownNow();
      clientSocket.close();
   }
}