package user;

import crud.Arquivo;
import entidades.Cliente;
import util.IO;

import java.util.ArrayList;

public class CrudCliente extends CrudAbstract<Cliente>
{
	public CrudCliente(Arquivo<Cliente> database)
	{
		super(database);
	}
	
	public int menuInclusao()
	{				
		Cliente cliente = new Cliente();
		
		cliente.readName();		
		cliente.readEmail();
		cliente.readSenha();
		
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
			IO.println("\nCliente não encontrado.\n");
		}

		return cliente;
		
	}//end alterar()	

	public void menuAlteracao()
	{ 
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
		int cod = -1;
		int idCliente = -1;
		
		IO.println("O que deseja listar ?");
		IO.println("Digite:");
		IO.println("1 Todos os clientes;");
		IO.println("2 Compras de um cliente.");
		IO.println("");
		cod = IO.readint("Opção: ");
		
		switch(cod) 
		{
			case 1:
				listarClientes();			
				break;
				
			case 2:
				idCliente = IO.readint("Entre com o id do cliente desejado: ");
				
				if (database.idIsValid(idCliente)) 
				{
					listarCompras(idCliente);
				}
				else 
				{
					IO.println("Id inválido!");
				}				
				break;
				
			default:
				IO.println("Opção inválida.");		
				
		}//end switch-case
	}
	
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
				if(Main.databaseCliente.indice.listarDadosComAChave(id).length == 0)
				{
					excluir(id);	
				}				
				else
				{
					cod = -1;
						
					IO.println("AVISO: Ainda há compras registradas em nome deste cliente.");
					IO.println("Ainda deseja excluí-lo? ");
					IO.println("Digite:");
					IO.println("1 Sim");
					IO.println("2 Não");
					IO.println("");
					cod = IO.readint("Opção: ");
					
					if(cod == 1) 
					{
						//exclui o cliente da databaseCliente
						excluir(id);
						
						//excluir as compras do cliente
						int[] listOfInvalids = Main.indiceClienteCompra.listarDadosComAChave(id);
						
						Main.databaseCompra.deleteObjects(listOfInvalids);
						
					}//end if
					
				}//end if
				
			}//end if
		}		
		else
		{
			IO.println("Id inválido!");
		}//end if
		
	}//end menuExclusao

	public Cliente login() {
		//usar o id como campo de login pela estrutura que estamos usando
		Cliente cliente = new Cliente();
		
		cliente.readEmail();
		cliente.readSenha();
		
		return autentica(cliente);
		
	}
	
	public Cliente autentica(Cliente cliente) {
		Cliente clienteValido = null;
		
		/*por enquanto vamos ter que fazer sequencial mesmo por estarmos tratando com nome
		 * Possíveis soluções que acho
		 * 1 - criar um novo atributo número para fazer autenticação 
		 * 2 - implmentar uma lista invertida
		 * */
		
		ArrayList<Cliente> listaClientes = Main.databaseCliente.list(); 
		int size = listaClientes.size();
		
		for(int i=0; i < size && clienteValido == null; i++) {
			if(listaClientes.get(i).getEmail().contentEquals(cliente.getEmail())) {
				//testa se a senha é a mesma
				if(listaClientes.get(i).getSenha().contentEquals(cliente.getSenha())) {
					clienteValido = listaClientes.get(i);
				}
			}
		}
		
		return clienteValido;
	}
	
	public void menu()
	{
		int selecao;
		//Cliente cliente = new Cliente();
		
		IO.println("Olá, meu nobre!\n");
		
		do {
			//Interface de entrada
			IO.println("[Menu Cliente]");
			IO.println("Bem vindo ao Mercadão Boladão");
			
			selecao = IO.readint();
			
			IO.println("Qual das seguintes operações o senhor deseja realizar ?");
			IO.println("Digite:");
			IO.println("1 - Alterar um cliente");
			IO.println("2 - Excluir um cliente"); //seria alterar ou cancelar uma compra
			IO.println("3 - Para consultar clientes"); //
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
					menuAlteracao();
					IO.pause();
					break;
					
				case 2:
					menuExclusao();
					IO.pause();
					break;
					
				case 3:
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
