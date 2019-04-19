package gerenciadores;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import crud.Arquivo;
import entidades.Entidade;
import serializaveis.SerializavelAbstract;
import user.Main;
import util.IO;

public class GerenciadorLogin<TIPO_ENTIDADE extends SerializavelAbstract & Entidade & Autenticavel>
{
	private Arquivo<TIPO_ENTIDADE> database;
	Constructor<TIPO_ENTIDADE> userConstructor;
	
	public GerenciadorLogin(
		Arquivo<TIPO_ENTIDADE> database,
		Constructor<TIPO_ENTIDADE> userConstructor)
	{
		this.database = database;
		this.userConstructor = userConstructor;
	}
	
	/**
	 * Registra um usuário na base de dados.
	 * 
	 * @return {@code null} se algo der errado. Caso contrário,
	 * retorna o usuário registrado.
	 */
	
	private TIPO_ENTIDADE register()
	{
		TIPO_ENTIDADE user = null;

		if (userConstructor != null && database != null)
		{
			try
			{
				user = userConstructor.newInstance();
			}
			
			catch (InstantiationException |
					IllegalAccessException |
					IllegalArgumentException |
					InvocationTargetException e)
			{
				e.printStackTrace();
			}
			
			if (user != null)
			{
				user.readNome();
				user.readEmail();
				user.readSenha();
				
				int userId = database.writeObject(user);
				
				if (userId != -1)
				{
					Main.indiceEmailUsuarioIdUsuario.inserir(user.getEmail(), user.getId());
				}
			}
		}
		
		return user;
	}
	
	/**
	 * Obtém o usuário com as credenciais informadas.
	 * 
	 * @param email Email do usuário.
	 * @param senha Senha do usuário.
	 * 
	 * @return {@code null} se algo der errado. Caso contrário,
	 * retorna o usuário com as credenciais informadas.
	 */
	
	private TIPO_ENTIDADE getUser(String email, String senha)
	{
		TIPO_ENTIDADE user = null;
		
		if (database != null)
		{
			int userId = Main.indiceEmailUsuarioIdUsuario.pesquisarDadoPelaChave(email);
			
			if (userId != Integer.MIN_VALUE)
			{
				user = database.readObject(userId);
				
				if ( !senha.equals(user.getSenha()) )
				{
					user = null;
				}
			}
		}
		
		return user;
	}
	
	/**
	 * Tenta logar com as credenciais do suposto usuário informado.
	 * 
	 * @param supposedUser Suposto usuário válido.
	 * 
	 * @return {@code null} se algo der errado. Caso contrário,
	 * retorna o real usuário com as credenciais informadas.
	 */
	
	private TIPO_ENTIDADE login(TIPO_ENTIDADE supposedUser)
	{
		TIPO_ENTIDADE user = null;
		
		if (supposedUser != null)
		{
			user = getUser(supposedUser.getEmail(), supposedUser.getSenha());
		}
		
		return user;
	}
	
	/**
	 * Tenta logar com as credenciais do suposto usuário informado.
	 * 
	 * @param supposedUser Suposto usuário válido.
	 * 
	 * @return {@code null} se algo der errado. Caso contrário,
	 * retorna o real usuário com as credenciais informadas.
	 */
	
	private TIPO_ENTIDADE login()
	{
		TIPO_ENTIDADE entity = null;

		try
		{
			entity = userConstructor.newInstance();
		}
		
		catch (InstantiationException |
				IllegalAccessException |
				IllegalArgumentException |
				InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
		if (entity != null)
		{
			entity.readEmail();
			entity.readSenha();
		}
		
		return login(entity);
	}
	
	/**
	 * Apresenta as opções de autenticação para o usuário por meio de um menu.
	 * <p>As opções do menu encontram-se na tabela abaixo:</p>
	 * 
	 * <table style="border: 1px solid black; text-align: center;">
	 * 
	 * 	<tr>
	 * 		<th>opção</th> <th>operação</th>
	 * 	</tr>
	 * 
	 * 	<tr>
	 * 		<td>1</td> <td>login</td>
	 * 	</tr>
	 * 
	 * 	<tr>
	 * 		<td>2</td> <td>registrar</td>
	 * 	</tr>
	 * 
	 * 	<tr>
	 * 		<td>0</td> <td>sair</td>
	 * 	</tr>
	 * 
	 * </table>
	 * 
	 * @return {@code null} se o usuário não completar nenhuma operação.
	 * Caso contrário, retorna o último usuário cadastrado ou o usuário logado.
	 */
	
	public TIPO_ENTIDADE menuAutenticacao()
	{
		TIPO_ENTIDADE user = null;
		int selecao;

		do {
			IO.println("[Autenticação]");
			IO.println("Qual das seguintes operações o senhor deseja realizar ?");
			IO.println("Digite:");
			IO.println("1 para fazer login");
			IO.println("2 para se cadastrar");
			IO.println("0 para sair");
			IO.println("");
			selecao = IO.readint("Operação: ");
			
			IO.println("\nOperação iniciada...");
			
			switch (selecao)
			{
				case 0:
					IO.println("Até breve :)");
					break;
					
				case 1:
					user = login();
					IO.println("\nLogin " + ( user == null ? "falhou" : "bem sucedido" ) + ".");
					IO.pause();
					break;
					
				case 2:
					user = register();
					IO.println("\nRegistro " + ( user == null ? "falhou" : "bem sucedido" ) + ".");
					IO.pause();
					break;
					
				default:
					IO.println("Operação inválida\n");
					break;
			}
			
			IO.println("\nOperação finalizada.\n");
			IO.println("--------------------------------------------\n");
			
		} while (selecao != 0 && selecao != 1);
		
		return user;
	}
}
