package seguranca;


public class ElementoDeslocamento{
	public char chave;
	public int dado;
	
	public ElementoDeslocamento() {
		this.chave = ' ';
		this.dado = 0;
	}
	
	public ElementoDeslocamento(char chave, int dado) {
		this.chave = chave;
		this.dado = dado;
	}

	public char getChave()
	{
		return chave;
	}

	public void setChave(char chave)
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
		return this.chave + "" + this.dado + "\n";
	}
	
}
