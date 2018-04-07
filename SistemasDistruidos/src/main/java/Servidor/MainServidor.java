package Servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainServidor {
    public static void main(String args[]) throws Exception {
        
        Properties prop = UDPServer.getProp();
        String porta = prop.getProperty("prop.server.port");
        
        DatagramSocket serverSocket = new DatagramSocket(Integer.parseInt(porta));
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        BufferedReader fromServer =
               new BufferedReader(new InputStreamReader(System.in));

        RecebeThread rcvTrd;
        
        ExecutorService executor = Executors.newCachedThreadPool();

        System.out.println("Servidor iniciado!");
        
        rcvTrd = new RecebeThread(serverSocket);

        executor.execute(rcvTrd);
        System.out.println("FROM CLIENT: " + rcvTrd.getComandos().toString());

        executor.shutdown();
        while (!executor.awaitTermination(24L, TimeUnit.HOURS)) {
            System.out.println("Ainda não. As threads ainda estão rodando.");
        }
    }
}
