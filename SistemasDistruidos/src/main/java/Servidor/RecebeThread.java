package Servidor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecebeThread implements Runnable{
    private List<String> comandos;
    private DatagramSocket serverSocket;
    private String comando;
    private ExecutorService executor;
    
    public RecebeThread(DatagramSocket serverSocket){
        this.comandos = new ArrayList<>();
        this.serverSocket = serverSocket;
        this.executor = Executors.newCachedThreadPool();
    }
    
    @Override
    public void run() {        
        while(true){
            try{
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                
                serverSocket.receive(receivePacket);
                comando = new String(receivePacket.getData());
                if(!comando.isEmpty() && !(comando.equalsIgnoreCase(""))){
                    comandos.add(comando);
                    
                    ConsumirThread conTrd = new ConsumirThread(comandos, receivePacket, serverSocket);
                    executor.execute(conTrd);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
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
