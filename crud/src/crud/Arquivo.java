package crud;

import java.io.*;
import java.util.ArrayList;
import java.util.function.Function;

import crud.hash_dinamica.implementacoes.HashDinamicaIntLong;
import entidades.Entidade;
import serializaveis.SerializavelAbstract;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Classe para gerenciamento de registros de tipos genéricos numa base de dados
 * 
 * @param T Tipo da entidade.
 */

public class Arquivo<T extends SerializavelAbstract & Entidade> {
	// deslocamento em relação ao início do arquivo que
	// deve ser dado para pular o metadados/cabeçalho
	// da base de dados
	private static final int HEADER_OFFSET = Integer.BYTES; // tamanho do int
	
	private String name;
	private int lastID;
	RandomAccessFile accessFile;
	private Constructor<T> constructor;
	
	public HashDinamicaIntLong indice;
	//private final int TREE_ORDER = 21;
	//private String indexFileName;
	
	/**
	 * Cria um objeto que gerencia uma base de dados de objetos
	 * do tipo {@code T}.
	 *  
	 * @param constructor Construtor do tipo {@code T}.
	 * @param databaseFileName Nome do arquivo da base de dados.
	 * @param indexesDirFileName Nome do arquivo do diretório da
	 * hash dinâmica.
	 * @param indexesFileName Nome do arquivo dos buckets da hash
	 * dinâmica.
	 */
	
