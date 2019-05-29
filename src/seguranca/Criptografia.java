/**
 * Classe base de criptografia do projeto CRUD
 * 	onde o algoritmo base usado na criptografia 
 * 	é o algoritmo de colunas 
 */

package seguranca;

public class Criptografia{		
	private String chave; 
	
	public Criptografia()
	{
		this.chave   = null;
	}
	
	public Criptografia(String chave) 
	{		
		this.chave   = chave;
	}
	
	private void setChave(String chave) {
		this.chave = chave;
	}
	
	public String cifrar(String mensagem) {
		String novaMensagem = null;
		
		if(mensagem != null) {
			transformarChave();
		}
		
		return novaMensagem;
	}
	
	/**
	 * Método para colocar a chave em uma árvore para conseguir uma ordenação dos caracteres
	 */
	public void transformarChave(){
		int tamanhoChave = this.chave.length();
		ElementoDeslocamento[] elementosDeslocamento= new ElementoDeslocamento[tamanhoChave];
		
		for(int i=0; i<tamanhoChave; i++) {
			elementosDeslocamento[i] = new ElementoDeslocamento(this.chave.charAt(i), i);
		}
		
		for(int i=0; i<tamanhoChave; i++) {
			System.out.println(elementosDeslocamento[i]);
		}
	}//end transformarChave()
	
	public static void main(String[] args) {
		Criptografia cript = new Criptografia("joao");
		
		cript.cifrar("Maycon Bruno de Jesus");
	}
}//end class
