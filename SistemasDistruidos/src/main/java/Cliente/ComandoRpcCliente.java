package Cliente;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.SistemasDistruidos.message.ComandRequest;
import io.grpc.SistemasDistruidos.message.ComandResponse;
import io.grpc.SistemasDistruidos.message.ComandServiceGrpc;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ComandoRpcCliente implements Runnable {
    private final ManagedChannel channel;
    private final ComandServiceGrpc.ComandServiceBlockingStub blockingStub;
    
    public ComandoRpcCliente(String host, int port){
        this(ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build());
    }
    
    ComandoRpcCliente(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = ComandServiceGrpc.newBlockingStub(channel);
    }
    
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
        
    public static void menu(){
        System.out.println("---- Sistemas Distruibuidos ----");
        System.out.println("Escolha uma das opções abaixo ");
        System.out.println("1. Criar <chave> <valor>");
        System.out.println("2. Deletar <chave>");
        System.out.println("3. Atualizar <chave> <valor>");
        System.out.println("4. Buscar <chave>");
        System.out.println("5. Listar");
        System.out.println("6. Visualizar menu");
        System.out.println("7. Sair");
        System.out.print("Digite a opção:  ");
    }

    public void run() {
        String name = "";
        Scanner sc = new Scanner(System.in);
        menu();

        while(!name.equalsIgnoreCase("7")){
            name = sc.nextLine();
            
            if(name.equalsIgnoreCase("6")){
                menu();
                continue;
            }
            
            if(name.equalsIgnoreCase("7")){
                System.out.println("Encerrando!");
            }
            
            ComandRequest request = ComandRequest.newBuilder().setComm(name).build();
            ComandResponse response;

            try {
              response = blockingStub.cmd(request);
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
              return;
            }
            System.out.println("sv: " + response.getCmd());
        }
    }
}
