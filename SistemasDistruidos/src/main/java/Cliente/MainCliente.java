package Cliente;

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
            
      int i = 0;
      while(i != 3){
            System.out.println("---- Sistemas Distruibuidos ----");
            System.out.print("CLIENT: ");
            
            ComandosClienteThread cmdcli = new ComandosClienteThread();
            executor.execute(cmdcli);
            
            if(cmdcli.getComando() != null && !(cmdcli.getComando().isEmpty())){
                
                sendData = cmdcli.getComando().getBytes();
                //Envia
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, Integer.parseInt(porta));
                clientSocket.send(sendPacket);

                //Recebe
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String modifiedSentence = new String(receivePacket.getData());

                ExibeComandosThread exibCmd = new ExibeComandosThread(modifiedSentence);
                executor.execute(exibCmd);
            }
                i++;
      }
      executor.shutdownNow();
      clientSocket.close();
   }
}