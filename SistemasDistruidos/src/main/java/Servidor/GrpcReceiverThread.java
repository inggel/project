package Servidor;

import io.grpc.SistemasDistruidos.message.ComandServiceGrpc;
import io.grpc.SistemasDistruidos.message.ComandResponse;
import io.grpc.SistemasDistruidos.message.ComandRequest;
import io.grpc.ServerBuilder;
import io.grpc.Server;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrpcReceiverThread implements Runnable {
    private BlockingQueue<String> comandos = new LinkedBlockingQueue<String>();
    private ConsumirThread conTrd;
    private Server server;
    private ExecutorService executor;
    private CRUD crud;
    
    public GrpcReceiverThread(ConsumirThread conTrd, CRUD crud){
        this.conTrd = conTrd;
        this.crud = crud;
        this.executor = Executors.newCachedThreadPool();
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
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
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
        
        @Override
        public void cmd(ComandRequest request,
        io.grpc.stub.StreamObserver<ComandResponse> responseObserver){
            ComandResponse rsp = ComandResponse.newBuilder().setCmd("Grpc> ").build();
            
            conTrd.addComando(request.getComm());
            conTrd.setCrud(crud);
            executor.execute(conTrd);
                                
            responseObserver.onNext(rsp);
            responseObserver.onCompleted();
        }
    }
}