	public Arquivo(
		Constructor<T> constructor,
		String databaseFileName,
		String indexesDirFileName,
		String indexesFileName)
	{
		this.name = databaseFileName;
		this.lastID = -1;
		this.constructor = constructor;
		
		try
		{
			this.indice = new HashDinamicaIntLong(indexesDirFileName, indexesFileName);
		}
		
		catch (NoSuchMethodException | SecurityException e)
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
	 * <p>
	 * <b>IMPORTANTE</b>: esta função não lê o último ID usado do
	 * cabeçalho da base de dados nem escreve o id recebido no
	 * cabeçalho da base de dados. Caso for necessário, gerencie
	 * isso por conta própria.
	 * </p>
	 * 
	 * @param entity Entidade a ser inserida.
	 * @param id Id da entidade.
	 * 
	 * @return {@code true} se tudo der certo;
	 * {@code false} caso contrário.
	 */
	
	private boolean writeObject(T entity, int id) {
		boolean success = false;
		
		if (entity != null)
		{
			try
			{
				accessFile = openFile();

				entity.setId(id);

				byte[] byteArray = entity.obterBytes();

				// go to final of file
				accessFile.seek(accessFile.length());

				// insere a chave (id) e o dado correspondente (endereço do registro)
				// no sistema de indexamento
				indice.inserir(entity.getId(), accessFile.getFilePointer());

				accessFile.writeByte(' '); // insere a lapide
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
		}
		
		return success;
	}
	
	/**
	 * Gera um novo id com base no último id usado na base de dados.
	 * Além disso, já escreve esse novo id no cabeçalho.
	 * 
	 * @return o novo id.
	 */
	
	public int createNewId()
	{
		return writeLastID( readLastID() + 1 );
	}
	
	/**
	 * Insere uma entidade na base de dados. O processo é dividido em três
	 * etapas:
	 * 
	 * <p>
	 * 1 - Este método gera um novo id que será o futuro id da entidade.<br>
	 * 2 - A função {@code apply} da interface {@code createEntity} é chamada
	 * recebendo como parâmetro esse novo id e ela deve retornar a entidade já
	 * pronta para ser inserida na base de dados.<br>
	 * 3 - A entidade é inserida na base de dados.
	 * </p>
	 * 
	 * @param createEntity Função que criará a entidade tendo acesso previo ao
	 * id dela.
	 * 
	 * @return o id da entidade se tudo der certo; caso contrário -1.
	 */
	
	public int writeObject(Function<Integer, T> createEntity)
	{
		int newId = createNewId();

		T entity = createEntity.apply(newId);

		return ( writeObject(entity, newId) ? newId : -1 );
	}
	
	/**
	 * Insere uma entidade na base de dados.
	 * 
	 * @param entity Entidade a ser inserida.
	 * 
	 * @return o id da entidade se tudo der certo;
	 * caso contrário -1.
	 */
	
	public int writeObject(T entity) {
		
		return writeObject
		(
			(entityId) ->
			{
				return entity;
			}
		);
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
		T entity = null;
		
		try
		{
			char lapide = (char) file.readByte();
			int entitySize = file.readInt();
			
			byte[] byteArray = new byte[entitySize];
			
			file.read(byteArray);
			
			if (lapide != '*')
			{
				try
				{
					entity = constructor.newInstance();
					entity.lerBytes(byteArray);
				}
				
				catch (InstantiationException |
					IllegalAccessException |
					IllegalArgumentException |
					InvocationTargetException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return entity;
	}

	/**
	 * Lê a entidade da base de dados que tiver o id informado.
	 * 
	 * @param id Id da entidade a ser procurada.
	 * 
	 * @return {@code null} se a entidade não for encontrada;
	 * a própria entidade caso contrário.
	 */
	
	public T readObject(int id) {
		T entity = null;
		
		try {
			long entityAddress = indice.pesquisarDadoPelaChave(id);
			
			if (entityAddress != Long.MIN_VALUE)
			{
				accessFile = openFile();
				accessFile.seek(entityAddress);
				
				entity = readObject(accessFile);
				
				accessFile.close();
			}
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}

		return entity;
	}
	
	/**
	 * Coleta todas as entidades ativas da base de dados.
	 * 
	 * @return Lista com todas as entidades ativas.
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
	 * @return a entidade excluída se tudo der certo;
	 * {@code null} caso contrário.
	 */
	
	public T deleteObject(int id) {

		T deletedObject = null;
		
		try
		{
			long entityAddress = indice.pesquisarDadoPelaChave(id);
			
			if (entityAddress != -1)
			{
				RandomAccessFile file = openFile();
				
				file.seek(entityAddress);
				file.writeByte('*');
				
				// pula o número que diz o tamanho da entidade
				file.skipBytes(Integer.BYTES);
				
				deletedObject = constructor.newInstance();
				deletedObject.lerObjeto(file);
				
				indice.excluir(id);
				
				file.close();
			}
		}
		
		catch (InstantiationException |
				IllegalAccessException |
				IllegalArgumentException |
				InvocationTargetException |
				IOException e)
		{
			e.printStackTrace();
		}
		
		return deletedObject;
	}
	
	/**
	 * Método que recebe um array de int com os ids das entidades para serem excluídas
	 * 
	 * @param list - array com ids para serem excluídos
	 * 
	 * @return a última entidade excluída se tudo der certo;
	 * {@code null} caso contrário.
	 */
	
	public T deleteObjects(int[] list){
		T lastDeletedEntity = null;
		
		for(int i=0; i<list.length && lastDeletedEntity != null; i++){
			lastDeletedEntity = deleteObject(list[i]);
		}
		
		return lastDeletedEntity;
	}

	/**
	 * Substitui a entidade com o id informado pela nova
	 * entidade recebida.
	 * 
	 * @param id Id da entidade a ser alterada.
	 * @param entity Nova entidade a ser posta no lugar.
	 * 
	 * @return se tudo der certo, a entidade antes de ser
	 * alterada; {@code null} caso contrário.
	 */
	
	public T changeObject(int id, T entity) {
		
		T deletedEntity = deleteObject(id);
		
		if (!writeObject(entity, id))
		{
			deletedEntity = null;
		}
		
		return deletedEntity;
	}
	
	/**
	 * Fecha todos os arquivos que este objeto estiver gerenciando.
	 * 
	 * @return {@code true} se tudo der certo. Caso contrário, {@code false}.
	 */
	
	public boolean close()
	{
		boolean success = false;
		
		if (accessFile != null && indice != null)
		{
			try
			{
				accessFile.close();
				success = indice.fechar();
			}
			
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return success;
	}
	
	// estrutura da base de dados
	// [ ultimo_id_usado (int), registros... ]
	//
	// estrutura dos registros
	// [ lápide (byte), tamanho_da_entidade (int), entidade ]
}