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
        List<String> inst = new ArrayList<String>();
        // Arquivo de propriedade porta e ip
        Properties prop = UDPServer.getProp();
        // Propertie para o arquivo de log e snapshot
        Properties recarregaLog;
        Properties recarregaSnap;
        // Mapa e crud para converter e armazenar os dados do arquivo de log
        Map<BigInteger, String> map;
        CRUD crud = new CRUD();
        
        // Thread que ira receber os comandos do cliente
        RecebeThread rcvTrd;
        
        String porta = prop.getProperty("prop.server.port");
        DatagramSocket serverSocket = new DatagramSocket(Integer.parseInt(porta));
        byte[] receiveData = new byte[1401];
        byte[] sendData = new byte[1401];

        try{
            // Carrega o log
            ProcessaThread pt = new ProcessaThread();        
            ExecutorService executor = Executors.newCachedThreadPool();
            
            ArrayList<File> arquivos = new ArrayList<File>();
            
            File logFile = new File("./properties/log.properties");
            File diretorio = new File("./properties/SnapShot/");
            File snapFile = new File("");
            
            if(diretorio.exists()){
                File files[] = diretorio.listFiles();
                if(files.length > 0)
                    snapFile = files[files.length-1];
            }
            
            if(logFile.exists()){
                arquivos.add(logFile);
            }
            
            if(snapFile.exists()){
                arquivos.add(snapFile);
            }
            
            map = new HashMap<BigInteger, String>();
             
            for(File f : arquivos){
                Set propertySnap = ManFileLog.getProp(f.getAbsolutePath()).entrySet();
                
                for(Object o : propertySnap){
                    Map.Entry entry = (Map.Entry) o;
                    BigInteger chave = new BigInteger(entry.getValue().toString().substring(3).split(" ")[0]);
                    String comando = entry.getValue().toString().substring(1, entry.getValue().toString().indexOf(" "));
                    String valor = comando +" "+entry.getValue().toString().substring(5, entry.getValue().toString().length() - 1);
                    
                    map.put(chave, valor);
                }
            }
             
            Set propertySet = map.entrySet();
            for(Object o: propertySet){
                Map.Entry entry = (Map.Entry) o;
                String cmd = entry.getValue().toString().substring(0, entry.getValue().toString().indexOf(" "));
                String valor = entry.getValue().toString().substring(entry.getValue().toString().indexOf(" ")+1, entry.getValue().toString().length());
                String chave = entry.getKey().toString();
                
                inst.add(cmd);
                inst.add(chave);
                inst.add(valor);
                
                crud = pt.processaComando(inst, crud);
                inst.clear();
            }
            
// Inicia thread para receber comandos dos clientes
            System.out.println("Servidor iniciado!");
            
            LogThread logTrd = new LogThread();
            ProcessaThread procTrd  = new ProcessaThread();
            ConsumirThread conTrd = new ConsumirThread(logTrd, procTrd);
            rcvTrd = new RecebeThread(conTrd, serverSocket, crud);
            GrpcReceiverThread grpcRcv = new GrpcReceiverThread(conTrd, crud, procTrd);
                        
            executor.execute(rcvTrd);
            executor.execute(conTrd);
            executor.execute(logTrd);
            executor.execute(procTrd);
            executor.execute(grpcRcv);

            executor.shutdown();
            while (!executor.awaitTermination(24L, TimeUnit.HOURS)) {
                System.out.println("Ainda não! As threads ainda estão rodando.");
            }
            
        } catch(Exception e){
            e.printStackTrace();
        }        
    }
}
