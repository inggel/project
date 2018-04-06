package Servidor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class RecebeThread implements Runnable{
    private ArrayList<String> comandos;
        
    // To convert it back to arrays
    // Byte[] soundBytes = arrays.toArray(new Byte[arrays.size()]);
    public RecebeThread(String porta, DatagramSocket serverSocket){
        try{
            byte[] receiveData = new byte[1024];
            this.comandos = new ArrayList<>();
            
            // Recebe o pacote le a string e add na lista
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String( receivePacket.getData());
            comandos.add(sentence);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        //To change body of generated methods, choose Tools | Templates.
    }

    public ArrayList<String> getComandos() {
        return comandos;
    }

    public void setComandos(ArrayList<String> comandos) {
        this.comandos = comandos;
    }
    
}
