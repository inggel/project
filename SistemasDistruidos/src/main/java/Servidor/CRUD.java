package Servidor;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CRUD {
    
    private Map<BigInteger, String> mapa = new HashMap<>();
    
    public boolean create(BigInteger chave, String valor){
        if((!mapa.containsKey(chave)) && (!valor.equals("")) && (!valor.equals(" ")) && valor != null){
            getMapa().put(chave, valor);
            return true;
        }
        return false;
    }
    
    public boolean update(BigInteger chave, String valor){
        if(getMapa().containsKey(chave) && (!valor.equals("")) && (!valor.equals(" "))){
            getMapa().replace(chave, valor);
            return true;
        }
        return false;
    }
    
    public boolean delete(BigInteger chave){
        if(getMapa().containsKey(chave) && (!chave.equals("")) && (!chave.equals(" "))){
            getMapa().remove(chave);
            return true;
        }
        return false;
    }
    
    /*public boolean delete(String valor){
        if(getMapa().containsKey(valor) && (!valor.equals("")) && (!valor.equals(" "))){
            getMapa().remove(valor);
            return true;
        }
        return false;
    }*/
    
    public String search(BigInteger chave){
        if(getMapa().containsKey(chave) && (!chave.equals("")) && (!chave.equals(" "))){
            return getMapa().get(chave);
        }
        return "";
    }
    
    /*public String search(String valor){
        if(getMapa().containsValue(valor) && (!valor.equals("")) && (!valor.equals(" "))){
            return getMapa().get(valor);
        }
        return "";
    }*/
    
    public ArrayList<String> read(){
        ArrayList<String> valores = new ArrayList<>();
        for(BigInteger bi : getMapa().keySet()){
            valores.add(getMapa().get(bi));
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

    public Map<BigInteger, String> getMapa() {
        return mapa;
    }

    public void setMapa(Map<BigInteger, String> mapa) {
        this.mapa = mapa;
    }
     
}
