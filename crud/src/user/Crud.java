package user;

import java.io.*;
import util.*;
import crud.*;
import java.util.*;

public class Crud 
{
    /**
     * Método construtor da interface do CRUD
     */
    public Crud() {
        
    }
    
    public static Produto readProduct()
    {
        String nome, descricao;
        float preco = 0.0F;

        nome = IO.readLine("\nDigite o nome do produto: ");
            
        descricao = IO.readLine("\nDigite a descrição do produto: ");
            
        preco = Float.parseFloat( IO.readLine("\nInforme o preço do produto: ") );
            
        return new Produto(nome, descricao, preco); 
    }
    
    /**
    * Metodo para criar um novo registro de produto
    *
    * @param - arquivo destino
    */
    public static void inserir(Arquivo arquivo)
    {  
        try 
        {
                   
            arquivo.writeObject( readProduct() );

            IO.println("\nSeu produto foi cadastrado com sucesso! :D\n");
          
        }catch(IOException ioe) {ioe.printStackTrace(); }
      
    }//end inserir()
   
   
    public static void alterar(Arquivo arquivo, int id)
    {  
        try 
        {
            arquivo.writeObject( readProduct() );

            IO.println("\nSeu produto foi alterado com sucesso! :D\n");

        }catch(IOException ioe) {ioe.printStackTrace(); }

    }//end alterar()
    
    public static void menu() throws IOException
    {
        //File file = new File("produto.db");
        Arquivo arquivo = new Arquivo("produtos.db");
        //ArrayList<Produto> list = new ArrayList<Produto>();
        int selecao = 12;
        IO.println("Olá, meu nobre!\n");

        //TESTAR SE E' PARA SAIR
        while(selecao != 0)
        {    
            //Interface de entrada
            selecao =
            Integer.parseInt
            (
                IO.readLine
                (
                	"Qual das seguintes operações o senhor deseja realizar?" + 
                    "\nDigite: " + 
                    "\n1 para inclusão;" +
                    "\n2 para alteração;" +
                    "\n3 para exclusão;" +
                    "\n4 para consulta de produtos;" +
                    "\n0 para sair." +
                    "\n" +
                    "\nOperação: "
                )
            ); //entrada do codigo de selecao de acao         
             
            //INCLUSAO
            if      (selecao == 1) { inserir(arquivo); }
            else if (selecao == 2)
            { 
                int id = Integer.parseInt( IO.readLine("Digite o id do produto a ser alterado: ") );
                IO.println("O que deseja alterar no produto?");
                IO.println("Digite:\n");
                IO.println("1 para alterar o nome");
                IO.println("2 para alterar a descrição");
                IO.println("3 para alterar o preço\n");
                IO.print("Alterar: ");
            }
            // else if(selecao == 3){ remover(arquivo);  } 
            // else if(selecao == 4){ consultar(arquivo);}

        }//end while

        IO.println("\nAté breve :)");

    }//end main()
   
}//end class Main
