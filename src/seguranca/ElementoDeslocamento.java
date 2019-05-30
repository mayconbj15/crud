package seguranca;

public class ElementoDeslocamento{
	public byte chave;
	public int dado; //esse dado representa onde o caracterer irá começar na String
	
	public ElementoDeslocamento() {
		this.chave = ' ';
		this.dado = 0;
	}
	
	public ElementoDeslocamento(byte chave, int dado) {
		this.chave = chave;
		this.dado = dado;
	}

	public byte getChave()
	{
		return chave;
	}

	public void setChave(byte chave)
	{
		this.chave = chave;
	}

	public int getDado()
	{
		return dado;
	}

	public void setDado(int dado)
	{
		this.dado = dado;
	}
	
	public String toString() {
		return this.chave + "" + this.dado;
	}
	
}
