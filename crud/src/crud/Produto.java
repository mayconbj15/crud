package crud;

import java.io.*;

import util.IO;

/**
 * Entidade comum da base de dados.
 */

public class Produto implements Entidade{
	private int id;
	private String nome;
	private String descricao;
	private float preco;
	private String fornecedor;
	private int quantidade;

	public Produto(int id, String nome, String descricao, float preco, String fornecedor, int quantidade) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.preco = preco;
		this.fornecedor = fornecedor;
		this.quantidade = quantidade;
	}

	public Produto(String nome, String descricao, float preco, String fornecedor, int quantidade) {
		this( -1, nome, descricao, preco, fornecedor, quantidade );
	}

	public Produto(){
		this( "", "", 0f, "", 0 );
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

	public String getDescricao(){
		return this.descricao;
	}
	
	public String setDescricao(String descricao){
		return this.descricao = descricao;
	}

	public float getPreco(){
		return this.preco;
	}
	
	public float setPreco(float preco){
		return this.preco = preco;
	}
	
	public String getFornecedor() {
		return this.fornecedor;
	}
	
	public String setFornecedor(String fornecedor) {
		return this.fornecedor = fornecedor;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public int setQuantidade(int quantidade) {
		return this.quantidade = quantidade;
	}

	/**
	 * <p>
	 * Obs.: a estrutura do arranjo é a seguinte:
	 * [ id, nome, descrição, preço, fornecedor, quantidade ]
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
			dataStream.writeUTF(this.descricao);
			dataStream.writeFloat(this.preco);
			dataStream.writeUTF(this.fornecedor);
			dataStream.writeInt(this.quantidade);
			
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
	 * [ id, nome, descrição, preço, fornecedor, quantidade ]
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
			this.descricao = dataStream.readUTF();
			this.preco = dataStream.readFloat();
			this.fornecedor = dataStream.readUTF();
			this.quantidade = dataStream.readInt();
			
			byteArrayStream.close();
			dataStream.close();
		} 
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Lê o nome do produto da entrada padrão e redefine
	 * o campo {@link #nome} desta entidade.
	 *  
	 * @return O nome lido.
	 */
	
	public String readName()
	{
		return setNome( IO.readLine("\nInforme o nome do produto: ") );
	}
	
	/**
	 * Lê a descrição do produto da entrada padrão e redefine
	 * o campo {@link #descricao} desta entidade.
	 *  
	 * @return A descrição lida.
	 */
	
	public String readDescription()
	{
		return setDescricao( IO.readLine("\nInforme a descrição do produto: ") );
	}
	
	/**
	 * Lê o preço do produto da entrada padrão e redefine
	 * o campo {@link #preco} desta entidade.
	 *  
	 * @return O preço lido.
	 */
	
	public float readPrice()
	{
		float price = IO.readfloat("\nInforme o preço do produto: ");
		
		while (price < 0)
		{
			price = IO.readfloat("\nInforme o preço do produto: ");
		}
		
		return setPreco( price );
	}
	
	/**
	 * Lê o fornecedor do produto da entrada padrão e redefine
	 * o campo {@link #fornecedor} desta entidade.
	 *  
	 * @return O fornecedor lido.
	 */
	
	public String readProvider()
	{
		return setFornecedor( IO.readLine("\nInforme o fornecedor: ") );
	}
	
	/**
	 * Lê a quantidade de produtos da entrada padrão e redefine
	 * o campo {@link #quantidade} desta entidade.
	 *  
	 * @return A quantidade lida.
	 */
	
	public int readQuantity()
	{
		int quantity = IO.readint("\nInforme a quantidade de produtos: ");
		
		while (quantity < 0)
		{
			quantity = IO.readint("\nInforme a quantidade de produtos: ");
		}
		
		return setQuantidade( quantity );
	}
	
	/**
	* Le os campos de um produto da entrada padrão e
	* cria o objeto equivalente.
	* 
	* Obs.: caso o usuário informe valores não numéricos
	* para campos numéricos, estes são definidos como -1.
	* 
	* @return Produto com os campos lidos.
	*/
	
	public Produto readProduct()
	{
		Produto produto = new Produto();
		
		produto.readName();
		produto.readDescription();
		produto.readPrice();
		produto.readProvider();
		produto.readQuantity();
		
		return produto; 
	}

	public String toString(){
		return
			"ID: " + this.id + '\n' +
			"Nome: " + this.nome + '\n' +
			"Descrição: " + this.descricao + '\n' +
			"Preço: " + this.preco + '\n' +
			"Fornecedor: " + this.fornecedor + '\n' +
			"Quantidade: " + this.quantidade;
	}
}

