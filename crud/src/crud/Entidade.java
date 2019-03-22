package crud;

import java.io.IOException;

public interface Entidade {
	public int getId();
	public void setId(int id);
	public String getNome();
	public void setNome(String nome);
	public String getDescricao();
	public void setDescricao(String descricao);
	public byte[] setByteArray() throws IOException;
	public void fromByteArray(byte[] b) throws IOException;
	public void fromByteArray(byte[] byteArray, short id);
}
