package Cliente;

import Servidor.UDPServer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.SistemasDistruidos.message.ComandRequest;
import io.grpc.SistemasDistruidos.message.ComandResponse;
import io.grpc.SistemasDistruidos.message.ComandServiceGrpc;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComandoRpcCliente implements Runnable {
    final CountDownLatch done = new CountDownLatch(1);
    ManagedChannel channel;
    String name="";
    Scanner sc = new Scanner(System.in);
    
    public static void menu(){
        System.out.println("---- Sistemas Distruibuidos ----");
        System.out.println("Escolha uma das opções abaixo ");
        System.out.println("1. Criar <chave> <valor>");
        System.out.println("2. Deletar <chave>");
        System.out.println("3. Atualizar <chave> <valor>");
        System.out.println("4. Buscar <chave>");
        System.out.println("5. Listar");
        System.out.println("6. Visualizar menu");
        System.out.println("7. Monitorar chave <chave>");
        System.out.println("8. Sair");
        System.out.print("Digite a opção:  ");
    }

    public void run() {
        try{
            Properties prop = UDPServer.getProp();
            String porta = prop.getProperty("prop.server.GRPCport");
            String ip = prop.getProperty("prop.server.GRPChost");
            channel = ManagedChannelBuilder
            .forAddress(ip, Integer.parseInt(porta))
            .usePlaintext()
            .build();
            
        } catch(Exception e){
            e.printStackTrace();
        }
        
        while(true){
            menu();
            name = sc.nextLine();
        
        ComandServiceGrpc.ComandServiceStub stub = ComandServiceGrpc.newStub(channel);
        
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
                        if(name != ""){
                            while(requestStream.isReady()){
                                // Send more messages if there are more messages to send.
                                
                                if(name.charAt(0) == '6'){
                                    menu();
                                    name = sc.nextLine();
                                    continue;
                                }

                                if(name.equalsIgnoreCase("8")){
                                    System.out.println("Encerrando!");
                                    try {
                                        channel.shutdown();
                                    } catch (Exception ex) {
                                        System.out.println("Erro ao encerrar o cli: " + ex);
                                    }
                                }
                                
                            try {
                                ComandRequest request = ComandRequest.newBuilder().setComm(name).build();
                                requestStream.onNext(request);
                                done.await();
                                requestStream.onCompleted();
                            } catch (InterruptedException ex) {
                            Logger.getLogger(ComandoRpcCliente.class.getName()).log(Level.SEVERE, null, ex);
        }   
            }
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
                System.out.println("Feito");
            }
        };
        
       stub.cmd(clientResponseObserver);

        /*try {
            done.await();
            channel.shutdown();
            channel.awaitTermination(24L, TimeUnit.HOURS);
        } catch (InterruptedException ex) {
            Logger.getLogger(ComandoRpcCliente.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }}
}