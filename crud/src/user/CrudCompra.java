package user;

import crud.Arquivo;

import entidades.Compra;
import entidades.ItemComprado;

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
	 * Altera o valor total de uma categoria caso o valor unitário de uma compra seja alterado.
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
	
	public Compra alterar(int id, int cod)
	{
		// procurar a compra desejada na base de dados
		Compra compra =  database.readObject(id);		
		
		if (compra != null) // checa se a compra foi encontrada 
		{ 
			// [EM DESENVOLVIMENTO]
			switch (cod)
			{
				case 0:
					compra = null;
					IO.println("Operação cancelada\n");
					break;

				case 1:
					IO.println("Qual dos seguintes produtos deseja alterar? ");					
					listarItensComprados(id);
					
					break;					
					
				default:
					compra = null;
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
			IO.println("\nCategoria não encontrada.\n");
		}

		return compra;
		
	}//end alterar()
	
	public void menuAlteracao()
	{ 
		int id = IO.readint("Digite o id da COMPRA a ser alterada: ");

		//testar antes se o id existe
		if(database.idIsValid(id))
		{
			Crud.menu
			(
				"Alteração",
				"O que deseja alterar na Compra ?",
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
				() -> { listarItensComprados( IO.readint("Entre com o id da compra desejada: ") ); }
			}
		);
		
	}//end menuListar()
	
	public void listarCompras() 
	{
		IO.println("Compras documentadas: ");
		listar();
	}
	
	/**
	 * Listar todos os produtos de uma compra dado o seu id.
	 * 
	 * @param id
	 */
	public void listarItensComprados(int id) 
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
		int cod = -1; //codigo de selecao
		int id = IO.readint("Digite o id da compra a ser removida: ");

		//testar antes se o id existe
		if (database.idIsValid(id))
		{
			IO.println("Realmente deseja excluir a compra " + database.readObject(id).getId() + " ?");
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
		
	}//end menuExclusao
}
