package user;

import entidades.Cliente;
import entidades.Compra;
import entidades.ItemComprado;
import util.IO;

public class GerenciadorCompras {
	private Cliente cliente;
	private Compra compra;
	
	public GerenciadorCompras() {
		this(null,null);
	}
	
	public GerenciadorCompras(Cliente cliente, Compra compra) {
		super();
		this.cliente = cliente;
		this.compra = compra;
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
			IO.println("0 - Sair");
			
			selecao = IO.readint();
			
			switch(selecao) {
				case 0: 
					IO.println("Vai cedo vacilão");
					break;
					
				case 1: 
					menuNovaCompra();
				
			
			}
		}while(selecao != 0);
	}
	
	public void menuNovaCompra() {
		//cria uma nova compra
		//Compra compraAtual = new Compra(Main.databaseCompra);
		int produto = 0;
		int continuaCompra = 0;
		
		this.compra = new Compra(Main.databaseCompra.readLastID());
		
		IO.println("Estoque disponível");
		Main.crudProduto.listar();
		
		do {
			produto = IO.readLineUntilPositiveInt("Qual produto deseja comprar ? (Digite o id)");
			if(Main.databaseProduto.idIsValid(produto)) {
				int quantidade = IO.readLineUntilPositiveInt("Digite a quantidade do produto");
				
				Main.crudItemComprado.inserir(new ItemComprado(this.compra.getId(), produto, quantidade, Main.databaseProduto.readObject(produto).getPreco()));
				/*
				 * [EM CONSTRUÇÃO]
				 * Falta fazer a ligação dos itens comprados a essa compra
				 */
				
			}
			
			
			continuaCompra = IO.readLineUntilPositiveInt("Deseja continuar a comprar? 1-Sim 2-Não");
		}while(continuaCompra != 2);
		
		this.compra.setValorTotal(this.compra.readValorTotal());
		
		
	}
}
