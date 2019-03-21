package user;

import util.*;

import crud.Produto;
import crud.Arquivo;

/**
 * Classe que gerencia a interação com o usuário.
 */

public class Crud<E>{
	private Arquivo<E> arquivo;
	
	/**
	 * Método construtor da interface do CRUD
	 */
	
	public Crud() {
		
	}
	
	public Crud(String crudName, E type) {
		this.arquivo = new Arquivo<E>(crudName);
	}

	/**
	 * Le os campos da entidade e a insere na base de dados.
	 * 
	 * @param arquivo Instância de {@link crud.Arquivo} voltada
	 * para o arquivo {@link user.Main#DATABASE_FILE_NAME}.
	 */
	
	public void inserir()
	{
		if ( arquivo.writeObject( E.readProduct() ) )
		{
			IO.println("\nSeu produto foi cadastrado com sucesso! :D\n");
		}
		
		else
		{
			IO.println("\nFalha no cadastramento do produto.\n");
		}
	}//end inserir()


	/**
	 * Altera um campo específico do produto com id informado.
	 * {@code cod} é responsável por indicar qual dos campos do
	 * produto deseja-se alterar.
	 * 
	 * <p></p>
	 * 
	 * <table style="border: 1px solid black; text-align: center;">
	 * 
	 * 	<tr>
	 * 		<th>{@code cod}</th> <th>campo</th>
	 * 	</tr>
	 * 
	 * 	<tr>
	 * 		<td>1</td> <td>nome</td>
	 * 	</tr>
	 * 
	 * 	<tr>
	 * 		<td>2</td> <td>descrição</td>
	 * 	</tr>
	 * 
	 * 	<tr>
	 * 		<td>3</td> <td>preço</td>
	 * 	</tr>
	 * 
	 * 	<tr>
	 * 		<td>4</td> <td>fornecedor</td>
	 * 	</tr>
	 * 
	 * 	<tr>
	 * 		<td>5</td> <td>quantidade</td>
	 * 	</tr>
	 * 
	 * </table>
	 * 
	 * @param arquivo Instância de {@link crud.Arquivo} voltada
	 * para o arquivo {@link user.Main#DATABASE_FILE_NAME}.
	 * @param id Id da entidade a ser alterada.
	 * @param cod Código do campo a ser alterado.
	 * 
	 * @return {@code false} se alguma coisa falhar na alteração.
	 * Caso contrário, retorna {@code true}.
	 */
	
	public boolean alterar(int id, int cod)
	{
		boolean success = false;

		E item = arquivo.readObject(id);// procurar o produto desejado na base de dados
		
		if (item != null) // checa se o produto foi encontrado
		{
			success = true;
			
			switch (cod)
			{
				case 1:
					E.readName();
					break;

				case 2:
					E.readDescription();
					break;

				case 3:
					E.readPrice();
					break;

				case 4:
					E.readProvider();
					break;

				case 5:
					E.readQuantity();
					break;

				default:
					success = false;
					IO.println("\nOpção inválida !\n");
					break;
			}
			
			if (success)
			{
				success = arquivo.changeObject(id, E);
				
				if (success)
				{
					IO.println("\nSeu produto foi alterado com sucesso! :D\n");
				}
				
				else
				{
					IO.println("\nFalha na alteração do produto.\n");
				}
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
			IO.println("4 para alterar o fornecedor");
			IO.println("5 para alterar a quantidade");
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

	public static <E> void menuExclusao(Arquivo<E> arquivo)
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

	public static <E> void menuConsulta(Arquivo<E> arquivo)
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

	public static <E> void menuListar(Arquivo<E> arquivo)
	{
		arquivo.list().forEach( (E) -> IO.println(E + "\n") );
	}

	/**
	 * Gerencia a interação com o usuário.
	 * @param <E>
	 */
	
	public static <E> void menu(E item)
	{
		Arquivo<E> arquivo = new Arquivo<E>(Main.DATABASE_FILE_NAME);		   
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
