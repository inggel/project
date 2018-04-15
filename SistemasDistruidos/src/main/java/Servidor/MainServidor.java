package Servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileReader;

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

       

        try{
            RecebeThread rcvTrd;
            ExecutorService executor = Executors.newCachedThreadPool();
            
            File file = new File("./properties/log.properties");

            if(file.exists()){
                ManFileLog.getProp();
                
                
                
                
                /*FileReader reader = new FileReader("./properties/log.properties");
                BufferedReader leitor = new BufferedReader(reader);
                leitor.close();
                reader.close();*/
            }
            
            System.out.println("Servidor iniciado!");
        
            rcvTrd = new RecebeThread(serverSocket, crud);
            executor.execute(rcvTrd);

            executor.shutdown();
            while (!executor.awaitTermination(24L, TimeUnit.HOURS)) {
                System.out.println("Ainda não! As threads ainda estão rodando.");
            }
            
        } catch(Exception e){
            e.printStackTrace();
        }        
    }
}
