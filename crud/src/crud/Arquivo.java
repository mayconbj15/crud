package crud;

import java.io.*;

import java.util.ArrayList;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Classe para gerenciamento de registros de tipos genéricos na base de dados
 */

public class Arquivo<T extends Entidade> {
	private String name;
	private short lastID;
	RandomAccessFile accessFile;
	private Constructor<? extends Entidade> constructor;
	
	Indice indice;
	final int treeOrder = 21;
	final String indexFileName = "indexes";

	public Arquivo(Constructor<? extends Entidade> constructor, String nameFile) {
		this.name = nameFile;
		this.lastID = -1;
		this.constructor = constructor;
		
		try
		{
			this.indice = new Indice(treeOrder, indexFileName);
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Arquivo(String nameFile) {
		this.name = nameFile;
		this.lastID = -1;
		
		try
		{
			this.indice = new Indice(treeOrder, indexFileName);
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/*
	public short getLastID() {
		return this.lastID;
	}*/

	public short getLastID() {
		return readLastID();
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
	* Obs.: pressupõe-se que os dois primeiros bytes da base de dados
	* são o short que guarda o último ID usado.
	*/
	
	private short readLastID()
	{
		RandomAccessFile file = openFile();
		short lastID = -1;
		
		try
		{
			lastID = file.readShort();
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
	
	public boolean idIsValid(short id)
	{
		return id > -1 && id <= readLastID();
	}

	/**
	* Escreve {@code lastID} no cabecalho da base de dados.
	* 
	* @param lastID Novo valor para o último ID.
	* 
	* @return {@code lastID}.
	*/
	
	private short writeLastID(short lastID)
	{
		RandomAccessFile file = openFile();
		
		try
		{
			file.writeShort(lastID);
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
	 * Insere uma entidade na base de dados.
	 * 
	 * <p>
	 * <b>Importante</b>: esta função não lê o último ID usado do
	 * cabeçalho da base de dados nem escreve o id recebido no
	 * cabeçalho da base de dados, caso for necessário, gerencie
	 * isso por conta própria.
	 * </p> 
	 * 
	 * @param produto Entidade a ser inserida.
	 * @param id Id da entidade.
	 * 
	 * @return {@code false} se alguma coisa falhar na inserção.
	 * Caso contrário, retorna {@code true}.
	 */
	
	private boolean writeObject(T item, int id) {
		boolean success = false;
		
		try {
			accessFile = openFile();
			
			byte[] byteArray = item.setByteArray();
			
			// go to final of file
			accessFile.seek(accessFile.length());
			
			// insere a chave (id) e o dado correspondente (endereço do registro)
			// no sistema de indexamento
			indice.inserir(id, accessFile.getFilePointer());
			
			accessFile.writeChar(' '); // insere a lapide
			accessFile.writeShort(id); // insere o id
			accessFile.writeInt(byteArray.length); // insere o tamanho da entidade
			accessFile.write(byteArray); // insere a entidade
			
			accessFile.close();
			
			success = true;
		} 
		catch (FileNotFoundException fNFE) {
			fNFE.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return success;
	}
	
	/**
	 * Insere uma entidade na base de dados.
	 * 
	 * @param produto Entidade a ser inserida.
	 * 
	 * @return {@code false} se alguma coisa falhar na inserção.
	 * Caso contrário, retorna {@code true}.
	 */
	
	public boolean writeObject(T item) {
		int newId = readLastID() + 1;

		writeLastID((short) newId);
		
		return writeObject(item, newId);
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
			short id = file.readShort();
			int entitySize = file.readInt();
			
			byte[] byteArray = new byte[entitySize];
			
			file.readFully(byteArray, 0, entitySize);
			
			if (lapide != '*')
			{
				try {
					//item = (T)item.getClass().newInstance();
					item = (T)constructor.newInstance();
				}catch(IllegalAccessException iae) {
					iae.printStackTrace();
				}catch(InstantiationException ie) {
					ie.printStackTrace();
				}catch(InvocationTargetException ite) {
					ite.printStackTrace();
				}
				
				item.fromByteArray(byteArray, id);
			}
		}
		
		catch (IOException e)
		{
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
		ArrayList<T> listProdutos = new ArrayList<T>();
		T produtoAux = null;
		
		try
		{
			accessFile = openFile();
			accessFile.seek(2);
			
			while (accessFile.getFilePointer() < accessFile.length()) {
				produtoAux = readObject(accessFile);
				
				if (produtoAux != null)
				{
					listProdutos.add(produtoAux);
				}
			}
			
			accessFile.close();
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return listProdutos;
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
		
		boolean success = deleteObject(id);
		
		if (success)
		{
			success = writeObject(item2, id);
		}
		
		return success;
	}
	
	// estrutura da base de dados
	// [ ultimo_id_usado (short), registros... ]
	//
	// estrutura dos registros
	// [ lápide (char), id (short), tamanho_da_entidade (int), entidade (Produto) ]
}