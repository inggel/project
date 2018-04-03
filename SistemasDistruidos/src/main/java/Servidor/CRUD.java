package Servidor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CRUD {
    
    Map<BigInteger, String> mapa = new HashMap<>();
    
    public boolean create(BigInteger chave, String valor){
        if((!mapa.containsKey(chave)) && (!valor.equals("")) && (valor.length() < 3000)){
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
    
    public String read(BigInteger chave){
        if(mapa.containsKey(chave)){
            return mapa.get(chave);
        }
        return "";
    }
    
    public ArrayList<String> list(){
        ArrayList<String> valores = new ArrayList<>();
        for(BigInteger bi : mapa.keySet()){
            valores.add(mapa.get(bi));
        }
        return valores;
    }
}
