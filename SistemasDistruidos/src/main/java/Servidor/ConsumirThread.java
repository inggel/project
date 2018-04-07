package Servidor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumirThread implements Runnable {
    private List<String> comandos;
    private ExecutorService executor;
    
    public ConsumirThread(List<String> comandos){
        this.comandos = new ArrayList<>();
        if(comandos != null && !comandos.isEmpty()){
            this.comandos.addAll(comandos);
            this.executor = Executors.newCachedThreadPool();
        }
    }
    
    @Override
    public void run() {
        while(true){
            if(comandos != null && !comandos.isEmpty()){
                LogThread logTrd = new LogThread(this.comandos);
                ProcessaThread procTrd = new ProcessaThread(this.comandos);

                //this.executor.execute(procTrd);
                System.out.println("iniciando log");
                this.executor.execute(logTrd);
                break;
            }
        }
    }
    
}
