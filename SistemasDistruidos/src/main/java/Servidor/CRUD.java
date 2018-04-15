package Servidor;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CRUD {
    
    Map<BigInteger, String> mapa = new HashMap<>();
    
    public boolean create(BigInteger chave, String valor){
        if((!mapa.containsKey(chave)) && (!valor.equals(""))){
            mapa.put(chave, valor);
            return true;
        }
        return false;
    }
    
    public boolean update(BigInteger chave, String valor){
        if(mapa.containsKey(chave)){
            mapa.replace(chave, valor);
            return true;
        }
        return false;
    }
    
    public boolean delete(BigInteger chave, String valor){
        if(mapa.containsKey(chave)){
            mapa.remove(chave, valor);
            return true;
        }
        return false;
    }
    
    public String search(BigInteger chave){
        if(mapa.containsKey(chave)){
            return mapa.get(chave);
        }
        return "";
    }
    
    public ArrayList<String> read(){
        ArrayList<String> valores = new ArrayList<>();
        for(BigInteger bi : mapa.keySet()){
            valores.add(mapa.get(bi));
        }
        return valores;
    }
    
    public static void salvaArq(BigInteger chave, String valor){
        try{ 
                    FileOutputStream fileout = new FileOutputStream(
                                    "./properties/base.properties", true);
                    Properties prop = ManFileLog.getProp();
                   
                    prop.put("chave", chave.toString()
                             .replaceAll("\u0000", "") /* removes NUL chars */
                            .replaceAll("\\u0000", "") /* removes backslash+u0000 */);
                    
                    prop.put("valor", valor.toString()
                             .replaceAll("\u0000", "") /* removes NUL chars */
                            .replaceAll("\\u0000", "") /* removes backslash+u0000 */);
                    
                    
                    prop.store(fileout, "Base de dados");
                    fileout.flush();
                    
                } catch(Exception ex){
                    ex.printStackTrace();
                }
    }
     
}
