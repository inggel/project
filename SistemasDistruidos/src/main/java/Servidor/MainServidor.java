package Servidor;

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

public class MainServidor {
    public static void main(String args[]) throws Exception {
        // Lista com os comandos do log
        List<String> inst = new ArrayList<>();
        // Arquivo de propriedade porta e ip
        Properties prop = UDPServer.getProp();
        // Propertie para o arquivo de log
        Properties recarrega;
        // Mapa e crud para converter e armazenar os dados do arquivo de log
        Map<BigInteger, String> map;
        CRUD crud = new CRUD();
        
        // Thread que ira receber os comandos do cliente
        RecebeThread rcvTrd;
        
        String porta = prop.getProperty("prop.server.port");
        DatagramSocket serverSocket = new DatagramSocket(Integer.parseInt(porta));
        byte[] receiveData = new byte[1400];
        byte[] sendData = new byte[1400];

        try{
            // Carrega o log
            ProcessaThread pt = new ProcessaThread();        
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
            // Inicia thread para receber comandos dos clientes
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
