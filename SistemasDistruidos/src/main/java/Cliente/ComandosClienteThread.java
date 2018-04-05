package Cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ComandosClienteThread implements Runnable {
    private byte[] sendData = new byte[1024];
    
    Scanner sc = new Scanner(System.in);
    
    public ComandosClienteThread(){
        try{
            System.out.println("digite:");
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            sendData = inFromUser.readLine().getBytes();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Metodo run
    }

    public byte[] getSendData() {
        return sendData;
    }

    public void setSendData(byte[] sendData) {
        this.sendData = sendData;
    }
    
}
