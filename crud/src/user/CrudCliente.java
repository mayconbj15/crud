package user;

import crud.Arquivo;
import entidades.Cliente;
import entidades.Produto;
import util.IO;

public class CrudCliente extends CrudAbstract<Cliente>
{
	public CrudCliente(Arquivo<Cliente> database)
	{
		super(database);
	}
	
	public int menuInclusao()
	{
		Cliente cliente = new Cliente();
		
		cliente.setNome(cliente.readName());
		
		return inserir(cliente);
	}

	/**
	 * Altera um campo específico do cliente com id informado.
	 * {@code cod} é responsável por indicar qual dos campos do
	 * cliente deseja-se alterar.
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
	
	public Cliente alterar(int id, int cod)
	{
		// procurar o cliente desejado na base de dados
		Cliente cliente =  database.readObject(id);
		
		if (cliente != null) // checa se o cliente foi encontrado
		{			
			switch (cod)
			{
				case 0:
					cliente = null;
					IO.println("Operação cancelada\n");
					break;

				case 1:
					cliente.readName();
					break;
					
				case 2:
					cliente.readEmail();
					break;
					
				default:
					cliente = null;
					IO.println("\nOpção inválida !\n");
					break;
			}
			
			
			if (cliente != null)
			{
				cliente = alterar(id, cliente);
			}
		}
		
		else
		{
			IO.println("\nCategoria não encontrada.\n");
		}

		return cliente;
		
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
	/*
	public void alterarCategoria(int id, int cod)
	{
		if(Main.crudProduto != null)
		{
			int [] lista = Main.indiceComposto.listarDadosComAChave(id);
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
	*/

	public void menuAlteracao()
	{ 
		listarClientes();
		int cod = -1; //codigo de selecao
		int id = IO.readint("Digite o id do cliente a ser alterado: ");

		//testar antes se o id existe
		if(database.idIsValid(id))
		{
			IO.println("O que deseja alterar no cliente? ");
			IO.println("Digite:");
			IO.println("1 para alterar o nome;");
			IO.println("2 para alterar o email;");
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
	
	/**
	 * Lista todos os clientes cadastrados.	 	
	 */	
	public void listarClientes() 
	{				
		if( Main.crudCliente != null )
		{
			listar();
		}
		else 
		{			
			System.out.println("Não há clientes na base de dados.");
		}//end if
		
	}//end listarClientes()
	
	/*
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
	*/
	
	public void menuExclusao()
	{
		listarClientes();
		int cod = -1; //codigo de selecao
		int id = IO.readint("Digite o id do cliente a ser removido: ");

		//testar antes se o id existe
		if (database.idIsValid(id))
		{
			IO.println("Realmente deseja excluir o cliente ?");
			IO.println("Digite:");
			IO.println("1 Sim");
			IO.println("2 Não");
			IO.println("");
			cod = IO.readint("Opção: ");
			
			if (cod == 1)
			{															
				Cliente clienteExcluido = excluir(id);
					
				Main.indiceClienteCompra.excluir(
					clienteExcluido.getId()
				);
			}
			/*
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
					
				}//end if
				
			}//end if
			*/			
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
			IO.println("[Menu Cliente]");
			IO.println("Qual das seguintes operações o senhor deseja realizar ?");
			IO.println("Digite:");
			IO.println("1 para inclusão");
			IO.println("2 para alteração");
			IO.println("3 para exclusão");
			IO.println("4 para consulta");			
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
					
				default:
					IO.println("Operação inválida\n");
					break;
			}
			
			IO.println("\nOperação finalizada.\n");
			IO.println("--------------------------------------------\n");
			
		} while (selecao != 0);
	}
}
