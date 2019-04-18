package gerenciadores;

import entidades.Cliente;
import entidades.Compra;
import entidades.ItemComprado;

import user.Main;

import util.IO;

import java.util.function.Function;

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
	
	public void menuNovaCompra() {
		//cria uma nova compra
		//Compra compraAtual = new Compra(Main.databaseCompra);
		int idProduto = 0;
		int quantidadeDeProdutos = 0;
		int continuaCompra = 0;
		
		//Main.databaseCompra.writeObject(Function<Integer, Compra>);
		Compra compra = new Compra(Main.databaseCompra.createNewId(), this.cliente.getId());
		
		IO.println("Estoque disponível");
		
		do {
			Main.crudProduto.listar();
			idProduto = IO.readLineUntilPositiveInt("Qual produto deseja comprar ? (Digite o id)");
			if(Main.databaseProduto.idIsValid(idProduto)) {
				quantidadeDeProdutos = IO.readLineUntilPositiveInt("Digite a quantidade do produto");
				if(quantidadeDeProdutos <= Main.databaseProduto.readObject(idProduto).getQuantidade()) {		
					Main.crudItemComprado.inserir(new ItemComprado(compra.getId(), idProduto, quantidadeDeProdutos, Main.databaseProduto.readObject(idProduto).getPreco()));
					//criando o relacionamento do produto com a atual compra
					Main.indiceCompraItemComprado.inserir(compra.getId(), idProduto);
					Main.indiceProdutoItemComprado.inserir(idProduto, compra.getId());
					/*
					 * [EM CONSTRUÇÃO]
					 * Falta fazer a ligação dos itens comprados a essa compra
					 */
					
				}
				else {
					IO.println("Quantidade inválida");
				}
			}
			else {
				IO.println("ID inválido");
			}
			
			continuaCompra = IO.readLineUntilPositiveInt("Deseja continuar a comprar? 1-Sim 2-Não");
		}while(continuaCompra != 2);
		
		compra.setValorTotal(compra.readValorTotal());
		
		IO.println("Você irá fazer a seguinte compra");
		IO.println(compra);
		IO.println("Com os seguintes produtos");
		
		int[] listaItensComprados = Main.indiceCompraItemComprado.listarDadosComAChave(compra.getId());
		for(int i=0; i < listaItensComprados.length; i++) {
			IO.println(Main.databaseItemComprado.readObject(listaItensComprados[i]));
		}
		
		int confirm = IO.readint("Confirma? 1-Sim 2-Não");
		if(confirm == 1) {
			Main.crudCompra.menuInclusao(compra); //adiciona a compra ao banco de dados
		}
		else {
			IO.println("Compra cancelada");
		}
		
		
	}
}
