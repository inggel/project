package Cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
public class ComandosClienteThread implements Runnable {
    private String comando;
    
    public ComandosClienteThread(){
        // Construtor
    }

    @Override
    public void run() {
        try{
            System.out.println("Menu: ");
            System.out.println("1- create");
            System.out.println("Digite: ");
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(System.in));
            while ((comando = streamReader.readLine()) != "sair") {
                System.out.println("sair");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * @return the comando
     */
    public String getComando() {
        return comando;
    }

    /**
     * @param comando the comando to set
     */
    public void setComando(String comando) {
        this.comando = comando;
    }
}
