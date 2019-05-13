package user;

import crud.Arquivo;
import entidades.Categoria;
import util.IO;

public class CrudCategoria extends CrudAbstract<Categoria>
{
	public CrudCategoria(Arquivo<Categoria> database)
	{
		super(database);
	}

	/**
	 * Tenta obter o nome da categoria com o id informado.
	 * 
	 * @param idCategoria Id da categoria a ser procurada.
	 * 
	 * @return {@code null} se a categoria não existir;
	 * o nome da categoria caso contrário.
	 */
	
	public String getCategoryName(int idCategoria)
	{
		Categoria category = database.readObject(idCategoria);
		String name = null;
		
		if (category != null)
		{
			name = category.getNome();
		}
		
		return name;
	}
	
	public void listarCategorias()
	{
		IO.println("Categorias disponíveis:\n");
		listar();
	}
	
	public int menuInclusao()
	{
		Categoria categoria = new Categoria();
		
		categoria.setNome(categoria.readName());
		
		return inserir(categoria);
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

		// procurar a categoria desejada na base de dados
		Categoria categoria =  database.readObject(id);
		
		if (categoria != null) // checa se a categoria foi encontrada
		{
			success = true;
			
			switch (cod)
			{
				case 0:
					success = false;
					IO.println("Operação cancelada\n");
					break;

				case 1:
					categoria.readName();
					break;
					
				default:
					success = false;
					IO.println("\nOpção inválida !\n");
					break;
			}
			
			if (success)
			{
				success = alterar(id, categoria) != null;
			}
		}
		
		else
		{
			IO.println("\nCategoria não encontrada.\n");
		}

		return success;
		
	}//end alterar()
	
	/**
	 * Altera a categoria do produto informado pelo id.
	 * {@code cod} é responsável por indicar qual das operações
	 * deseja-se realizar.
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
	 * 		<td>0</td> <td>sair do método</td>
	 * 	</tr>
	 * 
	 * 	<tr>
	 * 		<td>1</td> <td>mover produtos para uma categoria já existente</td>
	 * 	</tr>
	 * 
	 * 	<tr>
	 * 		<td>2</td> <td>criar nova categoria antes de mover produtos para esta categoria</td>
	 * 	</tr>
	 * 
	 * </table>
	 * 
	 * @param id Id da categoria a ser alterada.
	 * @param cod Operação a ser realizada
	 */
	
	public void alterarCategoria(int id, int cod)
	{
		if(Main.crudProduto != null)
		{
			int [] lista = Main.indiceCategoriaProduto.listarDadosComAChave(id);
			int tamanho = lista.length;
			int newIdCategoria;
			
			switch (cod)
			{
				case 0:
					IO.println("Até breve :)");
					break;
					
				case 1: // mover produtos para uma categoria diferente
					newIdCategoria =
					IO.readLineUntilPositiveInt("\nInforme a nova categoria dos produtos: ");
					
					IO.println("\nMovendo todos os produtos...");
					
					for (int idProduto : lista)
					{
						Main.crudProduto.alterarCategoria(idProduto, newIdCategoria);
					}
					
					excluir(id);
					break;
					
				case 2: // criar nova categoria e inserir elementos nela
					newIdCategoria = menuInclusao();
					
					IO.println("\nMovendo todos os produtos...");
					
					for(int y = 0; y < tamanho; y++)
					{
						Main.crudProduto.alterarCategoria(lista[y], newIdCategoria);
					}
					
					excluir(id);
					break;
					
				default:
					IO.println("\nOpção inválida !\n");
					break;
			}
		}
		else
		{
			IO.println("\nErro ao alterar categoria !\n");
		}
	}//end alterarCategoria()

	public void menuAlteracao()
	{ 
		listarCategorias();
		
		int id = IO.readint("Digite o id da categoria a ser alterada: ");
		
		Crud.noBackMenu
		(
			"Alteração",
			"O que deseja alterar na categoria ?",
			new String[] { "nome" },
			new Runnable[]
			{
				() -> { alterar(id, 1); },
			}
		);
	}

	public void menuConsulta()
	{
		int id = IO.readint("Digite o id da categoria a ser consultada: ");
		
		consultar(id);
	}
	
	/**
	 * Lista todos os produtos que estão na categoria com o id informado.
	 * 
	 * @param id Id da categoria a se listar os produtos.
	 */
	
	public void listarProdutos(int id) 
	{				
		if(Main.crudProduto != null)
		{
			int [] lista = Main.indiceCategoriaProduto.listarDadosComAChave(id);
			int tamanho = lista.length;
			
			for(int y = 0; y < tamanho; y++)
			{
				Main.crudProduto.consultar(lista[y]);
			}//end for
		}
		
	}//end listarProdutos()
	
	public void menuListar()
	{
		Crud.noBackMenu
		(
			"Listagem",
			"O que deseja listar ?",
			new String[] { "todas as categorias", "produtos de uma categoria" },
			new Runnable[]
			{
				() -> { listarCategorias(); },
				() -> { listarProdutos( IO.readint("Entre com a categoria desejada: ") ); }
			}
		);
		
	}//end menuListar()

	public void menuExclusao()
	{
		listarCategorias();
		int id = IO.readint("Digite o id da categoria a ser removida: ");
		IO.println("");

		//testar antes se o id existe
		if (database.idIsValid(id))
		{
			Crud.noBackMenu
			(
				"Confirmação",
				"Realmente deseja excluir a categoria ?",
				new String[] { "sim" },
				new Runnable[]
				{
					() ->
					{
						int[] idsOfTheProductsOfTheCategory =
							Main.indiceCategoriaProduto.listarDadosComAChave(id);
						
						if (idsOfTheProductsOfTheCategory.length == 0)
						{
							excluir(id);				
						}
						
						else
						{
							Crud.noBackMenu
							(
								"Excluir Produtos",
								"AVISO: Ainda há produtos nesta categoria.\n" +
								"Deseja excluí-los também ?",
								new String[] { "sim", "não" },
								new Runnable[]
								{
									() ->
									{
										Main.databaseProduto.deleteObjects(idsOfTheProductsOfTheCategory);
										excluir(id);
									},

									() ->
									{
										Crud.noBackMenu
										(
											"Outras Opções",
											"Qual das seguintes operações deseja realizar ?",
											new String[] { "mover produtos para outra categoria", "criar nova categoria" },
											new Runnable[]
											{
												() -> { alterarCategoria(id, 1); },
												() -> { alterarCategoria(id, 2); }
											}
										);
									}
								}
							);
							
						}//end if
					}
				}
			);
						
		}		
		else
		{
			IO.println("Id inválido!");
		}//end if
		
	}//end menuExclusao

	public void menu()
	{
		Crud.menu
		(
			"Categoria",
			"Qual das seguintes operações o senhor deseja realizar ?",
			new String[] { "inclusão", "alteração", "exclusão", "consulta", "listagem" },
			new Runnable[]
			{
				() -> { menuInclusao(); IO.pause(); },
				() -> { menuAlteracao(); IO.pause(); },
				() -> { menuExclusao(); IO.pause(); },
				() -> { menuConsulta(); IO.pause(); },
				() -> { menuListar(); IO.pause(); }
			}
		);
	}
}
