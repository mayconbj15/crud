package gerenciadores;

import entidades.Cliente;
import entidades.Compra;
import entidades.ItemComprado;
import user.Main;
import util.IO;

public class GerenciadorComprasCliente {
	private Cliente cliente;
	private Compra compra;
	
	public GerenciadorComprasCliente() {
		this(null,null);
	}
	
	public GerenciadorComprasCliente(Cliente cliente, Compra compra) {
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
		int idProduto = 0;
		int quantidadeDeProdutos = 0;
		int continuaCompra = 0;
		
		this.compra = new Compra(Main.databaseCompra.readLastID());
		
		IO.println("Estoque disponível");
		
		
		do {
			Main.crudProduto.listar();
			idProduto = IO.readLineUntilPositiveInt("Qual produto deseja comprar ? (Digite o id)");
			quantidadeDeProdutos = IO.readLineUntilPositiveInt("Digite a quantidade do produto");
			if(Main.databaseProduto.idIsValid(idProduto) && quantidadeDeProdutos <= Main.databaseProduto.readObject(idProduto).getQuantidade()) {
				
				
				Main.crudItemComprado.inserir(new ItemComprado(this.compra.getId(), idProduto, quantidadeDeProdutos, Main.databaseProduto.readObject(idProduto).getPreco()));
				/*
				 * [EM CONSTRUÇÃO]
				 * Falta fazer a ligação dos itens comprados a essa compra
				 */
				
			}
			else {
				IO.println("Id inválido");
			}
			
			
			continuaCompra = IO.readLineUntilPositiveInt("Deseja continuar a comprar? 1-Sim 2-Não");
		}while(continuaCompra != 2);
		
		this.compra.setValorTotal(this.compra.readValorTotal());
		
		
	}
}
