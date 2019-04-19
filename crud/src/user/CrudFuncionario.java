package user;

import util.IO;

import crud.Arquivo;
import entidades.Cliente;
import entidades.Funcionario;
import entidades.Produto;

/**
 * Classe que gerencia a interação com o usuário.
 */

public class CrudFuncionario extends CrudAbstract<Funcionario>
{
	public CrudFuncionario(Arquivo<Funcionario> database)
	{
		super(database);
	}
	
	public void menuInclusao()
	{
		Produto produto = new Produto();
		
		Main.crudCategoria.listarCategorias();
		
		produto.readCategory();
		
		// checa se a categoria informada existe
		if( Main.crudCategoria.consultar(produto.getIdCategoria()) ){
			
			produto.readName();
			produto.readDescription();
			produto.readPrice();
			produto.readProvider();
			produto.readQuantity();
			
			inserir(produto);
			
			Main.indiceCategoriaProduto.inserir(
				produto.getIdCategoria(),
				produto.getId()
			);
		}
	}

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
	 * 		<td>1</td> <td>categoria</td>
	 * 	</tr>
	 * 
	 * 	<tr>
	 * 		<td>2</td> <td>nome</td>
	 * 	</tr>
	 * 
	 * 	<tr>
	 * 		<td>3</td> <td>descrição</td>
	 * 	</tr>
	 * 
	 * 	<tr>
	 * 		<td>4</td> <td>preço</td>
	 * 	</tr>
	 * 
	 * 	<tr>
	 * 		<td>5</td> <td>fornecedor</td>
	 * 	</tr>
	 * 
	 * 	<tr>
	 * 		<td>6</td> <td>quantidade</td>
	 * 	</tr>
	 * 
	 * </table>
	 * 
	 * @param id Id da entidade a ser alterada.
	 * @param cod Código do campo a ser alterado.
	 * 
	 * @return {@code false} se alguma coisa falhar na alteração.
	 * Caso contrário, retorna {@code true}.
	 */
	
	public boolean alterar(int id, int cod)
	{
		boolean success = false;

		// procurar o produto desejado na base de dados
		Produto produto = database.readObject(id);
		
		if (produto != null) // checa se o produto foi encontrado
		{
			success = true;
			
			switch (cod)
			{
				case 1:
					Main.indiceCategoriaProduto.excluir(
						produto.getIdCategoria(),
						produto.getId()
					);
					
					while ( !Main.crudCategoria.consultar(produto.readCategory()) );
					
					Main.indiceCategoriaProduto.inserir(
						produto.getIdCategoria(),
						produto.getId()
					);
					
					break;

				case 2:
					produto.readName();
					break;

				case 3:
					produto.readDescription();
					break;

				case 4:
					produto.readPrice();
					break;

				case 5:
					produto.readProvider();
					break;

				case 6:
					produto.readQuantity();
					break;

				default:
					success = false;
					IO.println("\nOpção inválida !\n");
					break;
			}
			
			if (success)
			{
				success = alterar(id, produto) != null;
			}
		}
		
		else
		{
			IO.println("\nProduto não encontrado.\n");
		}

		return success;
	}//end alterar()

	public void menuAlteracao()
	{ 
		int cod = -1; //codigo de selecao
		int id = IO.readint("Digite o id do produto a ser alterado: ");

		//testar antes se o id existe
		if(database.idIsValid(id))
		{
			IO.println("O que deseja alterar no produto?");
			IO.println("Digite:");
			IO.println("1 para alterar a categoria");
			IO.println("2 para alterar o nome");
			IO.println("3 para alterar a descrição");
			IO.println("4 para alterar o preço");
			IO.println("5 para alterar o fornecedor");
			IO.println("6 para alterar a quantidade");
			IO.println("0 para cancelar");
			IO.println("");
			cod = IO.readint("Opção: ");
			
			alterar(id, cod);
		}
		
		else
		{
			IO.println("Id inválido!");
		}
	}

	public void menuExclusao()
	{ 
		int cod = -1; //codigo de selecao
		int id = IO.readint("Digite o id do produto a ser excluído: ");

		//testar antes se o id existe
		if (database.idIsValid(id))
		{
			IO.println("Realmente deseja excluir o produto ?");
			IO.println("Digite:");
			IO.println("1 Sim");
			IO.println("2 Não");
			IO.println("");
			cod = IO.readint("Opção: ");
			
			if (cod == 1)
			{
				Produto produtoExcluido = excluir(id);
				
				Main.indiceCategoriaProduto.excluir(
					produtoExcluido.getIdCategoria(),
					produtoExcluido.getId()
				);
			}
		}
		
		else
		{
			IO.println("Id inválido!");
		}
	}

	public void menuListar()
	{
		listar();
	}
	
}//end class CrudProduto