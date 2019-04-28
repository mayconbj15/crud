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

	public void listarCategorias()
	{
		IO.println("Categorias disponíveis:\n");
		listar();
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
			if(quantidadeDeProdutos <= produto.getQuantidade()) {
				itemComprado = new ItemComprado(idCompra, idProduto, quantidadeDeProdutos, produto.getPreco());
			}
			else {
				IO.println("Estoque insuficiente");
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
	 * Altera um campo específico da categoria com id informado.
	 * {@code cod} é responsável por indicar qual dos campos da
	 * categoria deseja-se alterar.
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
	
	public boolean alterar(int id, int cod)
	{
		boolean success = false;

		// procurar o item comprado desejado na base de dados
		ItemComprado itemComprado =  database.readObject(id);
		
		if (itemComprado != null) // checa se a item comprado foi encontrado
		{
			success = true;
			
			switch (cod)
			{
				case 0:
					success = false;
					IO.println("Operação cancelada\n");
					break;

				case 1:
					itemComprado.readQuantity();
					break;
					
				default:
					success = false;
					IO.println("\nOpção inválida !\n");
					break;
			}
			
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
	
	public void menuAlteracao()
	{ 
		int id = IO.readint("Digite o id do item a ser alterado: ");

		//testar antes se o id existe
		if(database.idIsValid(id))
		{
			Crud.noBackMenu
			(
				"Alteração",
				"O que deseja alterar no produto ?",
				new String[] { "quantidade" },
				new Runnable[]
				{
					() -> { alterar(id, 1); }					
				}
			);
		}
		
		else
		{
			IO.println("Id inválido!");
		}
	}

	public void menuConsulta()
	{
		int id = IO.readint("Digite o id da categoria a ser consultada: ");
		
		consultar(id);
	}
	
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
		
	public void menuExclusao()
	{
		listarCategorias();
		int cod = -1; //codigo de selecao
		int id = IO.readint("Digite o id da categoria a ser removida: ");

		//testar antes se o id existe
		if (database.idIsValid(id))
		{
			IO.println("Realmente deseja excluir a categoria ?");
			IO.println("Digite:");
			IO.println("1 Sim");
			IO.println("2 Não");
			IO.println("");
			cod = IO.readint("Opção: ");
			
			if (cod == 1)
			{
				if(Main.databaseProduto.indice.listarDadosComAChave(id).length == 0)
				{
					excluir(id);				
				}
				else
				{
					cod = -1;
					
					IO.println("AVISO: Ainda há produtos nesta categoria.");
					IO.println("Deseja excluí-los também? ");
					IO.println("Digite:");
					IO.println("1 Sim");
					IO.println("2 Não");
					IO.println("");
					cod = IO.readint("Opção: ");
					
					if(cod == 1) 
					{
						//excluí a categoria do databaseCategoria
						excluir(id);
						
						//excluir os produtos que estão na categoria
						int[] listOfInvalids = Main.indiceComposto.listarDadosComAChave(id);
						
						Main.databaseProduto.deleteObjects(listOfInvalids);
					}//end if
					else
					{
						IO.println("Qual das seguintes operações deseja realizar ?");
						IO.println("Digite:");
						IO.println("0 - Sair ");
						IO.println("1 - Mover produtos para uma categoria existente ");
						IO.println("2 - Criar nova categoria ");
						cod = IO.readint("Opção: ");
						
						alterarCategoria(id, cod);
					}
					
				}//end if
				
			}//end if
		}		
		else
		{
			IO.println("Id inválido!");
		}//end if
		
	}//end menuExclusao
}
