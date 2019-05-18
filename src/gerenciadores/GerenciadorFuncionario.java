package gerenciadores;

import entidades.Funcionario;
import user.Crud;
import user.Main;
import util.IO;

public class GerenciadorFuncionario {
	//private Funcionario funcionario;
	GerenciadorLogin<Funcionario> gerenciadorLogin;
	public Funcionario usuarioLogado;
	
	public GerenciadorFuncionario(Funcionario funcionario) {
		//this.funcionario = funcionario;
		
		try
		{
			this.gerenciadorLogin =
				new GerenciadorLogin<>(
					Main.databaseFuncionario,
					Funcionario.class.getConstructor()
				);
		}
		
		catch (NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}
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
			new String[] { "produto", "categoria", "cliente" },
			new Runnable[]
			{
				() -> { Main.crudProduto.menu(); IO.pause(); },
				() -> { Main.crudCategoria.menu(); IO.pause(); },
				() -> { Main.crudCliente.menu(); IO.pause(); }
			}
		);
	}
	
	public void menu(){
		usuarioLogado = gerenciadorLogin.menuAutenticacao();
		
		if (usuarioLogado != null)
		{
			//this.funcionario = usuarioLogado;
			menuFuncionario();
		}
	}
}
