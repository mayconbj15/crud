package crud;

import java.io.IOException;

public interface Entidade {
	public short getId();
	public short setId(int id);
	public String getNome();
	public String setNome(String nome);
	public String getDescricao();
	public String setDescricao(String descricao);
	public float getPreco();
	public float setPreco(float preco);
	public String getFornecedor();
	public String setFornecedor(String fornecedor);
	public int getQuantidade();
	public int setQuantidade(int quantidade);
	
	//colocar esses métodos na crud
	public String readName();
	public String readDescription();
	public float readPrice();
	public String readProvider();
	public int readQuantity();
	
	public byte[] setByteArray() throws IOException;
	//public void fromByteArray(byte[] b) throws IOException;
	public void fromByteArray(byte[] byteArray, short id);
	
	//isso ir para a crud
	/*public static Produto readProduct() {
		return null;
	}*/
	
	public Produto readProduct();
}
