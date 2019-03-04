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
      
         IO.print("\nDigite o nome do produto: ");
         nome = IO.readLine();
             
         IO.print("\nDigite a descrição do produto: ");
         descricao = IO.readLine();
             
         IO.print("\nInforme o preço do produto: ");
         preco = Float.parseFloat( IO.readLine() );
             
         Produto produto = new Produto(nome, descricao, preco); 
                
         arquivo.writeObject(produto);
      
         IO.println("\nSeu produto foi cadastrado com sucesso! :D\n");
       
      }
      catch(IOException ioe) {ioe.printStackTrace(); }
    
   }//end inserir()
  
  
   /**
    * M�todo para alterar dados do produto(exceto o id).
    * [EM CONSTRU��O]
    * @param arquivo
    * @param id
    * @param cod
    */
   public static void alterar(Arquivo arquivo, int id, int cod)
   {  
      try 
      {
         //definir dados
         String nome, descricao;
         float preco = 0.0F;
         Produto produto = new Produto();
         
         produto = readObject(id);// procurar o produto desejado na base de dados
         
         //Bloco de if's para alteracao de atributos
         if(cod == 1)
         {
            IO.print("\nDigite o nome do produto: ");
            nome = IO.readLine();
            produto.setNome(nome);
         }
         else if(cod == 2)
         {
            IO.print("\nDigite a descrição do produto: ");
            descricao = IO.readLine();
            produto.setDescricao(descricao);
         }
         else if(cod == 3)
         {
            IO.print("\nInforme o preço do produto: ");
            preco = Float.parseFloat( IO.readLine() );
            produto.setPreco(preco);
         }
         
         arquivo.deleteObject(id);  
         arquivo.writeObject(produto);
      
         IO.println("\nSeu produto foi alterado com sucesso! :D\n");
      
      }
      catch(IOException ioe) {ioe.printStackTrace(); }
   
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
            int id;
            
            IO.println("Digite o id do produto a ser alterado: ");
            id = Integer.parseInt( IO.readLine() );
            
            if(id > 0 && id <= arquivo.getLastID())
            {
               IO.println("O que deseja alterar no produto?\nDigite:\n");
               IO.println("1 para alterar o nome;");
               IO.println("2 para alterar a descrição;");
               IO.println("3 para alterar o preço;"); 
               IO.println("0 para cancelar."); 
               
               cod = Short.parseShort( br.readLine() );
               	
               alterar(arquivo, id, cod);                               
            }
            else{ IO.println("ID inv�lido!"); }
         }
         // else if(selecao == 3){ remover(arquivo);  } 
         // else if(selecao == 4){ consultar(arquivo);}
      
      }//end while
   
      IO.println("\nAté breve :)");
   
   }//end main()
  
}//end class Main
