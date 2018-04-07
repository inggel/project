package Servidor;

import java.util.ArrayList;
import java.util.List;

public class LogThread implements Runnable {
    private List<String> comandos;
    
    public LogThread(List<String> comandos){
        this.comandos = new ArrayList<>();
        if(comandos != null && !comandos.isEmpty()){
            System.out.println("Log iniciado");
            this.comandos.addAll(comandos);
        }
    }
    
    @Override
    public void run() {
        while(true){
            if(comandos != null && !comandos.isEmpty()){
                System.out.println("log: "+comandos.toString());
                break;
            }
        }
    }
    
}
