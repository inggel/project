package com.mycompany.sistemasdistruidos.servidor;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

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