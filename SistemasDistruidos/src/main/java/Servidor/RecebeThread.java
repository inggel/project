package Servidor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class RecebeThread implements Runnable{
    private List<String> comandos;
    private DatagramSocket serverSocket;
    private String comando;
    
    public RecebeThread(DatagramSocket serverSocket){
        this.comandos = new ArrayList<>();
        this.serverSocket = serverSocket;
    }
    
    @Override
    public void run() {
        try{
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            while(true){
                serverSocket.receive(receivePacket);
                comando = new String(receivePacket.getData());
                if(!comando.isEmpty() && !(comando == "")){
                    System.out.println(comando+ " added");
                    comandos.add(comando);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<String> getComandos() {
        return this.comandos;
    }

    public void setComandos(List<String> comandos) {
        this.comandos = comandos;
    }

    /**
     * @return the comando
     */
    public String getComando() {
        return comando;
    }

    /**
     * @param comando the comando to set
     */
    public void setComando(String comando) {
        this.comando = comando;
    }
    
}
