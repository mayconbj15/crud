package user;

import crud.Arquivo;

import entidades.Cliente;
import gerenciadores.GerenciadorLogin;
import util.IO;

public class CrudCliente extends CrudAbstract<Cliente>
{
	GerenciadorLogin<Cliente> gerenciadorLogin;
	
	public CrudCliente(Arquivo<Cliente> database)
	{
		super(database);
		
		try
		{
			this.gerenciadorLogin =
				new GerenciadorLogin<Cliente>(
					database,
					Cliente.class.getConstructor()
				);
		}
		
		catch (NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}
	}
	
	public GerenciadorLogin<Cliente> getGerenciadorLogin()
	{
		return gerenciadorLogin;
	}
	
	public int menuInclusao()
	{				
		Cliente cliente = new Cliente();
		int idCliente = -1; 
		
		cliente.readNome();		
		cliente.readEmail();
		cliente.readSenha();
		
		idCliente = inserir(cliente);
		
		if(idCliente != -1)
			Main.indiceEmailUsuarioIdUsuario.inserir(cliente.getEmail(), idCliente);
		
		return idCliente;
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
					cliente.readNome();
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
			IO.println("\nCliente não encontrado.\n");
		}

		return cliente;
		
	}//end alterar()	

	public void menuAlteracao()
	{ 
		int id = IO.readint("Digite o id do cliente a ser alterado: ");

		//testar antes se o id existe
		if(database.idIsValid(id))
		{
			Crud.menu
			(
				"Alteração",
				"O que deseja alterar no cliente ?",
				new String[] { "nome", "email" },
				new Runnable[]
				{
					() -> { alterar(id, 1); },
					() -> { alterar(id, 2); }
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
		int id = IO.readint("Digite o id do cliente a ser consultado: ");
		
		consultar(id);
	}

	/**
	 * Lista todos os clientes cadastrados.	 	
	 */
	
	public void listarClientes() 
	{				
		if( Main.crudCliente != null )
		{
			IO.println("Clientes disponíveis: \n");
			listar();
		}
		else 
		{			
			System.out.println("Não há clientes na base de dados.");
		}//end if
		
	}//end listarClientes()
	
	/**
	 * Listar compras de clientes
	 * 
	 * @param {@code}id
	 */
	
	public void listarCompras(int id) 
	{				
		if(Main.crudCompra != null)
		{
			int [] lista = Main.indiceClienteCompra.listarDadosComAChave(id);
			int tamanho = lista.length;
			
			for(int y = 0; y < tamanho; y++)
			{
				Main.crudCompra.consultar(lista[y]);
			}//end for
			
		}//end if
		
	}//end listarCompras()
	
	public void menuListar()
	{
		Crud.menu
		(
			"Listagem",
			"O que deseja listar ?",
			new String[] { "clientes", "compras de um cliente" },
			new Runnable[]
			{
				() -> { listarClientes(); },
				() ->
				{
					int idCliente = IO.readint("Entre com o id do cliente desejado: ");
					
					if (database.idIsValid(idCliente)) 
					{
						listarCompras(idCliente);
					}
					
					else 
					{
						IO.println("Id inválido!");
					}
				}
			}
		);
	}
	
	public void menuExclusao()
	{		
		listarClientes();
		
		int id = IO.readint("Digite o id do cliente a ser removido: ");

		//testar antes se o id existe
		if (database.idIsValid(id))
		{
			Crud.menu
			(
				"Exclusão",
				"Realmente deseja excluir o cliente ?",
				new String[] { "sim" },
				new Runnable[]
				{
					() ->
					{
						int[] idsOfThePurchasesOfTheClient =
							Main.indiceClienteCompra.listarDadosComAChave(id);
						
						if(idsOfThePurchasesOfTheClient.length == 0)
						{
							excluir(id);	
						}
						
						else
						{
							Crud.menu
							(
								"Excluir Compras",
								"AVISO: Ainda há compras registradas em nome deste cliente.\n" +
								"Ainda sim deseja excluí-lo ?",
								new String[] { "sim" },
								new Runnable[]
								{
									() ->
									{
										// exclui o cliente da databaseCliente
										excluir(id);
										// exclui as compras do cliente
										Main.databaseCompra.deleteObjects(idsOfThePurchasesOfTheClient);
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
			"Cliente",
			"Bem vindo ao Mercadão Boladão\n" +
			"Qual das seguintes operações o senhor deseja realizar ?",
			new String[] { "alterar um cliente", "excluir um cliente", "consultar um cliente" },
			new Runnable[]
			{
				() -> { menuAlteracao(); IO.pause(); },
				() -> { menuExclusao(); IO.pause(); },
				() -> { menuConsulta(); IO.pause(); }
			}
		);
	}
}
