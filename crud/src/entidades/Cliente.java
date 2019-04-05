package entidades;

import java.io.*;

import serializaveis.SerializavelAbstract;
import serializaveis.StringSerializavel;
import util.IO;

/**
 * Classe das entidades cliente.
 */

public class Cliente extends SerializavelAbstract implements Entidade
{
	private int id;
	private String nome;
	private String email;

	public Cliente(int id, String nome, String email) {
		this.id = id;
		this.nome = nome;
		this.email = email;
	}

	public Cliente(String nome, String email) {
		this( -1, nome, email );
	}

	public Cliente(){
		this( "", "" );
	}
	
	@Override
	public int setId(int id) {
		return this.id = id;
	}
	
	@Override
	public int getId(){
		return this.id;
	}

	@Override
	public int getIdSecundario() {
		return -1;
	}

	public String getNome(){
		return this.nome;
	}
	
	public String setNome(String nome){
		return this.nome = nome;
	}

	public String getEmail(){
		return this.email;
	}
	
	public String setEmail(String email){
		return this.email = email;
	}
	
	/**
	 * Lê o nome do cliente da entrada padrão e redefine
	 * interiormente o campo {@link #nome} desta entidade.
	 *  
	 * @return O nome lido.
	 */
	
	public String readName()
	{
		return setNome( IO.readLine("\nInforme o nome do cliente: ") );
	}
	
	/**
	 * Lê o email do cliente e redefine interiormente o campo
	 * {@link #email} desta entidade.
	 *  
	 * @return O email lido.
	 */
	
	public String readEmail()
	{
		return setEmail( IO.readLineUntilEmail("\nInforme o email do cliente: ") );
	}
	
	@Override
	public String toString(){
		return
			"ID: " + this.id + '\n' +
			"Nome: " + this.nome + '\n' +
			"Email: " + this.email;
	}
	
	@Override
	public String print(){
		return toString();
	}

	@Override
	public int obterTamanhoMaximoEmBytes()
	{
		return
			Integer.BYTES +
			StringSerializavel.PADRAO_TAMANHO_MAXIMO_EM_BYTES +
			StringSerializavel.PADRAO_TAMANHO_MAXIMO_EM_BYTES;
	}

	/**
	 * <p>
	 * Obs.: a estrutura do arranjo é a seguinte:
	 * [ id, nome, email ]
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
			dataStream.writeUTF(this.email);
			
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
	 * [ id, nome, email ]
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
			this.email = dataStream.readUTF();
			
			dataStream.close();
		} 
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}

