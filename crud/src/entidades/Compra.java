package entidades;

import java.io.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;

import serializaveis.SerializavelAbstract;

import user.Main;

import util.IO;

/**
 * Classe das entidades compra.
 */

public class Compra extends SerializavelAbstract implements Entidade
{
	private int id;
	private int idCliente;
	private Calendar data;
	private float valorTotal;

	public Compra(int id, int idCliente, float valorTotal) {
		this.id = id;
		this.idCliente = idCliente;
		this.valorTotal = valorTotal;
		
		this.data = new GregorianCalendar();
	}

	public Compra(float valorTotal) {
		this( -1, -1, valorTotal );
	}

	public Compra(){
		this(-1 );
	}
	
	@Override
	public int setId(int id) {
		return this.id = id;
	}
	
	@Override
	public int getId(){
		return this.id;
	}
	
	public int getIdCliente()
	{
		return idCliente;
	}

	public int setIdCliente(int idCliente)
	{
		return this.idCliente = idCliente;
	}

	public Calendar getData()
	{
		return data;
	}

	public void setData(Calendar data)
	{
		this.data = data;
	}

	public float getValorTotal()
	{
		return valorTotal;
	}

	public float setValorTotal(float valorTotal)
	{
		return this.valorTotal = valorTotal;
	}
		
	public float readValorTotal() {
		ArrayList<ItemComprado> itensComprados = Main.databaseItemComprado.list();
		
		float valorTotal = 0;
		int tam = itensComprados.size();
		
		//pegar cada item comprado com o idCompra e ir somando o valor unitário
		for(int i=0; i < tam; i++){
			if(itensComprados.get(i).getIdCompra() == this.id)
				valorTotal+= itensComprados.get(i).getValorUnitario() * itensComprados.get(i).getQuantidade();
		}
		
		return valorTotal;
	}
	
	
	@Override
	public String toString(){
		return
			"ID: " + this.id + '\n' +
			"IDCliente: " + this.idCliente + '\n' +
			"Data " + printData() + '\n' +
			"Valor Total: " + this.valorTotal;
	}
	
	public String printData() {
		int month = this.data.get(Calendar.MONTH);
		month++;
		
		return this.data.get(this.data.DAY_OF_MONTH) + "/" + 
				month + "/" +
				this.data.get(this.data.YEAR);
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
			dataStream.writeLong(this.data.getTimeInMillis());
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
			this.data.setTimeInMillis(dataStream.readLong());
			this.valorTotal = dataStream.readFloat();
			
			dataStream.close();
		} 
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}

