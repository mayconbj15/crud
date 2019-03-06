package crud;

import java.io.*;

import java.util.ArrayList;

//colocar no array list e fazer o index para buscar por nome 

public class Arquivo {
	private String name;
	private short lastID;
	RandomAccessFile accessFile;
	
	Indice indice;
	final int treeOrder = 21;
	final String indexFileName = "indexes";

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

	private short setLastID(short lastID) {
		return this.lastID = lastID;
	}
	
	/**
	* Abre o arquivo da base de dados.
	* 
	* @return o arquivo da base de dados.
	*/
	
	protected RandomAccessFile openFile()
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
			file.close();
		}
		
		catch (IOException e)
		{
			//e.printStackTrace();
		}
		
		return ( lastID == -1 ? this.lastID : lastID );
	}
	
	public boolean idIsValid(short id)
	{
		return id > -1 && id <= readLastID();
	}

	/**
	* Escreve {@code lastID} no cabecalho da base de dados.
	* 
	* @param lastID novo valor para o último ID
	* 
	* @return {@code lastID}
	*/
	
	private short writeLastID(short lastID)
	{
		RandomAccessFile file = openFile();
		
		try
		{
			file.writeShort(lastID);
			file.close();
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return lastID;
	}
	
	public boolean writeObject(Produto produto) {
		boolean success = false;
		
		try {
			accessFile = openFile();
			
			produto.setId( (short) (readLastID() + 1) );
			setLastID( writeLastID( produto.getId() ) );
			
			byte[] byteArray = produto.setByteArray();
			
			// go to final of file
			accessFile.seek(accessFile.length());
			indice.inserir(produto.getId(), accessFile.getFilePointer());
			
			accessFile.writeChar(' '); //lapide
			accessFile.writeShort(produto.getId()); // write id
			accessFile.writeInt(byteArray.length);
			accessFile.write(byteArray);
			
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
	* Percorre toda a base de dados procurando por uma entidade
	* específica que tenha o id {@code id}.
	* 
	* @param id id da entidade a ser procurada
	* 
	* @return {@code null} se a entidade não for encontrada. Caso
	* contrário, a entidade.
	*/
	
	public Produto readObject(int id) {
		Produto produto = null;
		
		try {
			long entityAddress = indice.buscar(id);
			
			if (entityAddress != -1)
			{
				accessFile = openFile();
				accessFile.seek(entityAddress);
				
				produto = readObject(accessFile);
			}
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}

		return produto;
	}
	
	/**
	* Lê um registro a partir de onde o ponteiro de {@code file} estiver e
	* retorna a entidade que o registro representa. Caso o registro esteja
	* desativado (lápide com '*'), o retorno é {@code null}.
	* 
	* Obs.: deixar o ponteiro em cima da lápide do registro
	*  
	* @param file arquivo já aberto
	* 
	* @return a entidade que o registro representa. Caso o registro esteja
	* desativado, lápide com '*', o retorno é {@code null}.
	*/
	
	public Produto readObject(RandomAccessFile file)
	{
		Produto produto = null;
		
		try
		{
			char lapide = file.readChar();
			short id = file.readShort();
			int entitySize = file.readInt();
			
			byte[] byteArray = new byte[entitySize];
			
			file.readFully(byteArray, 0, entitySize);
			
			if (lapide != '*')
			{
				produto = new Produto();
				produto.fromByteArray(byteArray, id);
			}
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return produto;
	}
	
	/**
	* Percorre toda a base de dados coletando as entidades.
	* 
	* @return lista com todas as entidades.
	*/
	
	public ArrayList<Produto> list() {
		ArrayList<Produto> listProdutos = new ArrayList<Produto>();
		Produto produtoAux = null;
		
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
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return listProdutos;
	}
	
	/**
	* Encontra a entidade com id {@code id} e a deleta.
	* 
	* @param id id da entidade a ser procurada
	* 
	* @return {@code true} se a exclusão for bem sucedida.
	* Caso contrário, retorna {@code false}.
	*/
	
	public boolean deleteObject(int id) {
		Produto produto;
		long address;
		try {
			accessFile = openFile();
			accessFile.seek(2);
			while (accessFile.getFilePointer() < accessFile.length()) {
				address = accessFile.getFilePointer();
				produto = readObject(accessFile);
				if (produto != null && produto.getId() == id) {
					accessFile.seek(address);
					accessFile.writeChar('*');
					return true;
				}
			}
			accessFile.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	* Percorre toda a base de dados procurando por uma entidade
	* que possua o id desejado para alterar na base de dados.
	* Para alterar marcar a lapide e inserir produto alterado no final do arquivo.
	* 
	* @param id id da entidade a ser alterada.
	* 
	* @return confirmação de alteração.
	*/
	
	public boolean changeObject(int id, Produto produto2) {
		Produto produto = new Produto();
		long address;
		try {
			accessFile = openFile();
			accessFile.seek(2);
			while (accessFile.getFilePointer() < accessFile.length()) {
				address = accessFile.getFilePointer();
				produto = readObject(accessFile);
				if (produto != null && produto.getId() == id) {
					System.out.println(produto.toString());
					//fazer a leitura dos novos dados (abaixo exemplo)
					//Produto produto2 = new Produto("TV", "4k Full HD Master", (float)2100.00);
					// confirmar alteração e excluí-lo do arquivo marcando a lápide e inserindo o novo no fim do arquivo
					// atualizar no indice
					accessFile.seek(address);
					accessFile.writeChar('*');
					writeObject(produto2);
					return true;
				}
			}
			accessFile.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// estrutura da base de dados
	// [ ultimo_id_usado (short), registros... ]
	//
	// estrutura dos registros
	// [ lápide (char), id (short), tamanho_da_entidade (int), entidade (Produto) ]
}