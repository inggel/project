package Cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ComandosClienteThread implements Runnable {
    private byte[] sendData = new byte[1024];
    
    Scanner sc = new Scanner(System.in);
    
    public ComandosClienteThread(){
        // Construtor
    }

    @Override
    public void run() {
        try{
            System.out.println("Digite: ");
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            this.sendData = inFromUser.readLine().getBytes();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public byte[] getSendData() {
        return this.sendData;
    }

    public void setSendData(byte[] sendData) {
        this.sendData = sendData;
    }
    
}
