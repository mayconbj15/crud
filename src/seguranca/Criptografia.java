/**
 * Classe base de criptografia do projeto CRUD
 * 	onde o algoritmo base usado na criptografia 
 * 	é o algoritmo de colunas 
 */

package seguranca;

public class Criptografia{		
	private String chave; 
	ElementoDeslocamento[] chaveTransformada;
	
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
			cifrarMensagem(mensagem);
		}
		
		return novaMensagem;
	}
	
	/**
	 * Método para transformar a chave e deixa-lá ordenada
	 */
	private void transformarChave(){
		int tamanhoChave = this.chave.length();
		this.chaveTransformada = new ElementoDeslocamento[tamanhoChave];
		
		for(int i=0; i<tamanhoChave; i++) {
			chaveTransformada[i] = new ElementoDeslocamento(this.chave.charAt(i), i);
		}
		
		sort(); //ordena o vetor de deslocamento com base na chave (caracter)
	}//end transformarChave()
	
	 /**
     * Algoritmo de ordenacao Quicksort.
     */
    private void sort() {
        quicksort(0, (this.chaveTransformada.length)-1);
    }
    
    private void cifrarMensagem(String mensagem) {
    	char[] novaMensagem = new char[mensagem.length()];
    	int i;
    	int j;
    	int k = 0; //variavel que irá percorrer a nova mensagem colocando os caracteres no vetor
    	int deslocamento;
    	int tamanhoMensagem = mensagem.length();
    	
    	
    	
    	for(i=0; i < this.chaveTransformada.length; i++) {
    		j = this.chaveTransformada[i].getDado(); //pega onde irá iniciar
    		
    		for( ; j<tamanhoMensagem; j = j + 4) {
    			novaMensagem[k] = mensagem.charAt(j);
    			k++;
    		}
    	}
    	
    	for(i=0; i<novaMensagem.length; i++) {
    		System.out.print(novaMensagem[i] + " ");
    	}
    	System.out.println();
    }
   
    /**
     * Algoritmo de ordenacao Quicksort de acordo com o nome.
	 * @param int esq inicio do array a ser ordenado
	 * @param int dir fim do array a ser ordenado
	 */
    private void quicksort(int esq, int dir) {
    	int i = esq, j = dir;
    	char pivo = chaveTransformada[(dir+esq)/2].getChave(); //pega a posição do pivo
		           
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
	
	public static void main(String[] args) {
		Criptografia cript = new Criptografia("joao");
		
		cript.cifrar("Maycon Bruno de Jesus");
	}
}//end class
