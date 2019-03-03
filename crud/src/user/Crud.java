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
    
    /**
    * Metodo para criar um novo registro de produto
    *
    * @param - arquivo destino
    */
    public static void inserir(Arquivo arquivo)
    {  
        try 
        {
            String nome, descricao;
            float preco = 0.0F;

            IO.println("\nDigite o nome do produto: ");
            nome = IO.readLine();
                
            IO.println("\nDigite a descrição do produto: ");
            descricao = IO.readLine();
                
            IO.println("\nInforme o preço do produto: ");
            preco = Float.parseFloat( IO.readLine() );
                
            Produto produto = new Produto(nome, descricao, preco); 
                   
            arquivo.writeObject(produto);

            IO.println("Seu produto foi cadastrado com sucasso! :D\n");
          
        }catch(IOException ioe) {ioe.printStackTrace(); }
      
    }//end inserir()
   
   
    public static void alterar(Arquivo arquivo, int id)
    {  
        try 
        {
            String nome, descricao;
            float preco = 0.0F;

            IO.println("\nDigite o nome do produto: ");
            nome = IO.readLine();

            IO.println("\nDigite a descrição do produto: ");
            descricao = IO.readLine();

            IO.println("\nInforme o preço do produto: ");
            preco = Float.parseFloat( IO.readLine() );

            Produto produto = new Produto(nome, descricao, preco); 
               
            arquivo.writeObject(produto);

            IO.println("Seu produto foi cadastrado com sucasso! :D\n");

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
            IO.println("Qual das seguintes operações o senhor deseja realizar?" + 
                        "\nDigite: " + 
                        "\n1 para inclusão;" +
                        "\n2 para alteração;" +
                        "\n3 para exclusão;" +
                        "\n4 para consulta de produtos;" +
                        "\n0 para sair.");
                             
            selecao = Integer.parseInt( IO.readLine() ); //entrada do codigo de selecao de acao         
             
            //INCLUSAO
            if      (selecao == 1) { inserir(arquivo); }
            else if (selecao == 2)
            { 
                int id = 0;
                IO.println("Digite o id do produto a ser alterado: ");
                id = Integer.parseInt( IO.readLine() );
                IO.println("O que deseja alterar no produto?\nDigite:\n");
                IO.println("1 para alterar o nome;");
                IO.println("1 para alterar a descrição;");
                IO.println("1 para alterar o preço;");            
            }
            // else if(selecao == 3){ remover(arquivo);  } 
            // else if(selecao == 4){ consultar(arquivo);}

        }//end while

        IO.println("Até breve :)");

    }//end main()
   
}//end class Main
