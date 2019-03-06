package user;

import util.*;

import crud.Produto;
import crud.Arquivo;

public class Crud 
{
	/**
	* Método construtor da interface do CRUD
	*/
	public Crud() {
		
	}
	
	/**
	* Le os campos de um produto da entrada padrão e
	* cria o objeto equivalente.
	* 
	* Obs.: caso o usuário informe valores não numéricos
	* para campos numéricos, estes são definidos como -1.
	* 
	* @return produto com os campos lidos.
	*/
	
	public static Produto readProduct()
	{
		String nome, descricao;
		float preco;

		nome = IO.readLine("\nDigite o nome do produto: ");

		descricao = IO.readLine("\nDigite a descrição do produto: ");

		preco = IO.readfloat("\nInforme o preço do produto: ");
		
		return new Produto(nome, descricao, preco); 
	}
	
	/**
	* Metodo para criar um novo registro de produto
	*
	* @param - arquivo destino
	*/
	public static void inserir(Arquivo arquivo)
	{
		if ( arquivo.writeObject( readProduct() ) )
		{
			IO.println("\nSeu produto foi cadastrado com sucesso! :D\n");
		}
		
		else
		{
			IO.println("\nFalha no cadastramento do produto.\n");
		}
	}//end inserir()


	/**
	* Método para alterar dados do produto(exceto o id).
	* [EM CONSTRUÇÃO]
	* @param arquivo
	* @param id
	* @param cod
	*/
	public static boolean alterar(Arquivo arquivo, int id, int cod)
	{
		boolean success = false;
		//definir dados
		String nome, descricao;
		float preco;
		Produto produto;
		
		produto = arquivo.readObject(id);// procurar o produto desejado na base de dados
		
		if (produto != null) // checa se o produto foi encontrado
		{
			success = true;
			
			//Bloco de if's para alteracao de atributos
			if(cod == 1)
			{
				nome = IO.readLine("\nDigite o nome do produto: ");
				produto.setNome(nome);
			}
			
			else if(cod == 2)
			{
				descricao = IO.readLine("\nDigite a descrição do produto: ");
				produto.setDescricao(descricao);
			}
			
			else if(cod == 3)
			{
				preco = IO.readfloat("\nInforme o preço do produto: ");
				produto.setPreco(preco);
			}
			
			else
			{
				success = false;
				IO.println("\nOpção inválida !\n");
			}
			
			if (success)
			{
				success = arquivo.deleteObject(id) && arquivo.writeObject(produto);
				
				if (success)
				{
					IO.println("\nSeu produto foi alterado com sucesso! :D\n");
				}
			}

			if (!success)
			{
				IO.println("\nFalha na alteração do produto.\n");
			}
		}
		
		else
		{
			IO.println("\nProduto não encontrado.\n");
		}

		return success;
	}//end alterar()

	public static void menuInclusao(Arquivo arquivo)
	{
		inserir(arquivo);
	}

	public static void menuAlteracao(Arquivo arquivo)
	{ 
		int cod = -1; //codigo de selecao
		short id = IO.readshort("Digite o id do produto a ser alterado: ");

		//testar antes se o id existe
		if(arquivo.idIsValid(id))
		{
			IO.println("O que deseja alterar no produto?");
			IO.println("Digite:");
			IO.println("1 para alterar o nome");
			IO.println("2 para alterar a descrição");
			IO.println("3 para alterar o preço");
			IO.println("0 para cancelar");
			IO.println("");
			cod = IO.readint("Opção: ");
			
			alterar(arquivo, id, cod);
		}
		
		else
		{
			IO.println("Id inválido!");
		}
	}

	public static void menuExclusao(Arquivo arquivo)
	{ 
		int cod = -1; //codigo de selecao
		short id = IO.readshort("Digite o id do produto a ser excluído: ");

		//testar antes se o id existe
		if (arquivo.idIsValid(id))
		{
			IO.println("Realmente deseja excluir o produto ?");
			IO.println("Digite:");
			IO.println("1 Sim");
			IO.println("2 Não");
			IO.println("");
			cod = IO.readint("Opção: ");
			
			if (cod == 1)
			{
				if (arquivo.deleteObject(id))
				{
					IO.println("Produto excluído com sucesso.");
				}
				
				else
				{
					IO.println("Falha na exclusão do produto.");
				}
			}
		}
		
		else
		{
			IO.println("Id inválido!");
		}
	}

	public static void menuConsulta(Arquivo arquivo)
	{
		short id = IO.readshort("Digite o id do produto a ser procurado: ");
		
		if (arquivo.idIsValid(id))
		{
			IO.println( "\n" + arquivo.readObject(id) );
		}
		
		else
		{
			IO.println("Id inválido!");
		}
	}

	public static void menuListar(Arquivo arquivo)
	{
		arquivo.list().forEach( (produto) -> IO.println(produto) );
	}

	public static void menu()
	{
		Arquivo arquivo = new Arquivo(Main.DATABASE_FILE_NAME);		   
		int selecao;
		
		IO.println("Olá, meu nobre!\n");
		
		do {
			//Interface de entrada
			IO.println("Qual das seguintes operações o senhor deseja realizar ?");
			IO.println("Digite:");
			IO.println("1 para inclusão");
			IO.println("2 para alteração");
			IO.println("3 para exclusão");
			IO.println("4 para consulta de produtos");
			IO.println("5 para listar todos os produtos");
			IO.println("0 para sair");
			IO.println("");
			selecao = IO.readint("Operação: ");
			
			IO.println("\nOperação iniciada...\n");
			
			switch (selecao)
			{
				case 0:
					IO.println("Até breve :)");
					break;
					
				case 1:
					menuInclusao(arquivo);
					IO.pause();
					break;
					
				case 2:
					menuAlteracao(arquivo);
					IO.pause();
					break;
					
				case 3:
					menuExclusao(arquivo);
					IO.pause();
					break;
					
				case 4:
					menuConsulta(arquivo);
					IO.pause();
					break;
					
				case 5:
					menuListar(arquivo);
					IO.pause();
					break;
					
				default:
					IO.println("Operação inválida\n");
					break;
			}

			
			IO.println("\nOperação finalizada.\n");
			IO.println("--------------------------------------------\n");
			
		} while (selecao != 0);
	}//end menu()
}//end class Main
