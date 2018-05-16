package Cliente;

import Servidor.UDPServer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.SistemasDistruidos.message.ComandRequest;
import io.grpc.SistemasDistruidos.message.ComandResponse;
import io.grpc.SistemasDistruidos.message.ComandServiceGrpc;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComandoRpcCliente implements Runnable {
    private static final Logger logger =
        Logger.getLogger(ComandoRpcCliente.class.getName());

    final CountDownLatch done = new CountDownLatch(1);
    ManagedChannel channel;
    String comando = "5";
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
        menu();
        while(true){
        ComandServiceGrpc.ComandServiceStub stub = ComandServiceGrpc.newStub(channel);
        
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                comando = streamReader.readLine();
                if(comando.charAt(0) == '6'){
                    menu();
                    continue;
                }
            } catch (IOException ex) {
                Logger.getLogger(ComandoRpcCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        
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
                        while(requestStream.isReady()){
                            if(comando.charAt(0) == '8'){
                                System.out.println("Encerrando!");
                                try {
                                    channel.shutdown();
                                } catch (Exception ex) {
                                    System.out.println("Erro ao encerrar o cli: " + ex);
                                }
                                break;
                            }
                            
                            ComandRequest request = ComandRequest.newBuilder().setComm(comando).build();
                            requestStream.onNext(request);
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ComandoRpcCliente.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            requestStream.onCompleted();
                           
                        }
                        
                    }
                });
            }

            @Override
            public void onNext(ComandResponse v) {
                System.out.println("GRPC: " + v.getCmd());
                requestStream.request(1);
                System.out.print("Digite a opção: ");
            }

            @Override
            public void onError(Throwable thrwbl) {
                thrwbl.printStackTrace();
                done.countDown();
            }

            @Override
            public void onCompleted() {
                //System.out.println("Feito");
                done.countDown();
            }
        };
        
            stub.cmd(clientResponseObserver);
        
            if(comando.charAt(0) == '8'){
                break;
            }
        }     
    }
}