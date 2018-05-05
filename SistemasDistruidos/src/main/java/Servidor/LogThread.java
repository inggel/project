package Servidor;

import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LogThread implements Runnable {
    private BlockingQueue<String> comandos = new LinkedBlockingQueue<String>();
    int i=0;
            
    public LogThread(){
        // ctor
    }
    
    @Override
    public void run() {
        while(true){
            if(comandos != null && !comandos.isEmpty()){
                
                try{ 
                    Iterator<String> c = comandos.iterator();
                
                    while(c.hasNext()){
                        String cmd = c.next();
                        
                        FileOutputStream fileout = new FileOutputStream(
                                    "./properties/log.properties", true);
                        Properties prop = ManFileLog.getProp();
                        prop.clear();
                                                
                        prop.put("comando"+java.util.UUID.randomUUID(), ("[" + cmd + "]")
                                .replaceAll("\u0000", "") /* removes NUL chars */
                                .replaceAll("\\u0000", "") /* removes backslash+u0000 */);

                        //System.out.println("log: "+comandos.toString());
                        prop.store(fileout, "Log dos comandos enviados pelo cliente");
                        fileout.flush();
                        
                        c.remove();
                    }                 
                    
                } catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public void addComando(String comando){
        comandos.add(comando);
    }
    
}
