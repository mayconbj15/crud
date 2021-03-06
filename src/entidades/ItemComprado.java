package entidades;

import java.io.*;
import java.util.ArrayList;

import serializaveis.SerializavelAbstract;
import user.Main;
import util.IO;

/**
 * Classe das entidades item comprado.
 */

public class ItemComprado extends SerializavelAbstract implements Entidade
{
	private int id;
	private int idCompra;
	private int idProduto;
	private int quantidade;
	private float valorUnitario;

	public ItemComprado(int id, int idCompra, int idProduto, int quantidade, float valorUnitario) {
		this.id = id;
		this.idCompra = idCompra;
		this.idProduto = idProduto;
		this.quantidade = quantidade;
		this.valorUnitario = valorUnitario;
	}

	public ItemComprado(int quantidade, float valorUnitario) {
		this( -1, -1, -1, quantidade, valorUnitario );
	}
	
	public ItemComprado(int idCompra, int idProduto, int quantidade, float valorUnitario) {
		this(-1, idCompra, idProduto, quantidade, valorUnitario);
	}
	
	public ItemComprado(){
		this( -1, -1 );
	}
	
	@Override
	public int setId(int id) {
		return this.id = id;
	}
	
	@Override
	public int getId(){
		return this.id;
	}

	public int getIdCompra()
	{
		return idCompra;
	}

	public int setIdCompra(int idCompra)
	{
		return this.idCompra = idCompra;
	}

	public int getIdProduto()
	{
		return idProduto;
	}

	public int setIdProduto(int idProduto)
	{
		return this.idProduto = idProduto;
	}

	public float getValorUnitario(){
		return this.valorUnitario;
	}
	
	public float setValorUnitario(float valorUnitario){
		return this.valorUnitario = valorUnitario;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public int setQuantidade(int quantidade) {
		return this.quantidade = quantidade;
	}
	
	public float getValorTotal()
	{
		return valorUnitario * quantidade;
	}
	
	/**
	 * Lê a quantidade de itens comprados da entrada padrão e redefine
	 * o campo {@link #quantidade} desta entidade.
	 *  
	 * @return A quantidade lida.
	 */
	
	public int readQuantity()
	{
		return setQuantidade(
			IO.readLineUntilPositiveInt("\nInforme a quantidade de itens comprados: ")
		);
	}
	
	/**
	 * Lê, da entrada padrão, o valor unitário do item comprado e redefine
	 * o campo {@link #valorUnitario} desta entidade.
	 *  
	 * @return O preço lido.
	 */
	
	public float readUnitaryValue()
	{
		return setValorUnitario(
			IO.readLineUntilPositiveFloat("\nInforme o valor unitário do item: ")
		);
	}
	
	@Override
	public String toString(){
		return
			"ID: " + this.id + '\n' +
			"IDCompra: " + this.idCompra + '\n' +
			"IDProduto: " + this.idProduto + '\n' +
			printToUser();
	}
	
	@Override
	public String print(){
		return toString();
	}
	
	@Override
	public String printToUser(){
		return
			"PRODUTO: " + '\n' + printProduto() + '\n' +  
			"Quantidade: " + this.quantidade + '\n' + 
			"Valor Unitário: " + this.valorUnitario + '\n';
	}
	
	public String printProduto() {
		Produto produto = Main.databaseProduto.readObject(this.idProduto);
		
		return "Nome do produto: " + produto.getNome() + '\n' + 
				"Descrição do produto: " + produto.getDescricao();
	}
	
	@Override
	public int obterTamanhoMaximoEmBytes()
	{
		return
			Integer.BYTES +
			Integer.BYTES +
			Integer.BYTES +
			Integer.BYTES +
			Float.BYTES;
	}

	public void listarItensComprados(ArrayList<ItemComprado> itensComprados) {
		itensComprados.forEach( (itemComprado) -> IO.println(itemComprado) );
	}
	
	/**
	 * <p>
	 * Obs.: a estrutura do arranjo é a seguinte:
	 * [ id, idCompra, idProduto, quantidade, valor unitário ]
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
			dataStream.writeInt(this.idCompra);
			dataStream.writeInt(this.idProduto);
			dataStream.writeInt(this.quantidade);
			dataStream.writeFloat(this.valorUnitario);
			
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
	 * [ id, idCompra, idProduto, quantidade, valor unitário ]
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
			this.idCompra = dataStream.readInt();
			this.idProduto = dataStream.readInt();
			this.quantidade = dataStream.readInt();
			this.valorUnitario = dataStream.readFloat();
			
			dataStream.close();
		} 
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}

