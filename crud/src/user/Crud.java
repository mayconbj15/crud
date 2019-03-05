package user;

import java.io.*;
import util.*;
import crud.*;
import java.util.*;

import crud.Produto;
import crud.Arquivo;

public class Crud 
{
   /**
   * Método para criar um novo registro de produto
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
    * Método para alterar dados do produto(exceto o id).
    * [EM CONSTRUÇÃO]
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
         
         produto = arquivo.readObject(id);// procurar o produto desejado na base de dados
         
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

   public static void menu()
   {

	   try
	   {
		   //definir dados    	 
	       BufferedReader br = new BufferedReader( new InputStreamReader(System.in) );
	       //File file = new File("produto.db");
	       Arquivo arquivo = new Arquivo("produtos.db");        
	       int selecao = 12;
	       System.out.println("Olá, meu nobre!\n");
	         
	       //TESTAR SE E' PARA SAIR
	       
	       do {
	    	   //Interface de entrada
		       System.out.println("Qual das seguintes operações o senhor deseja realizar?" + 
		                         "\nDigite: " + 
		                         "\n1 para inclusão;" +
		                         "\n2 para alteração;" +
		                         "\n3 para exclusão;" +
		                         "\n4 para consulta de produtos;" +
		                         "\n5 para listar todos os produtos" +
		                         "\n0 para sair.");
		       selecao = Integer.parseInt( br.readLine() ); //entrada do codigo de selecao de acao         
			        
		       if(selecao == 1)
		    	   inserir(arquivo);// INCLUSAO
		           else if(selecao == 2)// ALTERACAO
		           { 
		        	   int id = 0;
		               short cod = -1; //codigo de selecao
		            	
		               System.out.println("Digite o id do produto a ser alterado: ");
		               id = Integer.parseInt( br.readLine() );
		            	
		               //testar antes se o id existe
		               if(id > 0 && id <= arquivo.getLastID())
		               {
		            	   System.out.println("O que deseja alterar no produto?\nDigite:");
		                   System.out.println("1 para alterar o nome;");
		                   System.out.println("2 para alterar a descrição;");
		                   System.out.println("3 para alterar o preço;");
		                   System.out.println("0 para cancelar;");
		               	
		                   cod = Short.parseShort( br.readLine() );
		                   
		                   alterar(arquivo, id, cod);
		               	
		               }
		               else { System.out.println("Id inválido!"); }
		               
		            }
		            else if(selecao == 3){
		            	int id = 0;
		            	short cod = -1; // codigo de selecao
		            	IO.println("Digite o id do produto a ser excluído: ");
		            	id = Integer.parseInt( br.readLine() );
		            	// testar se o id existe
			            if(id > 0 && id <= arquivo.getLastID())
			            {
			            	Produto produto = new Produto();
			            	IO.println("Deseja realmente excluir o produto?\nDigite:");
			            	IO.println("1 Sim;");
			            	IO.println("2 Não;");
			            	
			            	cod = Short.parseShort( br.readLine() );
			            	
			            	if(cod == 1) { 
			            		arquivo.deleteObject(id);
			            		IO.println("\nSeu produto foi excluído com sucesso! :D\n");
			            	}
			            	else { IO.println("Operação cancelada!"); }
			            }
			            else { System.out.println("Id inválido!"); }
			        }
		            //else if(selecao == 4){ consultar(arquivo);}
		            else if(selecao == 5) 
		            {
		            	ArrayList<Produto> list = new ArrayList<Produto>();
		                list = arquivo.list();
		            
		                for(int i=0; i<list.size(); i++) {
		                	System.out.println(list.get(i));
		                }//end for
		            
		            }//end if
		            else if(selecao == 0)
		            	System.out.println("Até breve :)");
	         }while(selecao != 0);
	   }
	   catch(IOException ioe){
		   ioe.printStackTrace();
	   }
   }//end menu()
}//end class Main
