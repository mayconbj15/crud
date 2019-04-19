package gerenciadores;

import entidades.Cliente;
import entidades.Compra;
import entidades.ItemComprado;
import user.Crud;
import user.Main;

import util.IO;

import java.util.ArrayList;

public class GerenciadorComprasCliente {
	private Cliente cliente;
	//private Compra compra;
	GerenciadorLogin<Cliente> gerenciadorLogin;
	
	public GerenciadorComprasCliente(Cliente cliente, Compra compra) {
		this.cliente = cliente;
		//this.compra = compra;
		
		try
		{
			this.gerenciadorLogin =
				new GerenciadorLogin<Cliente>(
					Main.databaseCliente,
					Cliente.class.getConstructor()
				);
		}
		
		catch (NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}
	}
	
	public GerenciadorComprasCliente() {
		this(null, null);
	}
	
	/**
	 * Mostra a compra que a pessoa está fazendo e pede a confirmação.
	 * Caso a pessoa confirme, insere os itens comprados na base de dados
	 * e já cria o relacionamento deles com a compra e produto por meio
	 * dos índices compostos.
	 * 
	 * @param compra Compra que está sendo feita.
	 * @param itensComprados Itens que estão sendo comprados.
	 * 
	 * @return {@code true} se a pessoa confirmar a compra. Caso
	 * contrário, {@code false}.
	 */
	
	private boolean compraConfirmada(Compra compra, ArrayList<ItemComprado> itensComprados)
	{
		boolean confirmed = false;
		
		IO.println("Você irá fazer a seguinte compra:");
		IO.println(compra);
		
		IO.println("\nCom os seguintes produtos:");
		compra.listarProdutosDaCompra(itensComprados);
		
		confirmed = IO.readint("\nConfirma (1-Sim 2-Não)? ") == 1;
		IO.println("");
		
		if(confirmed) {
			// adiciona os itens comprados à base de dados e também cria
			// o relacionamento deles com a compra e os produtos por meio
			// dos índices compostos
			Main.crudItemComprado.inserirItensComprados(itensComprados);
		}
		else {
			//cancelar compra, apenas não insere no banco de dados
			IO.println("Compra cancelada");
		}
		
		return confirmed;
	}
	
	/**
	 * Método que cria uma nova compra e a adiciona à database relacionando-a com o
	 * idCliente do atual cliente que está logado na Crud e faz os relacionamento N:N
	 * de ItemComprado com Compra e ItemComprado com Produto
	 * 
	 * @return O id da compra se tudo der certo. Caso contrário, -1.
	 */
	
	private int menuNovaCompra() {
		return
		Main.databaseCompra.writeObject // fornece o id da entidade antes de inseri-la de fato
		(
			(idCompra) ->
			{
				Compra compra = new Compra(this.cliente.getId());
				ArrayList<ItemComprado> itensComprados = new ArrayList<ItemComprado>();
				ItemComprado itemComprado = null;
				
				int continuaCompra = 0;
				
				do {
					IO.println("Estoque disponível");
					Main.crudProduto.listar();
					
					// pergunta qual o produto e qual a quantidade deve ser comprada,
					// depois, gera o ItemComprado
					itemComprado = Main.crudItemComprado.novoItemComprado(idCompra);
					
					if (itemComprado != null)
					{
						itensComprados.add(itemComprado);
					}
					
					continuaCompra = IO.readLineUntilPositiveInt(
							"\nDeseja continuar a comprar (1-Sim 2-Não)? ");
					
					IO.println("");
				
				} while(continuaCompra != 2);
				
				compra.setValorTotal(Compra.readValorTotal(itensComprados));
				
				if (itensComprados.size() == 0 || !compraConfirmada(compra, itensComprados))
				{
					compra = null;
				}
				
				else
				{
					Main.indiceClienteCompra.inserir(compra.getIdCliente(), idCompra);
				}
				
				return compra;
			}
		);
	}
	
	private void menuCliente() {
		Crud.menu
		(
			"Cliente",
			"Qual das seguintes operações o senhor deseja realizar ?",
			new String[] { "comprar", "desfazer uma compra", "consultar uma compra", "listar suas compras" },
			new Runnable[]
			{
				() -> { menuNovaCompra(); IO.pause(); },
				() -> { IO.pause(); },
				() -> { Main.crudCompra.menuConsulta(); IO.pause(); },
				() -> { Main.crudCompra.menuListar(); IO.pause(); }
			}
		);
	}
	
	public void menu() {
		Cliente usuarioLogado = gerenciadorLogin.menuAutenticacao();
		
		if (usuarioLogado != null)
		{
			this.cliente = usuarioLogado;
			menuCliente();
		}
	}
}
