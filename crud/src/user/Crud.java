package user;

import util.*;

import crud.Produto;
import crud.Arquivo;
import crud.Entidade;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Classe que gerencia a interação com o usuário.
 */

public class Crud<T extends Entidade>{
	private Arquivo<T> arquivo;
	private Constructor<? extends Entidade> constructor;
	private String crudName;
	/**
	 * Método construtor da interface do CRUD
	 */
	
	public Crud() {
		
	}
	
	public Crud(String crudName, Constructor<? extends Entidade> constructor2) {
		//this.arquivo = new Arquivo<T>(type.class.getConstructor(), crudName);
		this.crudName = crudName;
		this.constructor = constructor2;
	}

	/**
	 * Le os campos da entidade e a insere na base de dados.
	 * 
	 * @param arquivo Instância de {@link crud.Arquivo} voltada
	 * para o arquivo {@link user.Main#DATABASE_FILE_NAME}.
	 */
	
	public void inserir(Arquivo<T> arquivo){
		//T item;
		try {
			T item = (T) constructor.newInstance();
			if ( arquivo.writeObject( (T)item.readProduct() ) )
			{
				IO.println("\nSeu produto foi cadastrado com sucesso! :D\n");
			}
			
			else
			{
				IO.println("\nFalha no cadastramento do produto.\n");
			}
		}catch(IllegalAccessException iae) {
			iae.printStackTrace();
		}catch(InstantiationException ie) {
			ie.printStackTrace();
		}catch(InvocationTargetException ite) {
			ite.printStackTrace();
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
	
	public boolean alterar(Arquivo<T> arquivo, int id, int cod)
	{
		boolean success = false;

		T item = arquivo.readObject(id);// procurar o produto desejado na base de dados
		
		if (item != null) // checa se o produto foi encontrado
		{
			success = true;
			
			switch (cod)
			{
				case 1:
					item.readName();
					break;

				case 2:
					item.readDescription();
					break;

				case 3:
					item.readPrice();
					break;

				case 4:
					item.readProvider();
					break;

				case 5:
					item.readQuantity();
					break;

				default:
					success = false;
					IO.println("\nOpção inválida !\n");
					break;
			}
			
			if (success)
			{
				success = arquivo.changeObject(id, item);
				
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

	public void menuInclusao(Arquivo<T> arquivo)
	{
		inserir(arquivo);
	}

	public void menuAlteracao(Arquivo<T> arquivo)
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

	public void menuExclusao(Arquivo<T> arquivo)
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

	public void menuConsulta(Arquivo<T> arquivo)
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

	public void menuListar(Arquivo<T> arquivo)
	{
		arquivo.list().forEach( (T) -> IO.println(T + "\n") );
	}

	/**
	 * Gerencia a interação com o usuário.
	 * @param <E>
	 */
	
	public void menu(T item)
	{
		try {
			Arquivo<T> arquivo = new Arquivo<T>(item.getClass().getConstructor(), Main.DATABASE_FILE_NAME);
			
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
		}catch(NoSuchMethodException nsme) {
			nsme.printStackTrace();
		}
				   
	
		
	}//end menu()
	
}//end class Main
