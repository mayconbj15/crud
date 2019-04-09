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

	/**
	 * Deleta um arquivo. Considerando que o tamanho de {@code prefixesAnd1Suffix} seja <b>n</b>,
	 * o nome do arquivo é formado prefixando-se <b>n - 1</b> strings ao nome do arquivo e,
	 * posteriomente, sufixando-se a última string a ele.
	 *  
	 * @param fileName Nome do arquivo.
	 * @param prefixesAnd1Suffix Lista de strings onde a última é um sufixo e as anteriores são
	 * sufixos.
	 * 
	 * @return {@code true} se o arquivo for deletado com sucesso. Caso contrário, {@code false}.
	 */
	
	public static boolean deletePrefixedAndSuffixedFile(String fileName, String... prefixesAnd1Suffix)
	{
		int numberOfPrefixes = ( prefixesAnd1Suffix == null ? 0 : prefixesAnd1Suffix.length ) - 1;
		String name = "";
		
		for (int i = 0; i < numberOfPrefixes; i++)
		{
			name += prefixesAnd1Suffix[i];
		}
		
		name += fileName;
		
		return numberOfPrefixes > -1 ?
				deleteSuffixedFile(name, prefixesAnd1Suffix[numberOfPrefixes]) :
				false;
	}
	
	/**
	 * Se {@code file} for um único arquivo, deleta-o. Caso seja um diretório,
	 * deleta-o recursivamente.
	 * 
	 * @param file Arquivo ou diretório a ser deletado.
	 */
	
	public static void delete(File file)
	{
		if (file != null)
		{
			if (file.isDirectory())
			{
				for (File child : file.listFiles())
				{
					delete(child);
				}
			}
			
			else
			{
				file.delete();
			}
		}
	}
	
	/**
	 * Se {@code file} for um único arquivo, deleta-o. Caso seja um diretório,
	 * deleta-o recursivamente.
	 * 
	 * @param file Arquivo ou diretório a ser deletado.
	 */
	
	public static void delete(String file)
	{
		delete(new File(file));
	}
	
	public static void createFolders(String... paths)
	{
		for (String path : paths)
		{
			if (path != null)
			{
				new File(path).mkdirs();
			}
		}
	}
}
