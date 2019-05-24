package user;

import java.util.ArrayList;

import crud.Arquivo;

import entidades.Compra;
import util.IO;

public class CrudCompra extends CrudAbstract<Compra>
{
	public CrudCompra(Arquivo<Compra> database)
	{
		super(database);
	}
	
	public int menuInclusao(Compra compra)
	{	
		int success = -1;
		
		if(compra != null) 
		{
			success = inserir(compra);
		}

		return success;
	}

	/**
	 * Altera o valor total de uma compra caso o valor unitário de uma compra seja alterado.
	 * {@code cod} é responsável por indicar qual dos campos da
	 * compra deseja-se alterar.
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
		// procurar a compra desejada na base de dados
		Compra compra =  database.readObject(id);		
		int idItensComprados = -1;
		boolean success = false;
		
		if (compra != null) // checa se a compra foi encontrada 
		{ 
			switch (cod)
			{
				case 0:
					compra = null;
					IO.println("Operação cancelada\n");
					break;

				case 1:
					IO.println("Qual dos seguintes produtos deseja alterar? ");					
					CrudItemComprado.listarItensComprados(id);		
					idItensComprados = IO.readint("Digite o id do item comprado: ");					
					success = Main.crudItemComprado.alterar(idItensComprados);					
					break;					
					
				default:
					compra = null;
					success = false;
					IO.println("\nOpção inválida !\n");
					break;
					
			}//end switch-case					
			
			if (compra != null)
			{
				compra = alterar(id, compra);
			}
		}
		
		else
		{
			IO.println("\nCompra não encontrada.\n");
		}

		return success;
		
	}//end alterar()
	
	public void menuAlteracao()
	{ 
		int id = IO.readint("Digite o id da COMPRA a ser alterada: ");

		//testar antes se o id existe
		if(database.entityExists(id))
		{
			Crud.noBackMenu
			(
				"Alteração",
				"O que deseja alterar na compra ?",
				new String[] { "Itens comprados" },
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
		int id = IO.readint("Digite o id da compra ser consultada: ");
		
		consultar(id);
	}
			
	public void menuListar()
	{
		Crud.noBackMenu
		(
			"Listagem",
			"O que deseja listar ?",
			new String[] { "todas as compras", "itens comprados de uma compra" },
			new Runnable[]
			{
				() -> { listarCompras(); },
				() -> { CrudItemComprado.listarItensComprados( IO.readint("Entre com o id da compra desejada: ") ); }
			}
		);
		
	}//end menuListar()
	
	public void listarCompras() 
	{
		IO.println("Compras documentadas: ");
		listar();
	}

	public void menuExclusao()
	{
		int cod = -1; //codigo de selecao
		int id = IO.readint("Digite o id da compra a ser removida: ");

		// testar antes se há compras registradas no arquivo de itens comprados
		if (Main.indiceCompraItemComprado.listarDadosComAChave(id) == null)
		{
			//testar antes se o id existe
			if (database.entityExists(id))
			{
				IO.println("Realmente deseja excluir a compra " + id + " ?");
				IO.println("Digite:");
				IO.println("1 Sim");
				IO.println("2 Não");
				IO.println("");
				
				cod = IO.readint("Opção: ");
				
				if (cod == 1)
				{
					excluir(id);
				}
				else if(cod == 2) 
				{
					IO.println("Operação cancelada");
				}
			}
			else
			{
				IO.println("Id inválido!");
			}
		}
		else
		{
			IO.println("ERRO: Há compras registrados no arquivo de itens comprados.");
		}
		
	}//end menuExclusao
	
	public void menuRelatarProdutos() 
	{
		IO.println("Produtos comprados:");
		
		// Obtém os ids das compras do cliente logado
		int[] idsCompras = Main.indiceClienteCompra.listarDadosComAChave(
			Crud.gerenciadorCliente.usuarioLogado.getId()
		);
		
		if (idsCompras.length > 0)
		{
			int[] idsItensComprados;
			ArrayList<Integer> idsProdutosComprados = new ArrayList<Integer>();
			int idProduto;

			for (int idCompra : idsCompras)
			{
				// Obtém os ids dos itens comprados das compras do cliente logado
				idsItensComprados = Main.indiceCompraItemComprado.listarDadosComAChave(idCompra);

				for (int idItemComprado : idsItensComprados)
				{
					idProduto = Main.databaseItemComprado.readObject(idItemComprado).getIdProduto();
					
					if (!idsProdutosComprados.contains(idProduto))
					{
						idsProdutosComprados.add(idProduto);
						
						Main.crudProduto.consultarParaOUsuario(idProduto);
					}
				}
			}
		}
		
		else
		{
			IO.println("Você não comprou nenhum produto.");
		}
	}
		
}//end class CrudCompra
