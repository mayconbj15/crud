package crud;

import java.io.*;

import java.util.ArrayList;

//colocar no array list e fazer o index para buscar por nome 

public class Arquivo {
	private String name;
	public short lastID;
	RandomAccessFile accessFile;

	public Arquivo(String nameFile) {
		this.name = nameFile;
		this.lastID = -1;
	}

	public short getLastID() {
		return this.lastID;
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
		
		catch (IOException IOEx) { }
		
		return ( lastID == -1 ? this.lastID : lastID );
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
		
		catch (IOException IOEx) { }
		
		return lastID;
	}
	
	public void writeObject(Produto produto) throws IOException {
		try {
			accessFile = openFile();
			
			produto.setId( (short) (readLastID() + 1) );
			setLastID( writeLastID( produto.getId() ) );
			
			byte[] byteArray = produto.setByteArray();

			// go to final of file
			accessFile.seek(accessFile.length());

			accessFile.writeChar(' '); //lapide
			accessFile.writeShort(produto.getId()); // write id
			accessFile.writeInt(byteArray.length);
			accessFile.write(byteArray);

			accessFile.close();
		} catch (FileNotFoundException fNFE) {
			fNFE.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método para listar todo o arquivo [EM CONSTRUÇÃO]
	 * @return ArrayList
	 * @throws IOException
	 */
	public ArrayList<Produto> list() throws IOException{
        ArrayList<Produto> listProdutos = new ArrayList<Produto>();

		try
		{
			accessFile = openFile();
			accessFile.seek(2);

			while (accessFile.getFilePointer() < accessFile.length()) {
				listProdutos.add(readObject(accessFile));
			}
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
       
        return listProdutos;
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
	 * desativado (lápide com '*'), o retorno é {@code null}.
	 */
	
	public Produto readObject(RandomAccessFile file)
	{
		Produto produto = null;
		
		try
		{
			char lapide = file.readChar();
			
			if (lapide != '*')
			{
				short id = file.readShort();
				int registerSize = file.readInt();
				
				byte[] byteArray = new byte[registerSize];
				
				file.readFully(byteArray, 0, registerSize);
				
				produto = new Produto();
				produto.fromByteArray(byteArray, id);
			}
		}
		
		catch (IOException IOEx) { }
		
		return produto;
	}
	
	public Produto readObject(int id) {
		Produto produto = null;

		try {
			accessFile = openFile();

			accessFile.seek(2);

			while (accessFile.getFilePointer() < accessFile.length()) {
				produto = readObject(accessFile);
			}
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}

		return produto;
	}
	
	//estrutura do vetor de byte
	// ultimo id usado - [lapide] - [id] - [tamanho da entidade] -> entidade
}