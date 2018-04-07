package Cliente;

import Servidor.UDPServer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;

public class ComandosClienteThread implements Runnable {
    private String comando;
    
    public ComandosClienteThread(){
        // Construtor
    }

    @Override
    public void run() {
        try{
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(System.in));
            byte[] sendData = new byte[1024];
            Properties prop = UDPServer.getProp();
            String porta = prop.getProperty("prop.server.port");
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(prop.getProperty("prop.server.host"));
            
            comando = streamReader.readLine();
            sendData = comando.getBytes();
            
            //Envia
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, Integer.parseInt(porta));
            clientSocket.send(sendPacket);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
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
