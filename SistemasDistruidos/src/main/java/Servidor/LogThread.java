package Servidor;

import java.util.List;

public class LogThread implements Runnable {
    private List<String> comandos;
    
    public LogThread(List<String> comandos){
        this.comandos.addAll(comandos);
    }
    
    @Override
    public void run() {
        //To change body of generated methods, choose Tools | Templates.
    }
    
}
