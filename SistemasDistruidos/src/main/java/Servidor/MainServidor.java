package Servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class MainServidor {
    public static void main(String args[]) throws Exception {
        
        List<String> inst = new ArrayList<>();
        Properties prop = UDPServer.getProp();
        Properties recarrega;
        ProcessaThread pt = new ProcessaThread();
        
        String porta = prop.getProperty("prop.server.port");
        CRUD crud = new CRUD();
        Map<BigInteger, String> map;
                
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
                recarrega = ManFileLog.getProp();
                map = new HashMap<BigInteger, String>((Map) recarrega);
                                
                Set propertySet = map.entrySet();
                for(Object o: propertySet){
                    Map.Entry entry = (Map.Entry) o;
                    inst = Arrays.asList(o.toString().split("\\["));
                    crud = pt.processaComando(inst, crud);
                }
            }
            
            System.out.println("Servidor iniciado!");
            AtomicInteger seq = new AtomicInteger();
            rcvTrd = new RecebeThread(serverSocket, crud, seq);
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
