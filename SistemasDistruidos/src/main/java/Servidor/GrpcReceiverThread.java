package Servidor;

import io.grpc.Server;
import io.grpc.SistemasDistruidos.message.ComandServiceGrpc;
import io.grpc.SistemasDistruidos.message.ComandResponse;
import io.grpc.SistemasDistruidos.message.ComandRequest;
import io.grpc.ServerBuilder;
import io.grpc.Server;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrpcReceiverThread implements Runnable {
    private Queue<String> comandos = new LinkedList<String>();
    private ConsumirThread conTrd;
    private Server server;
    
    public GrpcReceiverThread(ConsumirThread conTrd ){
        this.conTrd = conTrd;
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
    
    static class ComandServiceImpl extends ComandServiceGrpc.ComandServiceImplBase {
        
        @Override
        public void cmd(ComandRequest request,
        io.grpc.stub.StreamObserver<ComandResponse> responseObserver){
            ComandResponse rsp = ComandResponse.newBuilder().setCmd("sv " + request.getComm()).build();
            responseObserver.onNext(rsp);
            responseObserver.onCompleted();
        }
    }
}
