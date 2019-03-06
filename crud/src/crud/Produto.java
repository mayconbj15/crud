package crud;

import java.io.*;

public class Produto{
	private short id;
	protected String nome;
	protected String descricao;
	protected float preco;
	protected String fornecedor;

	public Produto(){
		this((short)-1, "", "", (float)0.0, "");
	}

	public Produto(String nome, String descricao, float preco, String fornecedor) {
		this((short)-1, nome, descricao, preco, fornecedor);
	}

	public Produto(short id, String nome, String descricao, float preco, String fornecedor) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.preco = preco;
		this.fornecedor = fornecedor;
	}

	public short getId(){
		return this.id;
	}
	public void setId(int id){
		this.id = (short)id;
	}

	public String getNome(){
		return this.nome;
	}
	public void setNome(String nome){
		this.nome = nome;
	}

	public String getDescricao(){
		return this.descricao;
	}
	public void setDescricao(String descricao){
		this.descricao = descricao;
	}

	public float getPreco(){
		return this.preco;
	}
	public void setPreco(float preco){
		this.preco = preco;
	}
	
	public String getFornecedor() {
		return this.fornecedor;
	}
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public byte[] setByteArray() throws IOException{
		ByteArrayOutputStream array = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(array);
		
		try{
			/*decidir nao colocar o id no array de bytes para facilicar na questão da busca*/
			//dataStream.writeShort(disco.getIdDisco());
			dataStream.writeUTF(this.nome);
			dataStream.writeUTF(this.descricao);
			dataStream.writeFloat(this.preco);
			dataStream.writeUTF(this.fornecedor);
			
			dataStream.close();
			array.close();
			
		} 
		catch(IOException e){
			e.printStackTrace();
		}

		return array.toByteArray();
	}

	public void fromByteArray(byte[] byteArray, short id){
		ByteArrayInputStream byteArrayStream = new ByteArrayInputStream(byteArray);
		DataInputStream dataStream = new DataInputStream(byteArrayStream);

		try{
			this.id = id;
			this.nome = dataStream.readUTF();
			this.descricao = dataStream.readUTF();
			this.preco = dataStream.readFloat();
			this.fornecedor = dataStream.readUTF();
		} 
		catch(IOException e){
			e.printStackTrace();
		}
	}

	public String toString(){
		return "ID: " + this.id + '\n' + "Nome: " + this.nome + '\n' + "Descrição: " + this.descricao + '\n'
		+ "Preço: " + this.preco + '\n' + "Fornecedor: " + this.fornecedor + '\n';
	}
}

