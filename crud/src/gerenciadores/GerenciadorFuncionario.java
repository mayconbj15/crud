package gerenciadores;

import entidades.Funcionario;
import user.Crud;
import user.Main;
import util.IO;

public class GerenciadorFuncionario {
	//private Funcionario funcionario;
	
	public GerenciadorFuncionario(Funcionario funcionario) {
		//this.funcionario = funcionario;
	}
	
	public GerenciadorFuncionario()
	{
		this(null);
	}
	
	public void menuFuncionario()
	{
		Crud.menu
		(
			"Funcionário",
			"Qual das seguintes entidades o senhor deseja manusear ?",
			new String[] { "produto", "categoria" },
			new Runnable[]
			{
				() -> { Main.crudProduto.menu(); IO.pause(); },
				() -> { Main.crudCategoria.menu(); IO.pause(); }
			}
		);
	}
	
	public void menu(){
		Funcionario usuarioLogado = Main.crudFuncionario.getGerenciadorLogin().menuAutenticacao();
		
		if (usuarioLogado != null)
		{
			//this.funcionario = usuarioLogado;
			menuFuncionario();
		}
	}
}
