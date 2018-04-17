package Servidor;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LogThread implements Runnable {
    private List<String> comandos;
            
    public LogThread(String comando){
        this.comandos = new ArrayList<>();
        this.comandos.add(comando);
    }
    
    @Override
    public void run() {
        while(true){
            if(comandos != null && !comandos.isEmpty()){
                
                try{ 
                    
                    FileOutputStream fileout = new FileOutputStream(
                                    "./properties/log.properties", true);
                     Properties prop = ManFileLog.getProp();
                    for (String comando : comandos) {
                        prop.put("comando"+java.util.UUID.randomUUID(), comandos.toString()
                                .replaceAll("\u0000", "") /* removes NUL chars */
                                .replaceAll("\\u0000", "") /* removes backslash+u0000 */);
                    }
                    
                    prop.store(fileout, "Log dos comandos enviados pelo cliente");
                    fileout.flush();
                    
                } catch(Exception ex){
                    ex.printStackTrace();
                }
                
                System.out.println("log: "+comandos.toString());
                break;
            }
        }
    }
    
}
