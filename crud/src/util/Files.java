package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class Files
{
	/**
	 * Abre o arquivo {@code fileName} no modo de acesso {@code mode}
	 * 
	 * @param fileName Nome do arquivo a ser aberto.
	 * @param mode Modo de acesso ("r", "w", "rw", "rws", "rwd").
	 * 
	 * @return {@code null} se alguma coisa falhar. Caso contrário,
	 * o {@link java.io.RandomAccessFile} correspendente com o
	 * arquivo aberto.
	 * 
	 * @see java.io.RandomAccessFile#RandomAccessFile(java.io.File, String)
	 */
	
	public static RandomAccessFile openFile(String fileName, String mode)
	{
		RandomAccessFile file = null;
		
		try
		{
			file = new RandomAccessFile(fileName, mode);
		}
		
		catch (FileNotFoundException fnfe)
		{
			fnfe.printStackTrace();
		}
		
		return file;
	}

	public static boolean deleteFile(String fileName)
	{
		return new File(fileName).delete();
	}

	public static boolean deleteSuffixedFile(String fileName, String suffix)
	{
		return deleteFile(fileName + suffix);
	}

}
