package entidades;

import java.io.*;

import util.IO;

/**
 * Classe das entidades categoria.
 */

public class Categoria implements Entidade{
	private int id;
	private String nome;

	public Categoria(int id, int idCategoria, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Categoria(String nome) {
		this( -1, -1, nome );
	}

	public Categoria(){
		this( "" );
	}

	@Override
	public int getId(){
		return this.id;
	}

	@Override
	public int setId(int id){
		return this.id = id;
	}

	public String getNome(){
		return this.nome;
	}
	
	public String setNome(String nome){
		return this.nome = nome;
	}

	/**
	 * <p>
	 * Obs.: a estrutura do arranjo é a seguinte:
	 * [ id, nome ]
	 * </p>
	 * 
	 * {@inheritDoc}
	 */

	@Override
	public byte[] setByteArray() {
		ByteArrayOutputStream array = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(array);
		
		try{
			dataStream.writeInt(this.id);
			dataStream.writeUTF(this.nome);
			
			dataStream.close();
			array.close();
		} 
		
		catch(IOException e){
			e.printStackTrace();
		}

		return array.toByteArray();
	}

	/**
	 * <p>
	 * Obs.: a estrutura de {@code byteArray} deve ser a seguinte:
	 * [ id, nome ]
	 * </p>
	 * 
	 * {@inheritDoc}
	 */

	@Override
	public void fromByteArray(byte[] byteArray){
		ByteArrayInputStream byteArrayStream = new ByteArrayInputStream(byteArray);
		DataInputStream dataStream = new DataInputStream(byteArrayStream);

		try{
			this.id = dataStream.readInt();
			this.nome = dataStream.readUTF();
			
			byteArrayStream.close();
			dataStream.close();
		} 
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Lê o nome da categoria da entrada padrão e redefine
	 * interiormente o campo {@link #nome} desta entidade.
	 *  
	 * @return O nome lido.
	 */
	
	public String readName()
	{
		return setNome( IO.readLine("\nInforme o nome da categoria: ") );
	}
	
	/**
	* Le os campos de uma categoria da entrada padrão e
	* cria o objeto equivalente.
	* 
	* @return Categoria com os campos lidos.
	*/
	
	public Categoria readCategory()
	{
		Categoria category = new Categoria();
		
		category.readName();
		
		return category; 
	}

	public String toString(){
		return
			"ID: " + this.id + '\n' +
			"Nome: " + this.nome;
	}
}

