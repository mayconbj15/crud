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
				success = alterar(id, categoria);
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
		listarCategorias();
		int cod = -1; //codigo de selecao
		int id = IO.readint("Digite o id da categoria a ser alterada: ");

		//testar antes se o id existe
		if(database.idIsValid(id))
		{
			IO.println("O que deseja alterar na categoria?");
			IO.println("Digite:");
			IO.println("1 para alterar o nome");
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

	public void menuConsulta()
	{
		int id = IO.readint("Digite o id da categoria a ser consultada: ");
		
		consultar(id);
	}
	
	public void listarProdutos(int id) 
	{				
		if(Main.crudProduto != null){
			int [] lista = Main.indiceComposto.listarDadosComAChave(id);
			int tamanho = lista.length;
			
			for(int y = 0; y < tamanho; y++)
			{
				Main.crudProduto.consultar(lista[y]);
			}//end for
		}
		
	}//end listarProdutos()
	
	public void alterarCategoria(int id, int cod)
	{
		if(Main.crudProduto != null){
			int [] lista = Main.indiceComposto.listarDadosComAChave(id);
			int tamanho = lista.length;
			
			switch (cod)
			{
				case 1: // mover produtos para categorias diferentes
					IO.println("\nMova cada produto para a categoria desejada\n");
					
					for(int y = 0; y < tamanho; y++)
					{
						Main.crudProduto.consultar(lista[y]);
						Main.crudProduto.alterar(lista[y], 1);
					}//end for
					
					break;
					
				case 2: // criar nova categoria e inserir elementos nela
					int newID = menuInclusao();
					
					for(int y = 0; y < tamanho; y++)
					{
						Main.crudProduto.alterarCategoria(lista[y], newID);
					}//end for
					
					break;
			}
		}// end if
	}//end alterarCategoria()
	
	public void menuListar()
	{
		int cod = -1;
		int idCategoria = -1;
		
		IO.println("O que deseja listar ?");
		IO.println("Digite:");
		IO.println("1 Todas as categorias;");
		IO.println("2 Produtos de uma categoria.");
		IO.println("");
		cod = IO.readint("Opção: ");
		
		listarCategorias();
		
		switch(cod) 
		{
			case 1:
				break;
				
			case 2:
				idCategoria = IO.readint("Entre com a categoria desejada: ");
				listarProdutos(idCategoria);
				break;
				
			default:
				IO.println("Opção inválida.");		
				
		}//end switch-case
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
						
						switch (cod)
						{
							case 0:
								IO.println("Até breve :)");
								break;
								
							case 1:
								alterarCategoria(id, cod);
								excluir(id);
								break;
								
							case 2:
								menuInclusao();
								alterarCategoria(id, cod);
								excluir(id);
								break;
								
						}
					}
					
				}//end if
				
			}//end if
		}		
		else
		{
			IO.println("Id inválido!");
		}//end if
		
	}//end menuExclusao

	
	public void menu()
	{
		int selecao;
		
		IO.println("Olá, meu nobre!\n");
		
		do {
			//Interface de entrada
			IO.println("Qual das seguintes operações o senhor deseja realizar ?");
			IO.println("Digite:");
			IO.println("1 para inclusão");
			IO.println("2 para alteração");
			IO.println("3 para exclusão");
			IO.println("4 para consulta de categoria");
			IO.println("5 para listar todas as categorias");
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
					menuInclusao();
					IO.pause();
					break;
					
				case 2:
					menuAlteracao();
					IO.pause();
					break;
					
				case 3:
					menuExclusao();
					IO.pause();
					break;
					
				case 4:
					menuConsulta();
					IO.pause();
					break;
					
				case 5:
					menuListar();
					IO.pause();
					break;
					
				default:
					IO.println("Operação inválida\n");
					break;
			}
			
			IO.println("\nOperação finalizada.\n");
			IO.println("--------------------------------------------\n");
			
		} while (selecao != 0);
	}
}
