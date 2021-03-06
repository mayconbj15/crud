package entidades;

import java.io.*;

import gerenciadores.Autenticavel;
import serializaveis.SerializavelAbstract;
import serializaveis.StringSerializavel;


import util.IO;

/**
 * Classe das entidades cliente.
 */

public class Cliente extends SerializavelAbstract implements Entidade, Autenticavel
{
	private int id;
	private String nome;
	private String email;
	private String senha;

	public Cliente(int id, String nome, String email, String senha) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}

	public Cliente(String nome, String email, String senha) {
		this( -1, nome, email, senha );
	}

	public Cliente(){
		this( "", "", "");
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

	@Override
	public String getEmail(){
		return this.email;
	}
	
	public String setEmail(String email){
		return this.email = email;
	}
	
	@Override
	public String getSenha() {
		return senha;
	}

	public String setSenha(String senha) {
		return this.senha = senha;
	}

	/**
	 * Lê o nome do cliente da entrada padrão e redefine
	 * interiormente o campo {@link #nome} desta entidade.
	 *  
	 * @return O nome lido.
	 */

	public String readNome()
	{
		return setNome( IO.readLine("\nInforme o seu nome: ") );
	}
	
	/**
	 * Lê o email do cliente e redefine interiormente o campo
	 * {@link #email} desta entidade.
	 *  
	 * @return O email lido.
	 */
	
	@Override
	public String readEmail()
	{
		return setEmail( IO.readLineUntilEmail("\nInforme o seu email: ") );
	}
	
	/**
	 * Lê a senha do cliente e redefine interiormente o campo
	 * {@link #senha} desta entidade.
	 * 
	 * @return A senha lida.
	 */
	
	@Override
	public String readSenha() {
		return setSenha( IO.readSenha("\nInforme a sua senha: ") );
	}
	
	@Override
	public String toString(){
		return
			"ID: " + this.id + '\n' +
			printToUser();
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
			dataStream.writeUTF(this.senha);
			
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
			this.senha = dataStream.readUTF();
			
			dataStream.close();
		} 
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public String printToUser()
	{
		return
				"Nome: " + this.nome + '\n' +
				"Email: " + this.email;
	}
}

