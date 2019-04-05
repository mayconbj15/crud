package entidades;

import java.io.*;

import serializaveis.SerializavelAbstract;

/**
 * Classe das entidades compra.
 */

public class Compra extends SerializavelAbstract implements Entidade
{
	private int id;
	private int idCliente;
	private long data;
	private float valorTotal;

	public Compra(int id, int idCliente, long data, float valorTotal) {
		this.id = id;
		this.idCliente = idCliente;
		this.data = data;
		this.valorTotal = valorTotal;
	}

	public Compra(long data, float valorTotal) {
		this( -1, -1, data, valorTotal );
	}

	public Compra(){
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

	@Override
	public int getIdSecundario() {
		return idCliente;
	}
	
	public long getData()
	{
		return data;
	}

	public long setData(long data)
	{
		return this.data = data;
	}

	public float getValorTotal()
	{
		return valorTotal;
	}

	public float setValorTotal(float valorTotal)
	{
		return this.valorTotal = valorTotal;
	}
	
	@Override
	public String toString(){
		return
			"ID: " + this.id + '\n' +
			"IDCliente: " + this.idCliente +
			"Data: " + this.data +
			"Valor Total: " + this.valorTotal;
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
			Integer.BYTES +
			Long.BYTES +
			Float.BYTES;
	}

	/**
	 * <p>
	 * Obs.: a estrutura do arranjo é a seguinte:
	 * [ id, idCliente, data, valorTotal ]
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
			dataStream.writeInt(this.idCliente);
			dataStream.writeLong(this.data);
			dataStream.writeFloat(this.valorTotal);
			
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
	 * [ id, idCliente, data, valorTotal ]
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
			this.idCliente = dataStream.readInt();
			this.data = dataStream.readLong();
			this.valorTotal = dataStream.readFloat();
			
			dataStream.close();
		} 
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}

