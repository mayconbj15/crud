package entidades;

import java.io.*;

import serializaveis.SerializavelAbstract;
import serializaveis.StringSerializavel;
import util.IO;

/**
 * Classe das entidades categoria.
 */

public class Categoria extends SerializavelAbstract implements Entidade
{
	private int id;
	private String nome;

	public Categoria(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Categoria(String nome) {
		this( -1, nome );
	}

	public Categoria(){
		this( "" );
	}
	
	@Override
	public int setId(int id) {
		return this.id = id;
	}
	
	@Override
	public int getId(){
		return this.id;
	}

	public String getNome(){
		return this.nome;
	}
	
	public String setNome(String nome){
		return this.nome = nome;
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
	
	public String toString(){
		return
			"ID: " + this.id + '\n' +
			"Nome: " + this.nome;
	}
	
	public String print(){
		return toString();
	}

	@Override
	public int obterTamanhoMaximoEmBytes()
	{
		return Integer.BYTES + StringSerializavel.PADRAO_TAMANHO_MAXIMO_EM_BYTES;
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
	public byte[] obterBytes()
	{
		ByteArrayOutputStream array = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(array);
		
		try
		{
			dataStream.writeInt(this.id);
			dataStream.writeUTF(this.nome);
			
			dataStream.close();
		} 
		
		catch(IOException e)
		{
			e.printStackTrace();
		}

		return array.toByteArray();
	}

	/**
	 * <p>
	 * Obs.: a estrutura de {@code bytes} deve ser a seguinte:
	 * [ id, nome ]
	 * </p>
	 * 
	 * {@inheritDoc}
	 */

	@Override
	public void lerBytes(byte[] bytes)
	{
		ByteArrayInputStream byteArrayStream = new ByteArrayInputStream(bytes);
		DataInputStream dataStream = new DataInputStream(byteArrayStream);

		try
		{
			this.id = dataStream.readInt();
			this.nome = dataStream.readUTF();
			
			dataStream.close();
		} 
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}

