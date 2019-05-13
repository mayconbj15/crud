package util;

import java.io.*;

/**
* @author Axell Brendow ( https://github.com/axell-brendow )
*/

public class IO
{
	public static final String REGEX_DIGIT = "[0-9]";
	public static final String REGEX_DIGITS = REGEX_DIGIT + "+";
	public static final String REGEX_INT = "[+|-]?" + REGEX_DIGITS;
	public static final String REGEX_INT_POSITIVE = "[+]?" + REGEX_DIGITS;
	public static final String REGEX_FLOAT = REGEX_INT + "(\\." + REGEX_DIGITS + ")?";
	public static final String REGEX_FLOAT_POSITIVE = REGEX_INT_POSITIVE + "(\\." + REGEX_DIGITS + ")?";

	public static final String REGEX_LOWER_CASE = "[a-z]";
	public static final String REGEX_UPPER_CASE = "[A-Z]";
	public static final String REGEX_LETTER = "[" + REGEX_LOWER_CASE + REGEX_UPPER_CASE + "]";
	public static final String REGEX_ALFANUMERIC = "[" + REGEX_LETTER + REGEX_DIGIT + "]";
	public static final String REGEX_SPECIAL_SYMBOLS = "[.!#$%&'*+/=?^_`{|}~-]";
	public static final String REGEX_ALFANUMERIC_AND_SPECIAL_SYMBOLS = "[" + REGEX_ALFANUMERIC + REGEX_SPECIAL_SYMBOLS + "]";
	public static final String REGEX_EMAIL_BEFORE_AT_SIGN = REGEX_ALFANUMERIC + "(" + REGEX_ALFANUMERIC_AND_SPECIAL_SYMBOLS + "*" + REGEX_ALFANUMERIC + ")*";
	public static final String REGEX_EMAIL_AFTER_AT_SIGN = REGEX_ALFANUMERIC + "(" + "[" + REGEX_ALFANUMERIC + ".:-]*" + REGEX_ALFANUMERIC + ")*";
	
	/**
	 * Ideia extraída de
	 * <a href="https://pt.stackoverflow.com/questions/1386/express%C3%A3o-regular-para-valida%C3%A7%C3%A3o-de-e-mail">
	 * Regex Email
	 * </a>
	 */
	
	public static final String REGEX_EMAIL = "^" + REGEX_EMAIL_BEFORE_AT_SIGN + "@" + REGEX_EMAIL_AFTER_AT_SIGN + "$";
	
	private static BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );

	public static void print(Object msg)
	{
		System.out.print(msg);
	}

	public static void println(Object msg)
	{
		print(msg + System.lineSeparator());
	}
	
	public static String readLine()
	{
		String line = "";

		try
		{
			line = reader.readLine();
		}

		catch (IOException ex)
		{
			println("Nao foi possivel ler da entrada padrao");
		}

		return line;
	}

	public static String readLine(String msg)
	{
		print(msg);

		return readLine();
	}
	
	public static String readLineUntilItMatch(String msg, String regex)
	{
		String line = readLine(msg);
		
		while (!line.matches(regex))
		{
			line = readLine(msg);
		}
		
		return line;
	}
	
	public static int readLineUntilPositiveInt(String msg)
	{
		return Integer.parseInt(readLineUntilItMatch(msg, REGEX_INT_POSITIVE));
	}
	
	public static float readLineUntilPositiveFloat(String msg)
	{
		return Float.parseFloat(readLineUntilItMatch(msg, REGEX_FLOAT_POSITIVE));
	}
	
	public static String readLineUntilEmail(String msg)
	{
		return readLineUntilItMatch(msg, REGEX_EMAIL);
	}
	
	public static short readshort()
	{
		short value = -1;
		
		try
		{
			value = Short.parseShort( readLine() );
		}
		
		catch (NumberFormatException nfex)
		{
			nfex.printStackTrace();
		}
		
		return value;
	}
	
	public static short readshort(String msg)
	{
		print(msg);
		
		return readshort();
	}
	
	public static int readint()
	{
		int value = -1;
		
		try
		{
			value = Integer.parseInt( readLine() );
		}
		
		catch (NumberFormatException nfex)
		{
			nfex.printStackTrace();
		}
		
		return value;
	}
	
	public static int readint(String msg)
	{
		print(msg);
		
		return readint();
	}
	
	public static float readfloat()
	{
		float value = -1;
		
		try
		{
			value = Float.parseFloat( readLine() );
		}
		
		catch (NumberFormatException nfex)
		{
			nfex.printStackTrace();
		}
		
		return value;
	}
	
	public static float readfloat(String msg)
	{
		print(msg);
		
		return readfloat();
	}/*
	
	public static String readSenha()
	{
		return System.console().readPassword().toString();
	}
	
	//colocar um regex para senha aqui
	public static String readSenha(String msg) {
		print(msg);
		
		return readSenha();
	}*/
	
	//colocar um regex para senha aqui
	public static String readSenha(String msg) {
		return readLine(msg);
	}
	
	public static void pause() 
	{
		readLine("Pressione ENTER para continuar...");
	}
}
