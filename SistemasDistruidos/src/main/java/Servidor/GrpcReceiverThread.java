package Servidor;

import io.grpc.SistemasDistruidos.message.ComandServiceGrpc;
import io.grpc.SistemasDistruidos.message.ComandResponse;
import io.grpc.SistemasDistruidos.message.ComandRequest;
import io.grpc.ServerBuilder;
import io.grpc.Server;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
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
    
    public GrpcReceiverThread(ConsumirThread conTrd, CRUD crud, ProcessaThread procTrd){
        this.conTrd = conTrd;
        this.crud = crud;
        this.executor = Executors.newCachedThreadPool();
        this.procTrd = procTrd;
    }

    @Override
    public void run() {
        int port = 1235;
        
        try {
            server = ServerBuilder.forPort(port)
                .addService(new ComandServiceImpl())
                .build()
                .start();
        } catch (Exception ex) {
            Logger.getLogger(GrpcReceiverThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Server started, listening on " + port);
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
          @Override
          public void run() {
            System.err.println("*** shutting down gRPC server");
            this.stop();
            System.err.println("*** server shut down");
          }
        });
    }
    
    private void stop() {
        if (server != null) {
          server.shutdown();
        }
    }
    
    class ComandServiceImpl extends ComandServiceGrpc.ComandServiceImplBase {
        
//        @Override
//        public void cmd(ComandRequest request,
//        io.grpc.stub.StreamObserver<ComandResponse> responseObserver){
//            conTrd.addComando(request.getComm());
//            conTrd.setCrud(crud);
//            
//            procTrd.setResponseObserverGrpc(responseObserver);
//        }
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
            
            // Give gRPC a StreamObserver that can observe and process incoming requests.
            return new StreamObserver<ComandRequest>() {
                @Override
                public void onNext(ComandRequest request) {
                    // Process the request and send a response or an error.
                    try {
                        // Accept and enqueue the request.
                        String name = request.getComm();

                        // Simulate server "work"
                        //Thread.sleep(100);

                        // Send a response.
                        procTrd.setResponseObserverGrpc(responseObserver);
                        conTrd.setCrud(crud);
                        conTrd.addComando(name);

                        
//                        String message = name;
//                        System.out.println("<-- " + message);
//                        ComandResponse reply = ComandResponse.newBuilder().setCmd(message).build();
//                        responseObserver.onNext(reply);
                        
                        if (serverCallStreamObserver.isReady()) {
                            serverCallStreamObserver.request(1);
                        } else {
                            // If not, note that back-pressure has begun.
                            wasReady.set(false);
                        }
                    } catch (Exception ex) {
                        System.out.println(""+ex);
                    }
                }
                
                @Override
                public void onError(Throwable t) {
                    // End the response stream if the client presents an error.
                    t.printStackTrace();
                    responseObserver.onCompleted();
                }

                @Override
                public void onCompleted() {
                    // Signal the end of work when the client ends the request stream.
                    System.out.println("COMPLETED");
                    responseObserver.onCompleted();
                }
            };
        }
    }
}
