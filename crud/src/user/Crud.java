package user;

import util.*;

import crud.Arquivo;
import entidades.Categoria;
import entidades.Produto;

/**
 * Classe que gerencia a interação com o usuário.
 */

public class Crud {
//	private CrudProduto crudProduto;
//	private CrudCategoria crudCategoria;
	
	public Crud(Arquivo<Produto> databaseProduto, Arquivo<Categoria> databaseCategoria) {
		
		Main.crudProduto = new CrudProduto(databaseProduto);
		Main.crudCategoria = new CrudCategoria(databaseCategoria);
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

			IO.println("\n--------------------------------------------\n");
			
		} while (selecao != 0);

	}//end menu()
	
}//end class Main
