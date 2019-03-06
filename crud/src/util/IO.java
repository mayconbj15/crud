package util;

import java.io.*;

/**
* @author Axell Brendow ( https://github.com/axell-brendow )
*/

public class IO
{
	static BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );

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
	}
	
	public static void pause() 
	{
		readLine("Pressione ENTER para continuar...");
	}
}
