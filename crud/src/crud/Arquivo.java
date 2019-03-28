package crud;

import java.io.*;

import java.util.ArrayList;

import entidades.Entidade;
import util.IO;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Classe para gerenciamento de registros de tipos genéricos na base de dados
 */

public class Arquivo<T extends Entidade> {
	// deslocamento em relação ao início do arquivo que
	// deve ser dado para pular o metadados/cabeçalho
	// da base de dados
	private static final int HEADER_OFFSET = Integer.BYTES; // tamanho do int
	
	private String name;
	private int lastID;
	RandomAccessFile accessFile;
	private Constructor<T> constructor;
	
	private Indice indice;
	private final int TREE_ORDER = 21;
	private String indexFileName;
	
	/**
	 * Cria um objeto que gerencia uma base de dados de objetos
	 * do tipo {@code T}.
	 *  
	 * @param constructor Construtor do tipo {@code T}.
	 * @param nameFile Nome do arquivo da base de dados.
	 */
	
	public Arquivo(Constructor<T> constructor, String databaseFileName, String indexesFileName) {
		this.name = databaseFileName;
		this.lastID = -1;
		this.constructor = constructor;
		// cada entidade tem que ter um arquivo de indice com nome personalizado
		this.indexFileName = indexesFileName;
		
		try
		{
			this.indice = new Indice(TREE_ORDER, indexFileName);
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	* Abre o arquivo da base de dados.
	* 
	* @return O arquivo da base de dados.
	*/
	
	private RandomAccessFile openFile()
	{
		RandomAccessFile file = null;
		
		try
		{
			file = new RandomAccessFile(name, "rw");
			file.seek(0);
		}
		
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return file;
	}
	
	/**
	* Lê o último ID usado no cabeçalho da base de dados.
	* 
	* Obs.: pressupõe-se que os quatro primeiros bytes da base de dados
	* são o int que guarda o último ID usado.
	*/
	
	private int readLastID()
	{
		RandomAccessFile file = openFile();
		int lastID = -1;
		
		try
		{
			lastID = file.readInt();
		}
		
		catch (IOException e)
		{
			//e.printStackTrace();
		}
		
		try
		{
			file.close();
		}
		
		catch (IOException ioex)
		{
			ioex.printStackTrace();
		}
		
		return ( lastID == -1 ? this.lastID : (this.lastID = lastID) );
	}

	/**
	* Escreve {@code lastID} no cabecalho da base de dados.
	* 
	* @param lastID Novo valor para o último ID.
	* 
	* @return {@code lastID}.
	*/
	
	private int writeLastID(int lastID)
	{
		RandomAccessFile file = openFile();
		
		try
		{
			file.writeInt(lastID);
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			file.close();
		}
		
		catch (IOException ioex)
		{
			ioex.printStackTrace();
		}
		
		return lastID;
	}
	
	/**
	 * Checa se é possível que o id recebido exista na base de dados.
	 * 
	 * @param id Id a ser analisado.
	 * 
	 * @return {@code true} se for possível que o id recebido exista
	 * na base de dados. Caso contrário, {@code false}.
	 */
	
	public boolean idIsValid(int id)
	{
		return id > -1 && id <= readLastID();
	}
	
	/**
	 * Insere uma entidade na base de dados.
	 * 
	 * @param item Entidade a ser inserida.
	 * 
	 * @return {@code false} se alguma coisa falhar na inserção.
	 * Caso contrário, retorna {@code true}.
	 */
	
	public boolean writeObject(T item) {
		boolean success = false;
		
		try {
			accessFile = openFile();
			
			item.setId( readLastID() + 1 );
			
			writeLastID(item.getId());
			
			byte[] byteArray = item.setByteArray();
			
			// go to final of file
			accessFile.seek(accessFile.length());
			
			// insere a chave (id) e o dado correspondente (endereço do registro)
			// no sistema de indexamento
			indice.inserir(item.getId(), accessFile.getFilePointer());
			
			accessFile.writeChar(' '); // insere a lapide
			accessFile.writeInt(byteArray.length); // insere o tamanho da entidade
			accessFile.write(byteArray); // insere a entidade
			
			accessFile.close();
			
			success = true;
		} 
		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return success;
	}
	
	/**
	* Lê um registro a partir de onde o ponteiro de {@code file} estiver e
	* retorna a entidade que o registro representa. Caso o registro esteja
	* desativado (lápide com '*'), o retorno é {@code null}.
	* 
	* Obs.: deixar o ponteiro em cima da lápide do registro
	*  
	* @param file Instância de {@link crud.Arquivo} voltada
	* para o arquivo {@link user.Main#DATABASE_FILE_NAME}.
	* 
	* @return {@code null} se o registro estiver desativado, lápide com '*'.
	* Caso contrário, retorna a entidade do registro.
	*/
	
	private T readObject(RandomAccessFile file)
	{
		T item = null;
		
		try
		{
			char lapide = file.readChar();
			int entitySize = file.readInt();
			
			byte[] byteArray = new byte[entitySize];
			
			file.read(byteArray);
			
			if (lapide != '*')
			{
				try {
					//item = (T)item.getClass().newInstance();
					item = constructor.newInstance();
					item.fromByteArray(byteArray);
				}
				catch(IllegalAccessException iae) {
					iae.printStackTrace();
				}
				catch(InstantiationException ie) {
					ie.printStackTrace();
				}
				catch(InvocationTargetException ite) {
					ite.printStackTrace();
				}
			}
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return item;
	}

	/**
	* Lê a entidade da base de dados que tiver o id informado.
	* 
	* @param id Id da entidade a ser procurada.
	* 
	* @return {@code null} se a entidade não for encontrada. Caso
	* contrário, a entidade.
	*/
	
	public T readObject(int id) {
		T item = null;
		
		try {
			long entityAddress = indice.buscar(id);
			
			if (entityAddress != -1)
			{
				accessFile = openFile();
				accessFile.seek(entityAddress);
				
				item = readObject(accessFile);
				
				accessFile.close();
			}
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}

		return item;
	}
	
	/**
	* Coleta todas as entidades ativas da base de dados.
	* 
	* @return Lista com todas as entidades.
	*/
	
	public ArrayList<T> list() {
		ArrayList<T> entitiesList = new ArrayList<T>();
		T entityAux = null;
		
		try
		{
			accessFile = openFile();
			accessFile.seek(HEADER_OFFSET);
			
			while (accessFile.getFilePointer() < accessFile.length()) {
				entityAux = readObject(accessFile);
				
				if (entityAux != null)
				{
					entitiesList.add(entityAux);
				}
			}
			
			accessFile.close();
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return entitiesList;
	}
	
	/**
	* Encontra a entidade com id informado e a deleta.
	* 
	* @param id Id da entidade a ser procurada.
	* 
	* @return {@code true} se a exclusão for bem sucedida.
	* Caso contrário, retorna {@code false}.
	*/
	
	public boolean deleteObject(int id) {

		boolean success = false;
		
		try
		{
			long entityAddress = indice.buscar(id);
			
			if (entityAddress != -1)
			{
				RandomAccessFile file = openFile();
				
				file.seek(entityAddress);
				file.writeChar('*');
				
				success = indice.excluir(id);
				
				file.close();
			}
		}
		
		catch (IOException ioex)
		{
			ioex.printStackTrace();
		}
		
		return success;
	}

	/**
	 * Altera a entidade com o id informado para a nova entidade {@code produto2}.
	 * 
	 * @param id Id da entidade a ser alterada.
	 * @param produto2 Nova entidade a ser posta no lugar.
	 * 
	 * @return {@code true} se a alteração for bem sucedida.
	 * Caso contrário, retorna {@code false}.
	 */
	
	public boolean changeObject(int id, T item2) {
		
		boolean success = false;
		
		if (deleteObject(id))
		{
			item2.setId(id);
			success = writeObject(item2);
		}
		
		return success;
	}
	
	// estrutura da base de dados
	// [ ultimo_id_usado (int), registros... ]
	//
	// estrutura dos registros
	// [ lápide (char), tamanho_da_entidade (int), entidade ]
}