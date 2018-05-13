package Servidor;

import io.grpc.SistemasDistruidos.message.ComandServiceGrpc;
import io.grpc.SistemasDistruidos.message.ComandResponse;
import io.grpc.SistemasDistruidos.message.ComandRequest;
import io.grpc.ServerBuilder;
import io.grpc.Server;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrpcReceiverThread implements Runnable {
    private BlockingQueue<String> comandos = new LinkedBlockingQueue<String>();
    private ConsumirThread conTrd;
    private Server server;
    private ExecutorService executor;
    private CRUD crud;
    private ProcessaThread procTrd;
    private ComandServiceGrpc.ComandServiceImplBase ComandServiceImpl;
    
    public GrpcReceiverThread(final ConsumirThread conTrd, final CRUD crud, final ProcessaThread procTrd){
        this.conTrd = conTrd;
        this.crud = crud;
        this.executor = Executors.newCachedThreadPool();
        this.procTrd = procTrd;
        
        this.ComandServiceImpl = new ComandServiceGrpc.ComandServiceImplBase() {
            @Override
            public StreamObserver<ComandRequest> cmd(final StreamObserver<ComandResponse> responseObserver) {
                final ServerCallStreamObserver<ComandResponse> serverCallStreamObserver =
                    (ServerCallStreamObserver<ComandResponse>) responseObserver;
                serverCallStreamObserver.disableAutoInboundFlowControl();

                final AtomicBoolean wasReady = new AtomicBoolean(false);

                serverCallStreamObserver.setOnReadyHandler(new Runnable() {
                    public void run() {
                        if (serverCallStreamObserver.isReady() && wasReady.compareAndSet(false, true)) {
                            System.out.println("READY");
                            serverCallStreamObserver.request(1);
                        }
                    }
                });

                return new StreamObserver<ComandRequest>() {
                    @Override
                    public void onNext(ComandRequest request) {
                        try {
                            String name = request.getComm();
                            procTrd.setResponseObserverGrpc(responseObserver);
                            conTrd.setCrud(crud);
                            conTrd.addComando(name);
                            
                            if (serverCallStreamObserver.isReady()) {
                                serverCallStreamObserver.request(1);
                            } else {
                                wasReady.set(false);
                            }
                        } catch (Exception ex) {
                            System.out.println(""+ex);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        responseObserver.onCompleted();
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("COMPLETED");
                        responseObserver.onCompleted();
                    }
                };
            }
        };
    }

    @Override
    public void run() {
        String port="";
        String ip = "";
        
        try {
            Properties prop = UDPServer.getProp();
            port = prop.getProperty("prop.server.GRPCport");
            ip = prop.getProperty("prop.server.GRPChost");
            
            server = ServerBuilder.forPort(Integer.parseInt(port))
                .addService(ComandServiceImpl)
                .build()
                .start();
        } catch (Exception ex) {
            Logger.getLogger(GrpcReceiverThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Server started, listening on " + port);
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
          @Override
          public void run() {
            System.err.println("*** encerrando gRPC server");
            server.shutdown();
            System.err.println("*** server shut down");
          }
        });
        try {
            server.awaitTermination();
        } catch (InterruptedException ex) {
            Logger.getLogger(GrpcReceiverThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
