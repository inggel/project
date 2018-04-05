package Cliente;

public class ExibeComandosThread implements Runnable {
    
    public ExibeComandosThread (String resposta) {
        System.out.println("Resposta: " + resposta);
    }

    @Override
    public void run() {
        // thread
    }
    
}
