package user;

import util.*;

import crud.Arquivo;
import entidades.*;

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
		
		
		do {
			//Interface de entrada
			IO.println("Olá, meu nobre!\n");
			IO.println("Deseja fazer login como funcionário ou cliente?");	
			IO.println("1 - Cliente");
			IO.println("2 - Funcionário");
			IO.println("0 - Sair");
			
			selecao = IO.readLineUntilPositiveInt("");
			if(selecao == 1) {
				GerenciadorCompras gerenciador = new GerenciadorCompras();
				gerenciador.menu();
			}
			
			else if(selecao == 2) {
				IO.println("[Menu Principal Funcionários]");
				IO.println("Qual das seguintes entidades o senhor deseja manusear ?");
				IO.println("Digite:");
				IO.println("1 para produto");
				IO.println("2 para categoria");
				IO.println("0 para sair");
				IO.println("");

				selecao = IO.readint("Opção: ");
				
				switch (selecao)
				{
					case 0:
						IO.println("Até breve :)");
						break;
					
					case 1:
						Main.crudProduto.menu();
						IO.pause();
						break;
						
					case 2:
						Main.crudCategoria.menu();
						IO.pause();
						break;
						
					default:
						IO.println("Entidade inválida\n");
						break;
				}
			}
			
			IO.println("\n--------------------------------------------\n");
			
		} while (selecao != 0);

	}//end menu()
	
}//end class Main
