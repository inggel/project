package Cliente;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.SistemasDistruidos.message.ComandRequest;
import io.grpc.SistemasDistruidos.message.ComandResponse;
import io.grpc.SistemasDistruidos.message.ComandServiceGrpc;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComandoRpcCliente implements Runnable {
//    private final ManagedChannel channel;
//    private final ComandServiceGrpc.ComandServiceBlockingStub blockingStub;
//    
//    public ComandoRpcCliente(String host, int port){
//        this(ManagedChannelBuilder.forAddress(host, port)
//                .usePlaintext(true)
//                .build());
//    }
//    
//    ComandoRpcCliente(ManagedChannel channel) {
//        this.channel = channel;
//        blockingStub = ComandServiceGrpc.newBlockingStub(channel);
//    }
//    
//    public void shutdown() throws InterruptedException {
//        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
//    }
    final CountDownLatch done = new CountDownLatch(1);
     ManagedChannel channel = ManagedChannelBuilder
        .forAddress("localhost", 1235)
        .usePlaintext()
        .build();
    ComandServiceGrpc.ComandServiceStub stub = ComandServiceGrpc.newStub(channel);
    
    public static void menu(){
        System.out.println("---- Sistemas Distruibuidos ----");
        System.out.println("Escolha uma das opções abaixo ");
        System.out.println("1. Criar <chave> <valor>");
        System.out.println("2. Deletar <chave>");
        System.out.println("3. Atualizar <chave> <valor>");
        System.out.println("4. Buscar <chave>");
        System.out.println("5. Listar");
        System.out.println("6. Visualizar menu");
        System.out.println("7. Monitorar chave");
        System.out.println("8. Sair");
        System.out.print("Digite a opção:  ");
    }

    public void run() {
//        String name = "";
//        Scanner sc = new Scanner(System.in);
//        menu();
//
//        while(!name.equalsIgnoreCase("7")){
//            name = sc.nextLine();
//            
//            if(name.equalsIgnoreCase("6")){
//                menu();
//                continue;
//            }
//            
//            if(name.equalsIgnoreCase("7")){
//                System.out.println("Encerrando!");
//                try {
//                    this.shutdown();
//                } catch (InterruptedException ex) {
//                    System.out.println("Erro ao encerrar o cli: " + ex);
//                }
//            }
//            
//            ComandRequest request = ComandRequest.newBuilder().setComm(name).build();
//            ComandResponse response;
//
//            try {
//              response = blockingStub.cmd(request);
//            } catch (Exception e) {
//                System.out.println("Erro: " + e.getMessage());
//              return;
//            }
//            System.out.println("Grpc: " + response.getCmd());
//            System.out.print("Digite a opção: ");

        ClientResponseObserver<ComandRequest, ComandResponse> clientResponseObserver =
            new ClientResponseObserver<ComandRequest, ComandResponse>() {

            ClientCallStreamObserver<ComandRequest> requestStream;

            @Override
            public void beforeStart(final ClientCallStreamObserver<ComandRequest> requestStream) {
                this.requestStream = requestStream;
                requestStream.disableAutoInboundFlowControl();
                
                requestStream.setOnReadyHandler(new Runnable() {
                    
                    @Override
                    public void run() {
                         // Start generating values from where we left off on a non-gRPC thread.
                        menu();
                        Scanner sc = new Scanner(System.in);
                        String name = "";
                        while(!name.equalsIgnoreCase("7") && requestStream.isReady()){
                            // Send more messages if there are more messages to send.
                            name = sc.nextLine();

                            if(name.equalsIgnoreCase("6")){
                                menu();
                                continue;
                            }

                            if(name.equalsIgnoreCase("7")){
                                System.out.println("Encerrando!");
                                try {
                                    channel.shutdown();
                                } catch (Exception ex) {
                                    System.out.println("Erro ao encerrar o cli: " + ex);
                                }
                            }
                            
                            ComandRequest request = ComandRequest.newBuilder().setComm(name).build();
                            requestStream.onNext(request);
                            //requestStream.onCompleted();
                        }
                    }
                });
            }

            @Override
            public void onNext(ComandResponse v) {
                System.out.println("<-- " + v.getCmd());
                requestStream.request(1);
            }

            @Override
            public void onError(Throwable thrwbl) {
                thrwbl.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("All Done");
            }
        };
        
        stub.cmd(clientResponseObserver);

        try {
            done.await();
            channel.shutdown();
            channel.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(ComandoRpcCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
}