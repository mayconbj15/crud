package gerenciadores;

import entidades.Cliente;
import entidades.Compra;
import entidades.ItemComprado;

import user.Main;

import util.IO;

import java.util.function.Function;
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
	 * Método que cria uma nova compra e adiciona a database relacionando com o idCliente do atual cliente que 
	 * 	está logado na Crud e fazendo os relacionamento N:N de Compra e Produto
	 */
	public void menuNovaCompra() {
		int continuaCompra = 0;
		
		//usar novo metodo para recuperar id pra gerar o provavel novo id da nova compra
		Compra compra = new Compra(Main.databaseCompra.createNewId(), this.cliente.getId());
		ArrayList<ItemComprado> itensComprados = new ArrayList<ItemComprado>();
		
		do {
			IO.println("Estoque disponível");
			Main.crudProduto.listar();
			
			//usar novo metodo para recuperar id pra gerar os provaveis ids dos novos itensComprados
			itensComprados = Main.crudItemComprado.novosItensComprados(compra.getId());
			
			continuaCompra = IO.readLineUntilPositiveInt("Deseja continuar a comprar? 1-Sim 2-Não");
		
		}while(continuaCompra != 2);
		
		compra.setValorTotal(compra.readValorTotal(itensComprados));
		
		IO.println("Você irá fazer a seguinte compra");
		IO.println(compra);
		
		IO.println("Com os seguintes produtos");
		compra.listarProdutosDaCompra(itensComprados);
		
		int confirm = IO.readint("Confirma? 1-Sim 2-Não");
		
		if(confirm == 1) {
			//adicionar os itensComprados as compras
			Main.crudItemComprado.novosItensComprados(itensComprados);
			Main.crudCompra.menuInclusao(compra); //adiciona a compra ao banco de dados
		}
		else {
			//cancelar compra, apenas não insere no banco de dados
			IO.println("Compra cancelada");
		}
		
	}
}
