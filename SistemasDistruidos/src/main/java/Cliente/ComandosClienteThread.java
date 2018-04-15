package Cliente;

import Servidor.UDPServer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;

public class ComandosClienteThread implements Runnable {
    private String comando;
    private DatagramSocket clientSocket;
    
    public ComandosClienteThread(){}
    
    public ComandosClienteThread(DatagramSocket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        comando = "";
        menu();
        while(!comando.equalsIgnoreCase("7")){
            try{
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(System.in));
                byte[] sendData = new byte[1400];
                Properties prop = UDPServer.getProp();
                String porta = prop.getProperty("prop.server.port");
                InetAddress IPAddress = InetAddress.getByName(prop.getProperty("prop.server.host"));

                comando = streamReader.readLine();
                if(comando.equalsIgnoreCase("6")){
                    menu();
                }
                
                sendData = comando.getBytes();

                //Envia
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, Integer.parseInt(porta));
                clientSocket.send(sendPacket);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }
    
    public static void menu(){
        System.out.println("---- Sistemas Distruibuidos ----");
        System.out.println("Escolha uma das opções abaixo ");
        System.out.println("1. Criar <chave> <valor>");
        System.out.println("2. Deletar <chave> <valor>");
        System.out.println("3. Atualizar <chave> <valor>");
        System.out.println("4. Buscar <chave>");
        System.out.println("5. Listar");
        System.out.println("6. Visualizar menu");
        System.out.println("7. Sair");
        System.out.print("Digite a opção:  ");
    }
}
