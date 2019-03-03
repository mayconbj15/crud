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
}
