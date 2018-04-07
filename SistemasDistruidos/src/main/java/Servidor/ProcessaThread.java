package Servidor;

import java.util.ArrayList;
import java.util.List;

public class ProcessaThread implements Runnable{
    private List<String> comandos;

    public ProcessaThread(List<String> comandos){
        this.comandos = new ArrayList<>();
        if(comandos != null && !comandos.isEmpty()){
        this.comandos.addAll(comandos);
        }
    }
    
    @Override
    public void run() {
        //To change body of generated methods, choose Tools | Templates.
    }
    
}
