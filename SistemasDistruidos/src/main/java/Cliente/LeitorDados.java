package Cliente;

import java.math.BigInteger;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LeitorDados {
    
    private Scanner sc = new Scanner(System.in);
    
    public int lerInt(){
        int valor;
        
        try{
            valor = sc.nextInt();
            sc.nextLine();
            
        }catch(InputMismatchException e){
            System.out.println("Apenas numeros são aceitos.");
            System.out.print("Digite novamente: ");
            sc.nextLine();
            valor = lerInt();
            System.out.println("");
            
        }
        return valor;
    }
    
    public BigInteger lerBigInteger(){
        BigInteger valor;
        
        try{
            valor = sc.nextBigInteger();
            sc.nextLine();
            
        }catch(InputMismatchException e){
            System.out.println("Apenas numeros são aceitos.");
            System.out.print("Digite novamente: ");
            sc.nextLine();
            valor = lerBigInteger();
            System.out.println("");
            
        }
        return valor;
    }
    
    public Long lerLong(){
        Long valor;
        
        try{
            valor = sc.nextLong();
            sc.nextLine();
            
        }catch(InputMismatchException e){
            System.out.println("Apenas numeros são aceitos.");
            System.out.print("Digite novamente: ");
            sc.nextLine();
            valor = lerLong();
            System.out.println("");

        }
        return valor;
    }
    
    public Date lerData(){
        String data = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date dataSaida;
        
        try {
            data = sc.nextLine();
            dataSaida = new java.sql.Date(sdf.parse(data).getTime());
            
        } catch (ParseException ex) {
            System.out.println("Apenas datas validas sao aceitas!");
            System.out.print("Digite novamente: ");
            dataSaida = lerData();
            System.out.println("");
        }
        
        return dataSaida;
    }
}
