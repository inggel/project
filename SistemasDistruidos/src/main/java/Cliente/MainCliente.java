package Cliente;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class MainCliente
{
    public static void main(String args[]) throws Exception {
       
        ExecutorService executor = Executors.newCachedThreadPool();
            
        System.out.println("---- Sistemas Distruibuidos ----");
        System.out.print("CLIENT: ");

        ComandosClienteThread cmdcli = new ComandosClienteThread();
        ExibeComandosThread exibCmd = new ExibeComandosThread();
        
        executor.execute(cmdcli);
        executor.execute(exibCmd);
        
        executor.shutdown();
        while (!executor.awaitTermination(24L, TimeUnit.HOURS)) {
            System.out.println("Not yet. Still waiting for termination");
        }
   }
}