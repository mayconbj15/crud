package gerenciadores;

import entidades.Funcionario;
import user.Main;
import util.IO;

public class GerenciadorFuncionario {
	private Funcionario funcionario;
	
	public GerenciadorFuncionario() {
		
	}
	
	public void menu(){
		int selecao = 0;
		
		IO.println("[Menu Principal Funcionários]");
		IO.println("Qual das seguintes entidades o senhor deseja manusear ?");
		IO.println("Digite:");
		IO.println("1 para produto");
		IO.println("2 para categoria");
		IO.println("0 para sair");
		IO.println("");

		selecao = IO.readint("Opção: ");
		
		switch (selecao){
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
}
