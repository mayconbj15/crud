package user;

import java.util.ArrayList;

import crud.Arquivo;

import entidades.ItemComprado;
import entidades.Produto;
import util.IO;

public class CrudItemComprado extends CrudAbstract<ItemComprado>
{
	public CrudItemComprado(Arquivo<ItemComprado> database)
	{
		super(database);
	}
	
	/**
	 * Lê o produto a ser comprado e a quantidade. Depois, gera o
	 * ItemComprado correspondente.
	 * 
	 * @param idCompra Id da compra em que o ItemComprado está.
	 * 
	 * @return ItemComprado correspondente à compra.
	 */
	
	public ItemComprado novoItemComprado(int idCompra) 
	{
		int quantidadeDeProdutos = 0;
		int idProduto = IO.readLineUntilPositiveInt("\nQual produto deseja comprar (Digite o id) ? ");
		Produto produto = Main.databaseProduto.readObject(idProduto);
		
		ItemComprado itemComprado = null;
		
		if(produto != null) {
			quantidadeDeProdutos = IO.readLineUntilPositiveInt("\nDigite a quantidade do produto: ");
			
			if (quantidadeDeProdutos > 0){
				if(quantidadeDeProdutos <= produto.getQuantidade()) {
					itemComprado = new ItemComprado(idCompra, idProduto, quantidadeDeProdutos, produto.getPreco());
				}
				
				else {
					IO.println("Estoque insuficiente");
				}
			}
			
		}
		else {
			IO.println("ID inválido");
		}
		
		return itemComprado;
	}
	
	public void inserirItensComprados(ArrayList<ItemComprado> itensComprados) 
	{
		if(itensComprados != null) {
			int size = itensComprados.size();
			int success = 0;
			
			for(int i=0; i < size && success != -1; i++) {
				success = inserirItemComprado(itensComprados.get(i));
			}
		}
	}
	
	private int inserirItemComprado(ItemComprado itemComprado) 
	{
		int success = -1;
		success = Main.crudItemComprado.inserir(itemComprado);
		
		if(success != -1) 
		{
			// criando o relacionamento da compra com o item comprado e
			// do produto com o item comprado
			Main.indiceCompraItemComprado.inserir(itemComprado.getIdCompra(), itemComprado.getId());
			Main.indiceProdutoItemComprado.inserir(itemComprado.getIdProduto(), itemComprado.getId());
		}
		
		return success;
	}
	

	/**
	 * Altera um campo específico de um item comprado com id informado.
	 * {@code cod} é responsável por indicar qual dos campos do
	 * item comprado deseja-se alterar.
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
	 * </table>
	 * 
	 * @param id Id da entidade a ser alterada.
	 * @param cod Código do campo a ser alterado.
	 * 
	 * @return {@code false} se alguma coisa falhar na alteração.
	 * Caso contrário, retorna {@code true}.
	 */
	
	public boolean alterar(int id)
	{
		boolean success = false;

		// procurar o item comprado desejado na base de dados
		ItemComprado itemComprado =  database.readObject(id);
		
		if (itemComprado != null) // checa se a item comprado foi encontrado
		{
			success = true;
			
			itemComprado.readQuantity();
									
			if (itemComprado != null)
			{
				itemComprado = alterar(id, itemComprado);
			}
		}
		
		else
		{
			IO.println("\nCategoria não encontrada.\n");
		}

		return success;
		
	}//end alterar()
	
	/**
	 * Listar todos os produtos de uma compra dado o seu id.
	 * 
	 * @param id
	 */
	public static void listarItensComprados(int id) 
	{
		int [] lista = Main.indiceCompraItemComprado.listarDadosComAChave(id);
		int tamanho = lista.length;							
		
		for(int y = 0; y < tamanho; y++)
		{
			Main.crudItemComprado.consultar(lista[y]);
		}//end for
	}
}
