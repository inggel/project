package Servidor;

// Classe para gerenciar arquivo de log.

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class ManFileLog {
    public static Properties getProp(String caminho) throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream(
				caminho);
		props.load(file);
		return props;
   }
    
    public static Long timeStamp(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        return timestamp.getTime();
    }
}
