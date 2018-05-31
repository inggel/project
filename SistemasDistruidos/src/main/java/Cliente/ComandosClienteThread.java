package Cliente;

import Servidor.UDPServer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import Servidor.ProcessaThread;

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
        while(!comando.equalsIgnoreCase("8")){
            try{
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(System.in));
                byte[] sendData = new byte[1401];
                Properties prop = UDPServer.getProp();
                String porta = prop.getProperty("prop.server.port");
                InetAddress IPAddress = InetAddress.getByName(prop.getProperty("prop.server.host"));

                comando = streamReader.readLine();
                if(comando.equalsIgnoreCase("6")){
                    menu();
                }
                
                if(!comando.equals("6")){
                    ProcessaThread threadServidor = new ProcessaThread();
                    List<String> comandosCliente = new ArrayList<String>();
                    String verificaChave="";
                    String verificaValor="";

                    comandosCliente = Arrays.asList(comando.split(" "));
                    if(!comando.equals("5") && !comando.equals("8"))
                        verificaChave = comandosCliente.get(1);
                    if(comando.equals("5") || comando.equals("8"))
                        verificaChave = comando;
                    
                    verificaValor = threadServidor.valor(comandosCliente);
                    /*verifica se o tamanho é <= ao descrito no documento do projeto*/
                    if(verificaChave.length() <= 20 && verificaValor.length() <= 1400 ){
                        sendData = comando.getBytes();
                        //Envia
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, Integer.parseInt(porta));
                        clientSocket.send(sendPacket);
                    } else{
                        System.out.println("Tamanho da chave ou valor excedido");
                        menu();
                    }
                   
                }
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
        System.out.println("2. Deletar <chave>");
        System.out.println("3. Atualizar <chave> <valor>");
        System.out.println("4. Buscar <chave>");
        System.out.println("5. Listar");
        System.out.println("6. Visualizar menu");
        System.out.println("7. Monitorar chave <chave>");
        System.out.println("8. Sair");
        System.out.println("9. Criar snapShot <minutos>");
        System.out.print("Digite a opção:  ");
    }
}
