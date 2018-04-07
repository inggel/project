package Cliente;

public class ExibeComandosThread implements Runnable {
    private String resposta;
    
    public ExibeComandosThread (String resposta) {
        this.resposta = resposta;
    }

    @Override
    public void run() {
        System.out.println("Resposta: " + this.resposta);
    }
    
}
