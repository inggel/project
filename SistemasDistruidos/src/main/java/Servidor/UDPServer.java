package Servidor;

import java.io.*;
import java.util.Properties;

public class UDPServer
{
    
   public static Properties getProp() throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream(
				"./properties/config.properties");
		props.load(file);
		return props;
   }
    
}