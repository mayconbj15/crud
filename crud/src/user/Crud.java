package user;

import util.*;

import crud.Arquivo;

import entidades.*;

import gerenciadores.GerenciadorComprasCliente;
import gerenciadores.GerenciadorFuncionario;

/**
 * Classe que gerencia a interação com o usuário.
 */

public class Crud {
	
	public Crud(
		Arquivo<Compra> databaseCompra,
		Arquivo<Produto> databaseProduto,
		Arquivo<Cliente> databaseCliente,
		Arquivo<Categoria> databaseCategoria,
		Arquivo<ItemComprado> databaseItemComprado)
	{

		Main.crudCompra = new CrudCompra(databaseCompra);
		Main.crudCliente = new CrudCliente(databaseCliente);
		Main.crudProduto = new CrudProduto(databaseProduto);
		Main.crudCategoria = new CrudCategoria(databaseCategoria);
		Main.crudItemComprado = new CrudItemComprado(databaseItemComprado);
	}

	/**
	 * Gerencia a interação com o usuário.
	 */
	
	public void menu()
	{
		int selecao = -1;
		
		IO.println("Olá, meu nobre!\n");
		
		
		do {
			//Interface de entrada
			IO.println("Deseja fazer login como funcionário ou cliente?");	
			IO.println("1 - Cliente");
			IO.println("2 - Funcionário");
			IO.println("0 - Sair");
			
			selecao = IO.readLineUntilPositiveInt("");
			if(selecao == 1) {
				GerenciadorComprasCliente gerenciadorComprasCliente = new GerenciadorComprasCliente();
				gerenciadorComprasCliente.menu();
			}
			
			else if(selecao == 2) {
				GerenciadorFuncionario gerenciadorFuncionario = new GerenciadorFuncionario();
				gerenciadorFuncionario.menu();
			}
			
			
			IO.println("\n--------------------------------------------\n");
			
		} while (selecao != 0);

	}//end menu()
	
}//end class Main
