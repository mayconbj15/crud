package user;

import util.*;

import java.util.function.Function;
import java.util.function.Supplier;

import gerenciadores.GerenciadorComprasCliente;
import gerenciadores.GerenciadorFuncionario;

/**
 * Classe que gerencia a interação com o usuário.
 */

public class Crud {

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
	
	public static <T> T menu(String nome, String mensagem, String[] opcoes, Function<Integer, T> gerenciarOpcoes)
	{
		T result = null;
		int opcao = 0;
		
		do
		{
			gerarCabecalhoEOpcoes(nome, mensagem, opcoes);
			opcao = IO.readint("Opção: ");
			
			IO.println("");
			
			if (opcao == 0)
			{
				IO.println("Até breve :D");
			}
			
			else if (opcao < 0 || opcao > opcoes.length)
			{
				IO.println("Opção inválida");
			}
			
			else
			{
				result = gerenciarOpcoes.apply(opcao);
			}
			
			IO.println("\n--------------------------------------------\n");
	
		} while(opcao != 0);
		
		return result;
	}

	public static void menu(String nome, String mensagem, String[] opcoes, Runnable[] acoes)
	{
		menu(nome, mensagem, opcoes,
			(opcao) ->
			{
				acoes[opcao - 1].run();
				return null;
			}
		);
	}
	
	public static <T> T menu(String nome, String mensagem, String[] opcoes, Supplier<T>[] acoes)
	{
		return menu(nome, mensagem, opcoes,
			(opcao) ->
			{
				return acoes[opcao - 1].get();
			}
		);
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
				() -> new GerenciadorComprasCliente().menu(),
				() -> new GerenciadorFuncionario().menu()
			}
		);

	}//end menu()
	
}//end class Main
