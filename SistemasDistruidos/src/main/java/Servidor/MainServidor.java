package Servidor;

import java.net.DatagramSocket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MainServidor {
    public static void main(String args[]) throws Exception {
        // Lista com os comandos do log
        List<String> inst = new ArrayList<String>();
        // Arquivo de propriedade porta e ip
        Properties prop = UDPServer.getProp();
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
            
            if(snapFile.exists()){
                arquivos.add(snapFile);
            }
            
            if(logFile.exists()){
                arquivos.add(logFile);
            }
            
            
            map = new HashMap<BigInteger, String>();
            TreeMap<BigInteger, String> mapOrdenado = new TreeMap<BigInteger, String>();
            ArrayList<Registro> registros = new ArrayList<Registro>();
            for(File f : arquivos){
                Set propertySnap = ManFileLog.getProp(f.getAbsolutePath()).entrySet();
                               
                for(Object o : propertySnap){
                    Map.Entry entry = (Map.Entry) o;
                    String  entrada = entry.getValue().toString();
                    String partes[] = entrada.split(" ");
                    String chave = partes[1];
                    String comando = partes[0].replace("[", "");
                    String valor = "";
                    
                    for(int i = 2; i < partes.length; i++)
                        valor += partes[i] + " ";
                    
                    valor = valor.substring(0, valor.length() - 2);
                    
                    registros.add(new Registro(Long.parseLong(entry.getKey().toString()), comando, new BigInteger(chave), valor));                    
                }
            }
            
            Collections.sort(registros, new Comparator<Registro>() {
                public int compare(Registro r1, Registro r2) {
                    Long s1 = r1.getDataCriacao();
                    Long s2 = r2.getDataCriacao();
                    return (s1 < s2 ? -1 : (s1 == s2 ? 1 : 0));
                }
            });
            
            for(Registro r: registros){                                
                inst.add(r.getChave().toString());
                inst.add(r.getValor());
                
                crud = pt.processaComando(r.getComando(), inst, crud);
                inst.clear();
            }
            
// Inicia thread para receber comandos dos clientes
            System.out.println("Servidor iniciado!");
            
            LogThread logTrd = new LogThread();
            ProcessaThread procTrd  = new ProcessaThread();
            ConsumirThread conTrd = new ConsumirThread(logTrd, procTrd);
            rcvTrd = new RecebeThread(conTrd, serverSocket, crud);
            GrpcReceiverThread grpcRcv = new GrpcReceiverThread(conTrd, crud, procTrd);
            SnapShot ss = new SnapShot(crud);
                        
            executor.execute(rcvTrd);
            executor.execute(conTrd);
            executor.execute(logTrd);
            executor.execute(procTrd);
            executor.execute(grpcRcv);
            executor.execute(ss);

            executor.shutdown();
            while (!executor.awaitTermination(24L, TimeUnit.HOURS)) {
                System.out.println("Ainda não! As threads ainda estão rodando.");
            }
            
        } catch(Exception e){
            e.printStackTrace();
        }        
    }
}
