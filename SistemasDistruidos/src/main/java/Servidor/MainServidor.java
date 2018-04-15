package Servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainServidor {
    public static void main(String args[]) throws Exception {
        
        Properties prop = UDPServer.getProp();
        String porta = prop.getProperty("prop.server.port");
        CRUD crud = new CRUD();
         
        DatagramSocket serverSocket = new DatagramSocket(Integer.parseInt(porta));
        byte[] receiveData = new byte[1400];
        byte[] sendData = new byte[1400];
        BufferedReader fromServer =
               new BufferedReader(new InputStreamReader(System.in));

        RecebeThread rcvTrd;
        
        ExecutorService executor = Executors.newCachedThreadPool();

        System.out.println("Servidor iniciado!");
        
        rcvTrd = new RecebeThread(serverSocket, crud);

        executor.execute(rcvTrd);

        executor.shutdown();
        while (!executor.awaitTermination(24L, TimeUnit.HOURS)) {
            System.out.println("Ainda não! As threads ainda estão rodando.");
        }
    }
}
