package Servidor;

// Classe para gerenciar arquivo de log.

import java.io.*;
import java.util.Properties;

public class ManFileLog {
    public static Properties getProp(String caminho) throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream(
				caminho);
		props.load(file);
		return props;
   }
}
