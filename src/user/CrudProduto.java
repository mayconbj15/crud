package user;

import util.IO;
import util.MyArray;

import java.util.Arrays;
import java.util.ArrayList;

import crud.Arquivo;
import entidades.ItemComprado;
import entidades.Produto;
import entidades.Cliente;

/**
 * Classe que gerencia a interação com o usuário.
 */

public class CrudProduto extends CrudAbstract<Produto>
{
	public CrudProduto(Arquivo<Produto> database)
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
	
	/**
	 * Altera a categoria do produto informado pelo id.
	 * 
	 * @param idProduto Id do produto para alterar a categoria.
	 * @param newIdCategoria Id da nova categoria.
	 */
	
	public void alterarCategoria(int idProduto, int newIdCategoria)
	{
		Produto produto = database.readObject(idProduto);
		
		if( produto != null )
		{
			produto.setIdCategoria(newIdCategoria);
			alterar(idProduto, produto);
		}
	}
	
	public void alterarQuantidade(int idProduto, int quantity) {
		Produto produto = database.readObject(idProduto);
		
		if(produto != null) {
			produto.setQuantidade(produto.getQuantidade() + quantity);
			
			alterar(idProduto, produto);
		}
		
	}

	public void menuAlteracao()
	{ 
		int id = IO.readint("Digite o id do produto a ser alterado: ");

		//testar antes se o id existe
		if(database.idIsValid(id))
		{
			Crud.noBackMenu
			(
				"Alteração",
				"O que deseja alterar no produto ?",
				new String[] { "categoria", "nome", "descrição", "preço", "fornecedor", "quantidade" },
				new Runnable[]
				{
					() -> { alterar(id, 1); },
					() -> { alterar(id, 2); },
					() -> { alterar(id, 3); },
					() -> { alterar(id, 4); },
					() -> { alterar(id, 5); },
					() -> { alterar(id, 6); }
				}
			);
		}
		
		else
		{
			IO.println("Id inválido!");
		}
	}

	public void menuExclusao()
	{ 
		int id = IO.readint("Digite o id do produto a ser excluído: ");
		
		// testar antes se há produtos registradas no arquivo de itens comprados
		if(Main.indiceProdutoItemComprado.listarDadosComAChave(id) == null)
		{
			//testar antes se o id existe
			if (database.idIsValid(id))
			{
				Crud.noBackMenu
				(
					"Confirmação",
					"Realmente deseja excluir o produto ?",
					new String[] { "sim" },
					new Runnable[]
					{
						() ->
						{
							Produto produtoExcluido = excluir(id);
							
							Main.indiceCategoriaProduto.excluir(
								produtoExcluido.getIdCategoria(),
								produtoExcluido.getId()
							);
						}
					}
				);
			}
			
			else
			{
				IO.println("Id inválido!");
			}
		}
		else
		{
			IO.println("ERRO: Há produtos registrados no arquivo de itens comprados.");
		}
	}

	public void consultarComprasDeUmProduto()
	{
		int id = IO.readint("Digite o id do produto cujas compras deseja-se consultar: ");
		
		int[] idsItensComprados = Main.indiceProdutoItemComprado.listarDadosComAChave(id);
		
		if (idsItensComprados != null)
		{
			int numeroDeComprasJaConsultadas = 0;
			int[] idsDasComprasJaConsultadas = new int[idsItensComprados.length];
			Arrays.fill(idsDasComprasJaConsultadas, -1);
			
			for (int idItemComprado : idsItensComprados)
			{
				ItemComprado itemComprado = Main.databaseItemComprado.readObject(idItemComprado);
				
				if (!MyArray.contains(itemComprado.getIdCompra(), idsDasComprasJaConsultadas))
				{
					idsDasComprasJaConsultadas[ numeroDeComprasJaConsultadas++ ] = itemComprado.getIdCompra();
					
					Main.crudCompra.consultar(itemComprado.getIdCompra());
				}
			}
		}
		
		else
		{
			IO.println("Produto não encontrado.");
		}
	}
	
	public void consultarProduto()
	{
		int id = IO.readint("Digite o id do produto a ser consultado: ");
		
		consultar(id);
	}
	
	public void menuConsulta()
	{
		Crud.menu
		(
			"Consulta",
			"Qual das seguintes operações o senhor deseja realizar ?",
			new String[] { "consultar produto", "consultar compras de um produto" },
			new Runnable[]
			{
				() -> { consultarProduto(); IO.pause(); },
				() -> { consultarComprasDeUmProduto(); IO.pause(); }
			}
		);
	}
	
	public void menuListarPre() {
		IO.println("Qual tipo de listagem deseja ?");
		IO.println("1 - Listagem de todos os produtos\n2 - Listagem dos clientes que compraram determinado produto\n");
		
		int cho = IO.readint();
		
		switch(cho) {
			case 1: menuListar(); break;
			case 2: relatarProdutosClientes(); break;
			default: IO.println("Opção inválida\n"); break;
		}
		
	}
	public void menuListar()
	{
		listar();
	}

	
	/**
	 * Método que lista os clientes que compraram um certo produto
	 */
	public void relatarProdutosClientes() {
		IO.println("Relatório de quantos clientes compraram certo produto\n");
		IO.println("PRODUTOS\n");
		
		ArrayList<Produto> produtos = Main.databaseProduto.list();
		ArrayList<Integer> clientes = new ArrayList<Integer>();
		
		for(Produto produto : produtos) {
			IO.println(produto);
		}
		
		int produto;
		do {
			produto = IO.readLineUntilPositiveInt("Qual produto deseja ver a relação dos clientes (digite o id)\n");
		}while(!Main.databaseProduto.idIsValid(produto));
		
		
		if(Main.databaseItemComprado.list().size() > 0) {
			int[] idsItensComprados = Main.indiceProdutoItemComprado.listarDadosComAChave(produto); //pega os itensComprados que tem esse produto
			
			if(idsItensComprados.length > 0) {
				for(int idItemComprado: idsItensComprados) {
					int idCompra = Main.databaseItemComprado.readObject(idItemComprado).getIdCompra();
					int idCliente = Main.databaseCompra.readObject(idCompra).getIdCliente();
					
					if(idCliente != -1) {
						if(!clientes.contains(idCliente))
							clientes.add(idCliente);
						Main.crudCliente.consultarParaOUsuario(idCliente);
					}
					
					
				}
			}
			else {
				IO.println("Esse produto não foi comprado por ninguém");
			}
		}
		else {
			IO.println("Nenhum produto comprado na loja");
		}
		
		
		
	}
	
	
	/**
	 * Gerencia a interação com o usuário.
	 */
	
	public void menu()
	{
		Crud.menu
		(
			"Produto",
			"Qual das seguintes operações o senhor deseja realizar ?",
			new String[] { "inclusão", "alteração", "exclusão", "consulta", "listagem" },
			new Runnable[]
			{
				() -> { menuInclusao(); IO.pause(); },
				() -> { menuAlteracao(); IO.pause(); },
				() -> { menuExclusao(); IO.pause(); },
				() -> { menuConsulta(); IO.pause(); },
				() -> { menuListarPre(); IO.pause(); }
			}
		);
		
	}//end menu()
	
}//end class CrudProduto