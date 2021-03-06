package user;

import util.*;

import java.util.function.Function;
import java.util.function.Supplier;

import gerenciadores.GerenciadorComprasCliente;
import gerenciadores.GerenciadorFuncionario;

/**
 * Classe que gerencia a interação com o usuário.
 */

public class Crud 
{
	public static GerenciadorComprasCliente gerenciadorCliente;
	public static GerenciadorFuncionario gerenciadorFuncionario; 
	
	public static void gerarCabecalhoEOpcoes(String nome, String mensagem, String[] opcoes)
	{
		IO.println("[Menu " + nome + "]");
		IO.println(mensagem);
		IO.println("Digite:");
		for (int i = 0; i < opcoes.length; i++)
		{
			IO.println((i + 1) + " para " + opcoes[i]);
		}
		IO.println("0 para sair");
		IO.println("");
	}
	
	public static <T> T menu(String nome, String mensagem, String[] opcoes, Function<Integer, T> gerenciarOpcoes, int[] opcoesDeSaida, boolean inverterLogicaDeSaida)
	{
		T result = null;
		int opcao = 0;
		
		do
		{
			gerarCabecalhoEOpcoes(nome, mensagem, opcoes);
			opcao = IO.readint("Opção: ");
			
			IO.println("");
			
			if (opcao < 0 || opcao > opcoes.length)
			{
				IO.println("Opção inválida");
			}
			
			else if (opcao != 0)
			{
				result = gerenciarOpcoes.apply(opcao);
			}
			
			IO.println("\n--------------------------------------------\n");
	
		} while (MyArray.contains(opcao, opcoesDeSaida) == inverterLogicaDeSaida);
		
		return result;
	}
	
	public static <T> T menu(String nome, String mensagem, String[] opcoes, Function<Integer, T> gerenciarOpcoes, int[] opcoesDeSaida)
	{
		return menu(nome, mensagem, opcoes, gerenciarOpcoes, opcoesDeSaida, false);
	}
	
	public static <T> T menu(String nome, String mensagem, String[] opcoes, Function<Integer, T> gerenciarOpcoes)
	{
		return menu(nome, mensagem, opcoes, gerenciarOpcoes, new int[] { 0 });
	}

	public static void menu(String nome, String mensagem, String[] opcoes, Runnable[] acoes, int[] opcoesDeSaida, boolean inverterLogicaDeSaida)
	{
		menu(nome, mensagem, opcoes,
			(opcao) ->
			{
				acoes[opcao - 1].run();
				return null;
			},
			opcoesDeSaida,
			inverterLogicaDeSaida
		);
	}

	public static void noBackMenu(String nome, String mensagem, String[] opcoes, Runnable[] acoes)
	{
		menu(nome, mensagem, opcoes, acoes, new int[] {  }, true);
	}

	public static void menu(String nome, String mensagem, String[] opcoes, Runnable[] acoes, int[] opcoesDeSaida)
	{
		menu(nome, mensagem, opcoes, acoes, opcoesDeSaida, false);
	}

	public static void menu(String nome, String mensagem, String[] opcoes, Runnable[] acoes)
	{
		menu(nome, mensagem, opcoes, acoes, new int[] { 0 });
	}
	
	public static <T> T menu(String nome, String mensagem, String[] opcoes, Supplier<T>[] acoes, int[] opcoesDeSaida, boolean inverterLogicaDeSaida)
	{
		return menu(nome, mensagem, opcoes,
			(opcao) ->
			{
				return acoes[opcao - 1].get();
			},
			opcoesDeSaida,
			inverterLogicaDeSaida
		);
	}
	
	public static <T> T noBackMenu(String nome, String mensagem, String[] opcoes, Supplier<T>[] acoes, int[] opcoesDeSaida)
	{
		return menu(nome, mensagem, opcoes, acoes, new int[] {  }, true);
	}
	
	public static <T> T menu(String nome, String mensagem, String[] opcoes, Supplier<T>[] acoes, int[] opcoesDeSaida)
	{
		return menu(nome, mensagem, opcoes, acoes, opcoesDeSaida, false);
	}
	
	public static <T> T menu(String nome, String mensagem, String[] opcoes, Supplier<T>[] acoes)
	{
		return menu(nome, mensagem, opcoes, acoes, new int[] { 0 });
	}
	
	/**
	 * Gerencia a interação com o usuário.
	 */
	
	public static void menu()
	{
		IO.println("Olá, meu nobre!\n");
		
		menu
		(
			"Principal",
			"Como deseja fazer login ?",
			new String[] { "cliente", "funcionario" },
			new Runnable[]
			{
				() ->
				{
					gerenciadorCliente = new GerenciadorComprasCliente();
					gerenciadorCliente.menu();
				},
				() ->
				{
					gerenciadorFuncionario = new GerenciadorFuncionario();
					gerenciadorFuncionario.menu();
				}
			}
		);

		IO.println("Até breve :D");
	}//end menu()
	
}//end class Main
