package crud;

import java.io.*;

import util.IO;

/**
 * Entidade comum da base de dados.
 */

public class Produto{
	private short id;
	private String nome;
	private String descricao;
	private float preco;
	private String fornecedor;
	private int quantidade;

	public Produto(){
		this( (short) -1, "", "", 0f, "", 0 );
	}

	public Produto(String nome, String descricao, float preco, String fornecedor, int quantidade) {
		this( (short) -1, nome, descricao, preco, fornecedor, quantidade );
	}

	public Produto(short id, String nome, String descricao, float preco, String fornecedor, int quantidade) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.preco = preco;
		this.fornecedor = fornecedor;
		this.quantidade = quantidade;
	}

	public short getId(){
		return this.id;
	}
	
	public short setId(int id){
		return this.id = (short)id;
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
	 * Gera um arranjo de bytes com os campos desta entidade.
	 * 
	 * Obs.: a estrutura do arranjo é a seguinte:
	 * [ nome, descrição, preço, fornecedor, quantidade ]
	 * 
	 * @return Arranjo de bytes com os campos desta entidade.
	 */
	
	public byte[] setByteArray() {
		ByteArrayOutputStream array = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(array);
		
		try{
			/*decidir nao colocar o id no array de bytes para facilicar na questão da busca*/
			//dataStream.writeShort(disco.getIdDisco());
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
	 * Extrai os valores desta entidade de {@code byteArray}.
	 * 
	 * Obs.: a estrutura de {@code byteArray} deve ser a seguinte:
	 * [ nome, descrição, preço, fornecedor, quantidade ]
	 * 
	 * @param byteArray Arranjo de bytes com cada um dos campos da entidade.
	 * @param id Id da entidade.
	 */
	
	public void fromByteArray(byte[] byteArray, short id){
		ByteArrayInputStream byteArrayStream = new ByteArrayInputStream(byteArray);
		DataInputStream dataStream = new DataInputStream(byteArrayStream);

		try{
			this.id = id;
			this.nome = dataStream.readUTF();
			this.descricao = dataStream.readUTF();
			this.preco = dataStream.readFloat();
			this.fornecedor = dataStream.readUTF();
			this.quantidade = dataStream.readInt();
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
		return setPreco( IO.readfloat("\nInforme o preço do produto: ") );
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
		return setQuantidade( IO.readint("\nInforme a quantidade de produtos: ") );
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
	
	public static Produto readProduct()
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

