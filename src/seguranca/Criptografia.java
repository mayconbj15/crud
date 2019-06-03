/**
 * Classe base de criptografia do projeto CRUD
 * 	onde o algoritmo base usado na criptografia 
 * 	é o algoritmo de colunas 
 */

package seguranca;

import java.io.UnsupportedEncodingException;
import util.IO;;

public class Criptografia{		
	private byte[] chave; 
	ElementoDeslocamento[] chaveTransformada;
	
	public Criptografia()
	{
		this.chave   = null;
	}
	
	public Criptografia(byte[] chave) 
	{		
		this.chave   = chave;
		transformarChave();
	}
	
	private void setChave(byte[] chave) {
		this.chave = chave;
	}
	
	/*
	 * Método que irá cifrar um vetor de bytes
	 */
	public byte[] cifrar(byte[] mensagem) {
		byte[] novaMensagem = null;
		
		if(mensagem != null) {

			novaMensagem = cifrarMensagem(mensagem);
		}
		
		return novaMensagem;
	}
	
	/**
	 * Método para transformar a chave e deixa-lá ordenada
	 */
	private void transformarChave(){
		int tamanhoChave = this.chave.length;
		this.chaveTransformada = new ElementoDeslocamento[tamanhoChave];
		
		for(int i=0; i<tamanhoChave; i++) {
			chaveTransformada[i] = new ElementoDeslocamento(this.chave[i], i);
		}
		
		sort(); //ordena o vetor de deslocamento com base na chave (caracter)

	}//end transformarChave()
	
	 /**
     * Algoritmo de ordenacao Quicksort.
     */
    private void sort() {
        quicksort(0, (this.chaveTransformada.length)-1);
    }
    
    private byte[] cifrarMensagem(byte[] mensagem) {
    	byte[] novaMensagem = new byte[mensagem.length];
    	int i;
    	int j;
    	int k = 0; //variavel que irá percorrer a nova mensagem colocando os caracteres no vetor
    	int tamanhoMensagem = mensagem.length;
    	int tamanhoChave = this.chaveTransformada.length; //que irá determinar o deslocamento de caracteres
    	
    	
    	for(i=0; i < this.chaveTransformada.length; i++) {
    		j = this.chaveTransformada[i].getDado(); //pega onde irá iniciar
    		
    		for( ; j<tamanhoMensagem; j = j + tamanhoChave) {
    			novaMensagem[k] = mensagem[j];
    			k++;
    		}
    	}
    	
    	return novaMensagem;
    }
    
    public byte[] decifrar(byte[] mensagem) {
    	byte[] novaMensagem = new byte[mensagem.length];
    	int i;
    	int j;
    	int k = 0; //variavel que irá percorrer a nova mensagem colocando os caracteres no vetor
    	int tamanhoMensagem = mensagem.length;
    	int tamanhoChave = this.chaveTransformada.length; //que irá determinar o deslocamento de caracteres
    	
    	
    	for(i=0; i < tamanhoMensagem; k++) {
    		j = this.chaveTransformada[k].getDado();
    		for( ; j<tamanhoMensagem; j = j + tamanhoChave, i++) {
    			novaMensagem[j] = mensagem[i];
    		}
    	}
 	
    	return novaMensagem;
    }
    
    /**
     * Algoritmo de ordenacao Quicksort de acordo com o nome.
	 * @param int esq inicio do array a ser ordenado
	 * @param int dir fim do array a ser ordenado
	 */
    private void quicksort(int esq, int dir) {
    	int i = esq, j = dir;
    	byte pivo = chaveTransformada[(dir+esq)/2].getChave(); //pega a posição do pivo
		           
    	while(i <= j) {
    		while (chaveTransformada[i].getChave() < pivo){
    			i++;
    		} 
              
	    	while(chaveTransformada[j].getChave() > pivo){
	    		j--;
	        } 
 
	        if(i <= j){
	        	swap(i, j);
	        	i++;
	        	j--;
	        }
    	}
    	if (esq < j) {
    		quicksort(esq, j);
    	} 
    	if (i < dir) {
    		quicksort(i, dir);
    	} 
      }
	 
 
    /**
    * Troca o conteudo de duas posicoes do array
    */
   private void swap(int i, int j) {
	   ElementoDeslocamento temp = chaveTransformada[i];
	   chaveTransformada[i] = chaveTransformada[j];
	   chaveTransformada[j] = temp;
   }
   	
   public static void main(String[] args) throws UnsupportedEncodingException{
		Criptografia cript = new Criptografia(new String("joao").getBytes("UTF-8"));
		
		String teste;
		while(true) {
			teste = IO.readLine("Digita carai");
			
			System.out.print("Original: ");
			byte[] testeBytes = teste.getBytes("UTF-8");
			for(int i=0; i<testeBytes.length; i++) {
				System.out.print(testeBytes[i] + " ");
			}
			System.out.println();
			
			byte[] mensagemCifrada = cript.cifrar(teste.getBytes("UTF-8"));
			byte[] mensagemOriginal = cript.decifrar(mensagemCifrada);
			
			System.out.print("Cifrada: ");
			for(int i=0; i<mensagemCifrada.length; i++) {
				System.out.print(mensagemCifrada[i] + " ");
			}
			System.out.println();
			
			System.out.print("Original: ");
			for(int i=0; i<mensagemOriginal.length; i++) {
				System.out.print(mensagemOriginal[i] + " ");
			}
		}
	}
}//end class
