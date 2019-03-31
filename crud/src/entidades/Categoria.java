package entidades;

import java.io.*;

import util.IO;

/**
 * Classe das entidades categoria.
 */
//
public class Categoria implements Entidade{
	private int idCategoria;
	private String nome;

	public Categoria(int idCategoria, String nome) {
		this.idCategoria = idCategoria;
		this.nome = nome;
	}

	public Categoria(String nome) {
		this( -1, nome );
	}

	public Categoria(){
		this( "" );
	}

	@Override
	public int getIdCategoria() {
		return -1;
	}
	
	@Override
	public int setIdCategoria(int idCategoria) {
		return this.idCategoria = idCategoria;
	}
	
	@Override
	public int getId(){
		return this.idCategoria;
	}

	@Override
	public int setId(int idCategoria){
		return this.idCategoria = idCategoria;
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
			dataStream.writeInt(this.idCategoria);
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
			this.idCategoria = dataStream.readInt();
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
	
	public String print(){
		return
				"ID: " + this.idCategoria + '\n' +
				"Nome: " + this.nome;
	}
	
	public String toString(){
		return
			"ID: " + this.idCategoria + '\n' +
			"Nome: " + this.nome;
	}
}

