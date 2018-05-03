package Cliente;

import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class MainCliente
{
    public static void main(String args[]) throws Exception {
       
        ExecutorService executor = Executors.newCachedThreadPool();
        DatagramSocket clientSocket = new DatagramSocket();
       
        ComandosClienteThread cmdcli = new ComandosClienteThread(clientSocket);
        ExibeComandosThread exibCmd = new ExibeComandosThread(clientSocket);
        //ComandoRpcCliente cmdCliRpc = new ComandoRpcCliente("localhost", 1235);
        
        //cmdCliRpc.sndComand();
        
        executor.execute(cmdcli);
        executor.execute(exibCmd);
        
        executor.shutdown();
        while (!executor.awaitTermination(24L, TimeUnit.HOURS)) {
            System.out.println("Not yet. Still waiting for termination");
        }
   }
}