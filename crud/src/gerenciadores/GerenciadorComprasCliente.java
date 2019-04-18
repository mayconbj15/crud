package gerenciadores;

import entidades.Cliente;
import entidades.Compra;
import entidades.ItemComprado;

import user.Main;

import util.IO;

import java.util.ArrayList;

public class GerenciadorComprasCliente {
	private Cliente cliente;
	//private Compra compra;
	
	public GerenciadorComprasCliente() {
		this(null,null);
	}
	
	public GerenciadorComprasCliente(Cliente cliente, Compra compra) {
		super();
		this.cliente = cliente;
		//this.compra = compra;
	}
	
	public void menu() {
		int selecao = 0;
		
		IO.println("Autenticação");
		
		
		do {
			IO.println("1 - Fazer login");
			IO.println("2 - Novo cliente");
			IO.println("0 - Sair");
			
			selecao = IO.readint();
			
			if(selecao == 1) {
				//fazer login
				this.cliente = Main.crudCliente.login();
				if(this.cliente != null) {
					menuCliente();
				}
				else {
					IO.println("Email ou senha incorreta");
				}
			}
			else if(selecao == 2) {
				//criar novo cliente
				Main.crudCliente.menuInclusao();
			}
		}while(selecao != 0);
	}
	
	private void menuCliente() {
		int selecao = 0;
		
		do {
			IO.println("O que deseja meu bom?");
			IO.println("1 - Realizar uma compra");
			IO.println("2 - Desfazer uma compra");
			IO.println("3 - Consultar uma compra");
			IO.println("4 - Listar suas compras");
			IO.println("0 - Sair");
			
			selecao = IO.readint();
			
			switch(selecao) {
				case 0: 
					IO.println("Vai cedo vacilão");
					break;
					
				case 1: 
					menuNovaCompra(); break;
				
				case 3:
					Main.crudCompra.menuConsulta();
					IO.pause();
					break;
					
				case 4:
					Main.crudCompra.menuListar();
					IO.pause();
					break;
					
				default:
					IO.println("Operação inválida\n");
					break;
			}
			
			IO.println("\nOperação finalizada.\n");
			IO.println("--------------------------------------------\n");
	
		}while(selecao != 0);
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
		
		IO.println("Você irá fazer a seguinte compra");
		IO.println(compra);
		
		IO.println("Com os seguintes produtos");
		compra.listarProdutosDaCompra(itensComprados);
		
		confirmed = IO.readint("Confirma? (1-Sim 2-Não): ") == 1;
		
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
	
	public int menuNovaCompra() {
		return
		Main.databaseCompra.writeObject
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
					
					continuaCompra = IO.readLineUntilPositiveInt("Deseja continuar a comprar? 1-Sim 2-Não");
				
				} while(continuaCompra != 2);
				
				compra.setValorTotal(Compra.readValorTotal(itensComprados));
				
				if (!compraConfirmada(compra, itensComprados))
				{
					compra = null;
				}
				
				return compra;
			}
		);
	}
}
