package com.mycompany.sistemasdistruidos.cliente;

import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.Scanner;
import com.mycompany.sistemasdistruidos.servidor.UDPServer;

class UDPClient
{
   public static void main(String args[]) throws Exception
   {
       
       
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
      
      while(sair){
            System.out.print("CLIENT: ");
            String sentence = inFromUser.readLine();
            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, Integer.parseInt(porta));
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData());
            System.out.println("FROM SERVER: " + modifiedSentence);
      
      }
      clientSocket.close();
   }
}