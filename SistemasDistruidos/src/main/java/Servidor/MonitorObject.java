package Servidor;

import io.grpc.SistemasDistruidos.message.ComandResponse;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class MonitorObject {
    private io.grpc.stub.StreamObserver<ComandResponse> responseObserverGrpc = null;
    private String monChave = "";
    private DatagramPacket pacoteUdp = null;
    private DatagramSocket socketUdp = null;
    

    public io.grpc.stub.StreamObserver<ComandResponse> getResponseObserverGrpc() {
        return responseObserverGrpc;
    }

    public void setResponseObserverGrpc(io.grpc.stub.StreamObserver<ComandResponse> responseObserverGrpc) {
        this.responseObserverGrpc = responseObserverGrpc;
    }

    public String getMonChave() {
        return monChave;
    }

    public void setMonChave(String monChave) {
        this.monChave = monChave;
    }

    public DatagramPacket getPacoteUdp() {
        return pacoteUdp;
    }

    public void setPacoteUdp(DatagramPacket pacoteUdp) {
        this.pacoteUdp = pacoteUdp;
    }

    public DatagramSocket getSocketUdp() {
        return socketUdp;
    }

    public void setSocketUdp(DatagramSocket socketUdp) {
        this.socketUdp = socketUdp;
    }
    
}
