package crud;
import java.io.*;
import java.util.ArrayList;

// Esta versão da árvore funciona apenas como um conjunto de par de chaves.
// A primeira chave pode repetir na árvore, mas não o par de chaves, 
// isto é, quando a primeira chave de dois elementos for igual, a segunda chave,
// deve ser necessariamente diferente.

public class ArvoreBMais_ChaveComposta {

    private int  ordem;                 // Número máximo de filhos que uma página pode conter
    private int  maxElementos;          // Variável igual a ordem - 1 para facilitar a clareza do código
    private int  maxFilhos;             // Variável igual a ordem para facilitar a clareza do código
    private RandomAccessFile arquivo;   // Arquivo em que a árvore será armazenada
    private String nomeArquivo;
    
    // Variáveis usadas nas funções recursivas (já que não é possível passar valores por referência)
    private int  chave1Aux;
    private int  chave2Aux;
    private long paginaAux;
    private boolean cresceu;
    private boolean diminuiu;
    
    // Esta classe representa uma página da árvore (folha ou não folha). 
    private class Pagina {

        protected int    ordem;                 // Número máximo de filhos que uma página pode ter
        protected int    maxElementos;          // Variável igual a ordem - 1 para facilitar a clareza do código
        protected int    maxFilhos;             // Variável igual a ordem  para facilitar a clareza do código
        protected int    n;                     // Número de elementos presentes na página
        protected int[]  chaves1;               // Chave 1 (principal)
        protected int[]  chaves2;               // Chave 2 (subordinada)
        protected long   proxima;               // Próxima folha, quando a página for uma folha
        protected long[] filhos;                // Vetor de ponteiros para os filhos
        protected int    TAMANHO_REGISTRO;      // Os elementos são de tamanho fixo
        protected int    TAMANHO_PAGINA;        // A página será de tamanho fixo, calculado a partir da ordem

        // Construtor da página
        public Pagina(int o) {

            // Inicialização dos atributos
            n = 0;
            ordem = o;
            maxFilhos = o;
            maxElementos = o-1;
            chaves1 = new int[maxElementos];
            chaves2 = new int[maxElementos];
            filhos = new long[maxFilhos];
            proxima = -1;
            
            // Criação de uma página vázia
            for(int i=0; i<maxElementos; i++) {  
                chaves1[i] = 0;
                chaves2[i] = 0;
                filhos[i]  = -1;
            }
            filhos[maxFilhos-1] = -1;
            
            // Cálculo do tamanho (fixo) da página
            // n -> 4 bytes
            // cada elemento -> 8 bytes (int + int)
            // cada ponteiro de filho -> 8 bytes
            // último filho -> 8 bytes
            // ponteiro próximo -> 8 bytes
            TAMANHO_REGISTRO = 8;
            TAMANHO_PAGINA = 4 + maxElementos*TAMANHO_REGISTRO + maxFilhos*8 + 16;
        }
        
        // Retorna o vetor de bytes que representa a página para armazenamento em arquivo
        protected byte[] getBytes() throws IOException {
            
            // Um fluxo de bytes é usado para construção do vetor de bytes
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(ba);
            
            // Quantidade de elementos presentes na página
            out.writeInt(n);
            
            // Escreve todos os elementos
            int i=0;
            while(i<n) {
                out.writeLong(filhos[i]);
                out.writeInt(chaves1[i]);
                out.writeInt(chaves2[i]);
                i++;
            }
            out.writeLong(filhos[i]);
            
            // Completa o restante da página com registros vazios
            byte[] registroVazio = new byte[TAMANHO_REGISTRO];
            while(i<maxElementos){
                out.write(registroVazio);
                out.writeLong(filhos[i+1]);
                i++;
            }

            // Escreve o ponteiro para a próxima página
            out.writeLong(proxima);
            
            // Retorna o vetor de bytes que representa a página
            return ba.toByteArray();
        }

        
        // Reconstrói uma página a partir de um vetor de bytes lido no arquivo
        public void setBytes(byte[] buffer) throws IOException {
            
            // Usa um fluxo de bytes para leitura dos atributos
            ByteArrayInputStream ba = new ByteArrayInputStream(buffer);
            DataInputStream in = new DataInputStream(ba);
            
            // Lê a quantidade de elementos da página
            n = in.readInt();
            
            // Lê todos os elementos (reais ou vazios)
            int i=0;
            while(i<maxElementos) {
                filhos[i]  = in.readLong();
                chaves1[i] = in.readInt();
                chaves2[i] = in.readInt();
                i++;
            }
            filhos[i] = in.readLong();
            proxima = in.readLong();
        }
    }
    
    // ------------------------------------------------------------------------------
        
    
    public ArvoreBMais_ChaveComposta(int o, String na) throws IOException {
        
        // Inicializa os atributos da árvore
        ordem = o;
        maxElementos = o-1;
        maxFilhos = o;
        nomeArquivo = na;
        
        // Abre (ou cria) o arquivo, escrevendo uma raiz vazia, se necessário.
        arquivo = new RandomAccessFile(nomeArquivo,"rw");
        if(arquivo.length()<8) 
            arquivo.writeLong(-1);  // raiz vazia
    }
    
    // Testa se a árvore está vazia. Uma árvore vazia é identificada pela raiz == -1
    public boolean vazia() throws IOException {
        long raiz;
        arquivo.seek(0);
        raiz = arquivo.readLong();
        return raiz == -1;
    }
    
        
    // Busca recursiva por um elemento a partir da chave. Este metodo invoca 
    // o método recursivo lista1, passando a raiz como referência.
    // O método retorna a lista de elementos que possuem a chave (considerando
    // a possibilidade chaves repetidas)
    public int[] lista(int c1) throws IOException {
        
        // Recupera a raiz da árvore
        long raiz;
        arquivo.seek(0);
        raiz = arquivo.readLong();
        
        // Executa a busca recursiva
        if(raiz!=-1)
            return lista1(c1,raiz);
        else
            return new int[0];
    }
    
    // Busca recursiva. Este método recebe a referência de uma página e busca
    // pela chave na mesma. A busca continua pelos filhos, se houverem.
    private int[] lista1(int chave1, long pagina) throws IOException {
        
        // Como a busca é recursiva, a descida para um filho inexistente
        // (filho de uma página folha) retorna um vetor vazio.
        if(pagina==-1)
            return new int[0];
        
        // Reconstrói a página passada como referência a partir 
        // do registro lido no arquivo
        arquivo.seek(pagina);
        Pagina pa = new Pagina(ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
        pa.setBytes(buffer);
 
        // Encontra o ponto em que a chave deve estar na página
        // Nesse primeiro passo, todas as chaves menores que a chave buscada
        // são ultrapassadas
        int i=0;
        while(i<pa.n && chave1>pa.chaves1[i]) {
            i++;
        }
        
        // Chave encontrada (ou pelo menos o ponto onde ela deveria estar).
        // Segundo passo - testa se a chave é a chave buscada e se está em uma folha
        // Obs.: em uma árvore B+, todas as chaves válidas estão nas folhas
        if(i<pa.n && pa.filhos[0]==-1 && chave1==pa.chaves1[i]) {

            // Cria a lista de retorno e insere as chaves secundárias encontradas
            ArrayList lista = new ArrayList();
            while(chave1<=pa.chaves1[i]) {
                
                if(chave1==pa.chaves1[i])
                    lista.add(pa.chaves2[i]);
                i++;

                // Se chegar ao fim da folha, então avança para a folha seguinte
                if(i==pa.n) {
                    if(pa.proxima==-1)
                        break;
                    arquivo.seek(pa.proxima);
                    arquivo.read(buffer);
                    pa.setBytes(buffer);
                    i=0;
                }
            }
            
            // Constrói o vetor de resposta
            int[] resposta = new int[lista.size()];
            for(int j=0; j<lista.size(); j++)
                resposta[j] = (int)lista.get(j);
            return resposta;

        }
        
        // Terceiro passo - se a chave não tiver sido encontrada nesta folha, 
        // testa se ela está na próxima folha. Isso pode ocorrer devido ao 
        // processo de ordenação. 
        else if(i==pa.n && pa.filhos[0]==-1) { 
            
            // Testa se há uma próxima folha. Nesse caso, retorna um vetor vazio
            if(pa.proxima==-1)
                return new int[0];
            
            // Lê a próxima folha
            arquivo.seek(pa.proxima);
            arquivo.read(buffer);
            pa.setBytes(buffer);
            
            // Testa se a chave é a primeira da próxima folha
            i=0;
            if(chave1<=pa.chaves1[0]) {
                
                // Cria a lista de retorno
                ArrayList lista = new ArrayList();
                
                // Testa se a chave foi encontrada, e adiciona todas as chaves
                // secundárias
                while(chave1<=pa.chaves1[i]) {
                    if(chave1==pa.chaves1[i])
                        lista.add(pa.chaves2[i]);
                    i++;
                    if(i==pa.n) {
                        if(pa.proxima==-1)
                            break;
                        arquivo.seek(pa.proxima);
                        arquivo.read(buffer);
                        pa.setBytes(buffer);
                        i=0;
                    }
                }
                
                // Constrói o vetor de respostas
                int[] resposta = new int[lista.size()];
                for(int j=0; j<lista.size(); j++)
                    resposta[j] = (int)lista.get(j);
                return resposta;
            }
            
            // Se não houver uma próxima página, retorna um vetor vazio
            else
                return new int[0];
        }
        
        // Chave ainda não foi encontrada, continua a busca recursiva pela árvore
        if(i==pa.n || chave1<=pa.chaves1[i])
            return lista1(chave1, pa.filhos[i]);
        else
            return lista1(chave1, pa.filhos[i+1]);
    }
        
    
    // Inclusão de novos elementos na árvore. A inclusão é recursiva. A primeira
    // função chama a segunda recursivamente, passando a raiz como referência.
    // Eventualmente, a árvore pode crescer para cima.
    public boolean inserir(int c1, int c2) throws IOException {

        // Validação das chaves
        if(c1<0 || c2<0) {
            System.out.println( "Chaves não podem ser negativas" );
            return false;
        }
            
        // Carrega a raiz
        arquivo.seek(0);       
        long pagina;
        pagina = arquivo.readLong();

        // O processo de inclusão permite que os valores passados como referência
        // sejam substituídos por outros valores, para permitir a divisão de páginas
        // e crescimento da árvore. Assim, são usados os valores globais chave1Aux 
        // e chave2Aux. Quando há uma divisão, as chaves promovidas são armazenadas
        // nessas variáveis.
        chave1Aux = c1;
        chave2Aux = c2;
        
        // Se houver crescimento, então será criada uma página extra e será mantido um
        // ponteiro para essa página. Os valores também são globais.
        paginaAux = -1;
        cresceu = false;
                
        // Chamada recursiva para a inserção do par de chaves
        boolean inserido = inserir1(pagina);
        
        // Testa a necessidade de criação de uma nova raiz.
        if(cresceu) {
            
            // Cria a nova página que será a raiz. O ponteiro esquerdo da raiz
            // será a raiz antiga e o seu ponteiro direito será para a nova página.
            Pagina novaPagina = new Pagina(ordem);
            novaPagina.n = 1;
            novaPagina.chaves1[0] = chave1Aux;
            novaPagina.chaves2[0] = chave2Aux;
            novaPagina.filhos[0] = pagina;
            novaPagina.filhos[1] = paginaAux;
            
            // Acha o espaço em disco. Nesta versão, todas as novas páginas
            // são escrita no fim do arquivo.
            arquivo.seek(arquivo.length());
            long raiz = arquivo.getFilePointer();
            arquivo.write(novaPagina.getBytes());
            arquivo.seek(0);
            arquivo.writeLong(raiz);
        }
        
        return inserido;
    }
    
    
    // Função recursiva de inclusão. A função passa uma página de referência.
    // As inclusões são sempre feitas em uma folha.
    private boolean inserir1(long pagina) throws IOException {
        
        // Testa se passou para o filho de uma página folha. Nesse caso, 
        // inicializa as variáveis globais de controle.
        if(pagina==-1) {
            cresceu = true;
            paginaAux = -1;
            return false;
        }
        
        // Lê a página passada como referência
        arquivo.seek(pagina);
        Pagina pa = new Pagina(ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
        pa.setBytes(buffer);
        
        // Busca o próximo ponteiro de descida. Como pode haver repetição
        // da primeira chave, a segunda também é usada como referência.
        // Nesse primeiro passo, todos os pares menores são ultrapassados.
        int i=0;
        while(i<pa.n && (chave1Aux>pa.chaves1[i] || (chave1Aux==pa.chaves1[i] && chave2Aux>pa.chaves2[i]))) {
            i++;
        }
        
        // Testa se o registro já existe em uma folha. Se isso acontecer, então 
        // a inclusão é cancelada.
        if(i<pa.n && pa.filhos[0]==-1 && chave1Aux==pa.chaves1[i] && chave2Aux==pa.chaves2[i]) {
            cresceu = false;
            return false;
        }
        
        // Continua a busca recursiva por uma nova página. A busca continuará até o
        // filho inexistente de uma página folha ser alcançado.
        boolean inserido;
        if(i==pa.n || chave1Aux<pa.chaves1[i] || (chave1Aux==pa.chaves1[i] && chave2Aux<pa.chaves2[i]))
            inserido = inserir1(pa.filhos[i]);
        else
            inserido = inserir1(pa.filhos[i+1]);
        
        // A partir deste ponto, as chamadas recursivas já foram encerradas. 
        // Assim, o próximo código só é executado ao retornar das chamadas recursivas.

        // A inclusão já foi resolvida por meio de uma das chamadas recursivas. Nesse
        // caso, apenas retorna para encerrar a recursão.
        // A inclusão pode ter sido resolvida porque o par de chaves já existia (inclusão inválida)
        // ou porque o novo elemento coube em uma página existente.
        if(!cresceu)
            return inserido;
        
        // Se tiver espaço na página, faz a inclusão nela mesmo
        if(pa.n<maxElementos) {

            // Puxa todos elementos para a direita, começando do último
            // para gerar o espaço para o novo elemento
            for(int j=pa.n; j>i; j--) {
                pa.chaves1[j] = pa.chaves1[j-1];
                pa.chaves2[j] = pa.chaves2[j-1];
                pa.filhos[j+1] = pa.filhos[j];
            }
            
            // Insere o novo elemento
            pa.chaves1[i] = chave1Aux;
            pa.chaves2[i] = chave2Aux;
            pa.filhos[i+1] = paginaAux;
            pa.n++;
            
            // Escreve a página atualizada no arquivo
            arquivo.seek(pagina);
            arquivo.write(pa.getBytes());
            
            // Encerra o processo de crescimento e retorna
            cresceu=false;
            return true;
        }
        
        // O elemento não cabe na página. A página deve ser dividida e o elemento
        // do meio deve ser promovido (sem retirar a referência da folha).
        
        // Cria uma nova página
        Pagina np = new Pagina(ordem);
        
        // Copia a metade superior dos elementos para a nova página,
        // considerando que maxElementos pode ser ímpar
        int meio = maxElementos/2;
        for(int j=0; j<(maxElementos-meio); j++) {    
            
            // copia o elemento
            np.chaves1[j] = pa.chaves1[j+meio];
            np.chaves2[j] = pa.chaves2[j+meio];   
            np.filhos[j+1] = pa.filhos[j+meio+1];  
            
            // limpa o espaço liberado
            pa.chaves1[j+meio] = 0;
            pa.chaves2[j+meio] = 0;
            pa.filhos[j+meio+1] = -1;
        }
        np.filhos[0] = pa.filhos[meio];
        np.n = maxElementos-meio;
        pa.n = meio;
        
        // Testa o lado de inserção
        // Caso 1 - Novo registro deve ficar na página da esquerda
        if(i<=meio) {   
            
            // Puxa todos os elementos para a direita
            for(int j=meio; j>0 && j>i; j--) {
                pa.chaves1[j] = pa.chaves1[j-1];
                pa.chaves2[j] = pa.chaves2[j-1];
                pa.filhos[j+1] = pa.filhos[j];
            }
            
            // Insere o novo elemento
            pa.chaves1[i] = chave1Aux;
            pa.chaves2[i] = chave2Aux;
            pa.filhos[i+1] = paginaAux;
            pa.n++;
            
            // Se a página for folha, seleciona o primeiro elemento da página 
            // da direita para ser promovido, mantendo-o na folha
            if(pa.filhos[0]==-1) {
                chave1Aux = np.chaves1[0];
                chave2Aux = np.chaves2[0];
            }
            
            // caso contrário, promove o maior elemento da página esquerda
            // removendo-o da página
            else {
                chave1Aux = pa.chaves1[pa.n-1];
                chave2Aux = pa.chaves2[pa.n-1];
                pa.chaves1[pa.n-1] = 0;
                pa.chaves2[pa.n-1] = 0;
                pa.filhos[pa.n] = -1;
                pa.n--;
            }
        } 
        
        // Caso 2 - Novo registro deve ficar na página da direita
        else {
            int j;
            for(j=maxElementos-meio; j>0 && (chave1Aux<np.chaves1[j-1] || (chave1Aux==np.chaves1[j-1]&&chave2Aux<np.chaves2[j-1]) ); j--) {
                np.chaves1[j] = np.chaves1[j-1];
                np.chaves2[j] = np.chaves2[j-1];
                np.filhos[j+1] = np.filhos[j];
            }
            np.chaves1[j] = chave1Aux;
            np.chaves2[j] = chave2Aux;
            np.filhos[j+1] = paginaAux;
            np.n++;

            // Seleciona o primeiro elemento da página da direita para ser promovido
            chave1Aux = np.chaves1[0];
            chave2Aux = np.chaves2[0];
            
            // Se não for folha, remove o elemento promovido da página
            if(pa.filhos[0]!=-1) {
                for(j=0; j<np.n-1; j++) {
                    np.chaves1[j] = np.chaves1[j+1];
                    np.chaves2[j] = np.chaves2[j+1];
                    np.filhos[j] = np.filhos[j+1];
                }
                np.filhos[j] = np.filhos[j+1];
                
                // apaga o último elemento
                np.chaves1[j] = 0;
                np.chaves2[j] = 0;
                np.filhos[j+1] = -1;
                np.n--;
            }

        }
        
        // Se a página era uma folha e apontava para outra folha, 
        // então atualiza os ponteiros dessa página e da página nova
        if(pa.filhos[0]==-1) {
            np.proxima=pa.proxima;
            pa.proxima = arquivo.length();
        }

        // Grava as páginas no arquivos arquivo
        paginaAux = arquivo.length();
        arquivo.seek(paginaAux);
        arquivo.write(np.getBytes());

        arquivo.seek(pagina);
        arquivo.write(pa.getBytes());
        
        return true;
    }

    
    // Remoção elementos na árvore. A remoção é recursiva. A primeira
    // função chama a segunda recursivamente, passando a raiz como referência.
    // Eventualmente, a árvore pode reduzir seu tamanho, por meio da exclusão da raiz.
    public boolean excluir(int chave1, int chave2) throws IOException {
                
        // Encontra a raiz da árvore
        arquivo.seek(0);       
        long pagina;                
        pagina = arquivo.readLong();

        // variável global de controle da redução do tamanho da árvore
        diminuiu = false;  
                
        // Chama recursivamente a exclusão de registro (na chave1Aux e no 
        // chave2Aux) passando uma página como referência
        boolean excluido = excluir1(chave1, chave2, pagina);
        
        // Se a exclusão tiver sido possível e a página tiver reduzido seu tamanho,
        // por meio da fusão das duas páginas filhas da raiz, elimina essa raiz
        if(excluido && diminuiu) {
            
            // Lê a raiz
            arquivo.seek(pagina);
            Pagina pa = new Pagina(ordem);
            byte[] buffer = new byte[pa.TAMANHO_PAGINA];
            arquivo.read(buffer);
            pa.setBytes(buffer);
            
            // Se a página tiver 0 elementos, apenas atualiza o ponteiro para a raiz,
            // no cabeçalho do arquivo, para o seu primeiro filho.
            if(pa.n == 0) {
                arquivo.seek(0);
                arquivo.writeLong(pa.filhos[0]);  
            }
        }
         
        return excluido;
    }
    

    // Função recursiva de exclusão. A função passa uma página de referência.
    // As exclusões são sempre feitas em folhas e a fusão é propagada para cima.
    private boolean excluir1(int chave1, int chave2, long pagina) throws IOException {
        
        // Declaração de variáveis
        boolean excluido=false;
        int diminuido;
        
        // Testa se o registro não foi encontrado na árvore, ao alcançar uma folha
        // inexistente (filho de uma folha real)
        if(pagina==-1) {
            diminuiu=false;
            return false;
        }
        
        // Lê o registro da página no arquivo
        arquivo.seek(pagina);
        Pagina pa = new Pagina(ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
        pa.setBytes(buffer);

        // Encontra a página em que o par de chaves está presente
        // Nesse primeiro passo, salta todas os pares de chaves menores
        int i=0;
        while(i<pa.n && (chave1>pa.chaves1[i] || (chave1==pa.chaves1[i] && chave2>pa.chaves2[i]))) {
            i++;
        }

        // Chaves encontradas em uma folha
        if(i<pa.n && pa.filhos[0]==-1 && chave1==pa.chaves1[i] && chave2==pa.chaves2[i]) {

            // Puxa todas os elementos seguintes para uma posição anterior, sobrescrevendo
            // o elemento a ser excluído
            int j;
            for(j=i; j<pa.n-1; j++) {
                pa.chaves1[j] = pa.chaves1[j+1];
                pa.chaves2[j] = pa.chaves2[j+1];
            }
            pa.n--;
            
            // limpa o último elemento
            pa.chaves1[pa.n] = 0;
            pa.chaves2[pa.n] = 0;
            
            // Atualiza o registro da página no arquivo
            arquivo.seek(pagina);
            arquivo.write(pa.getBytes());
            
            // Se a página contiver menos elementos do que o mínimo necessário,
            // indica a necessidade de fusão de páginas
            diminuiu = pa.n<maxElementos/2;
            return true;
        }

        // Se a chave não tiver sido encontrada (observar o return true logo acima),
        // continua a busca recursiva por uma nova página. A busca continuará até o
        // filho inexistente de uma página folha ser alcançado.
        // A variável diminuído mantem um registro de qual página eventualmente 
        // pode ter ficado com menos elementos do que o mínimo necessário.
        // Essa página será filha da página atual
        if(i==pa.n || chave1<pa.chaves1[i] || (chave1==pa.chaves1[i] && chave2<pa.chaves2[i])) {
            excluido = excluir1(chave1, chave2, pa.filhos[i]);
            diminuido = i;
        } else {
            excluido = excluir1(chave1, chave2, pa.filhos[i+1]);
            diminuido = i+1;
        }
        
        
        // A partir deste ponto, o código é executado após o retorno das chamadas
        // recursivas do método
        
        // Testa se há necessidade de fusão de páginas
        if(diminuiu) {

            // Carrega a página filho que ficou com menos elementos do 
            // do que o mínimo necessário
            long paginaFilho = pa.filhos[diminuido];
            Pagina pFilho = new Pagina(ordem);
            arquivo.seek(paginaFilho);
            arquivo.read(buffer);
            pFilho.setBytes(buffer);
            
            // Cria uma página para o irmão (da direita ou esquerda)
            long paginaIrmao;
            Pagina pIrmao;
            
            // Tenta a fusão com irmão esquerdo
            if(diminuido>0) {
                
                // Carrega o irmão esquerdo
                paginaIrmao = pa.filhos[diminuido-1];
                pIrmao = new Pagina(ordem);
                arquivo.seek(paginaIrmao);
                arquivo.read(buffer);
                pIrmao.setBytes(buffer);
                
                // Testa se o irmão pode ceder algum registro
                if(pIrmao.n>maxElementos/2) {
                    
                    // Move todos os elementos do filho aumentando uma posição
                    // à esquerda, gerando espaço para o elemento cedido
                    for(int j=pFilho.n; j>0; j--) {
                        pFilho.chaves1[j] = pFilho.chaves1[j-1];
                        pFilho.chaves2[j] = pFilho.chaves2[j-1];
                        pFilho.filhos[j+1] = pFilho.filhos[j];
                    }
                    pFilho.filhos[1] = pFilho.filhos[0];
                    pFilho.n++;
                    
                    // Se for folha, copia o elemento do irmão, já que o do pai
                    // será extinto ou repetido
                    if(pFilho.filhos[0]==-1) {
                        pFilho.chaves1[0] = pIrmao.chaves1[pIrmao.n-1];
                        pFilho.chaves2[0] = pIrmao.chaves2[pIrmao.n-1];
                    }
                    
                    // Se não for folha, rotaciona os elementos, descendo o elemento do pai
                    else {
                        pFilho.chaves1[0] = pa.chaves1[diminuido-1];
                        pFilho.chaves2[0] = pa.chaves2[diminuido-1];
                    }

                    // Copia o elemento do irmão para o pai (página atual)
                    pa.chaves1[diminuido-1] = pIrmao.chaves1[pIrmao.n-1];
                    pa.chaves2[diminuido-1] = pIrmao.chaves2[pIrmao.n-1];
                        
                    
                    // Reduz o elemento no irmão
                    pFilho.filhos[0] = pIrmao.filhos[pIrmao.n];
                    pIrmao.n--;
                    diminuiu = false;
                }
                
                // Se não puder ceder, faz a fusão dos dois irmãos
                else {

                    // Se a página reduzida não for folha, então o elemento 
                    // do pai deve ser copiado para o irmão
                    if(pFilho.filhos[0] != -1) {
                        pIrmao.chaves1[pIrmao.n] = pa.chaves1[diminuido-1];
                        pIrmao.chaves2[pIrmao.n] = pa.chaves2[diminuido-1];
                        pIrmao.filhos[pIrmao.n+1] = pFilho.filhos[0];
                        pIrmao.n++;
                    }
                    
                    
                    // Copia todos os registros para o irmão da esquerda
                    for(int j=0; j<pFilho.n; j++) {
                        pIrmao.chaves1[pIrmao.n] = pFilho.chaves1[j];
                        pIrmao.chaves2[pIrmao.n] = pFilho.chaves2[j];
                        pIrmao.filhos[pIrmao.n+1] = pFilho.filhos[j+1];
                        pIrmao.n++;
                    }
                    pFilho.n = 0;   // aqui o endereço do filho poderia ser incluido em uma lista encadeada no cabeçalho, indicando os espaços reaproveitáveis
                    
                    // Se as páginas forem folhas, copia o ponteiro para a folha seguinte
                    if(pIrmao.filhos[0]==-1)
                        pIrmao.proxima = pFilho.proxima;
                    
                    // puxa os registros no pai
                    int j;
                    for(j=diminuido-1; j<pa.n-1; j++) {
                        pa.chaves1[j] = pa.chaves1[j+1];
                        pa.chaves2[j] = pa.chaves2[j+1];
                        pa.filhos[j+1] = pa.filhos[j+2];
                    }
                    pa.chaves1[j] = 0;
                    pa.chaves2[j] = 0;
                    pa.filhos[j+1] = -1;
                    pa.n--;
                    diminuiu = pa.n<maxElementos/2;  // testa se o pai também ficou sem o número mínimo de elementos
                }
            }
            
            // Faz a fusão com o irmão direito
            else {
                
                // Carrega o irmão
                paginaIrmao = pa.filhos[diminuido+1];
                pIrmao = new Pagina(ordem);
                arquivo.seek(paginaIrmao);
                arquivo.read(buffer);
                pIrmao.setBytes(buffer);
                
                // Testa se o irmão pode ceder algum elemento
                if(pIrmao.n>maxElementos/2) {
                    
                    // Se for folha
                    if( pFilho.filhos[0]==-1 ) {
                    
                        //copia o elemento do irmão
                        pFilho.chaves1[pFilho.n] = pIrmao.chaves1[0];
                        pFilho.chaves2[pFilho.n] = pIrmao.chaves2[0];
                        pFilho.filhos[pFilho.n+1] = pIrmao.filhos[0];
                        pFilho.n++;

                        // sobe o próximo elemento do irmão
                        pa.chaves1[diminuido] = pIrmao.chaves1[1];
                        pa.chaves2[diminuido] = pIrmao.chaves2[1];
                        
                    } 
                    
                    // Se não for folha, rotaciona os elementos
                    else {
                        
                        // Copia o elemento do pai, com o ponteiro esquerdo do irmão
                        pFilho.chaves1[pFilho.n] = pa.chaves1[diminuido];
                        pFilho.chaves2[pFilho.n] = pa.chaves2[diminuido];
                        pFilho.filhos[pFilho.n+1] = pIrmao.filhos[0];
                        pFilho.n++;
                        
                        // Sobe o elemento esquerdo do irmão para o pai
                        pa.chaves1[diminuido] = pIrmao.chaves1[0];
                        pa.chaves2[diminuido] = pIrmao.chaves2[0];
                    }
                    
                    // move todos os registros no irmão para a esquerda
                    int j;
                    for(j=0; j<pIrmao.n-1; j++) {
                        pIrmao.chaves1[j] = pIrmao.chaves1[j+1];
                        pIrmao.chaves2[j] = pIrmao.chaves2[j+1];
                        pIrmao.filhos[j] = pIrmao.filhos[j+1];
                    }
                    pIrmao.filhos[j] = pIrmao.filhos[j+1];
                    pIrmao.n--;
                    diminuiu = false;
                }
                
                // Se não puder ceder, faz a fusão dos dois irmãos
                else {

                    // Se a página reduzida não for folha, então o elemento 
                    // do pai deve ser copiado para o irmão
                    if(pFilho.filhos[0] != -1) {
                        pFilho.chaves1[pFilho.n] = pa.chaves1[diminuido];
                        pFilho.chaves2[pFilho.n] = pa.chaves2[diminuido];
                        pFilho.filhos[pFilho.n+1] = pIrmao.filhos[0];
                        pFilho.n++;
                    }
                    
                    // Copia todos os registros do irmão da direita
                    for(int j=0; j<pIrmao.n; j++) {
                        pFilho.chaves1[pFilho.n] = pIrmao.chaves1[j];
                        pFilho.chaves2[pFilho.n] = pIrmao.chaves2[j];
                        pFilho.filhos[pFilho.n+1] = pIrmao.filhos[j+1];
                        pFilho.n++;
                    }
                    pIrmao.n = 0;   // aqui o endereço do irmão poderia ser incluido em uma lista encadeada no cabeçalho, indicando os espaços reaproveitáveis
                    
                    // Se a página for folha, copia o ponteiro para a próxima página
                    pFilho.proxima = pIrmao.proxima;
                    
                    // puxa os registros no pai
                    for(int j=diminuido; j<pa.n-1; j++) {
                        pa.chaves1[j] = pa.chaves1[j+1];
                        pa.chaves2[j] = pa.chaves2[j+1];
                        pa.filhos[j+1] = pa.filhos[j+2];
                    }
                    pa.n--;
                    diminuiu = pa.n<maxElementos/2;  // testa se o pai também ficou sem o número mínimo de elementos
                }
            }
            
            // Atualiza todos os registros
            arquivo.seek(pagina);
            arquivo.write(pa.getBytes());
            arquivo.seek(paginaFilho);
            arquivo.write(pFilho.getBytes());
            arquivo.seek(paginaIrmao);
            arquivo.write(pIrmao.getBytes());
        }
        return excluido;
    }
    
    
    // Imprime a árvore, usando uma chamada recursiva.
    // A função recursiva é chamada com uma página de referência (raiz)
    public void print() throws IOException {
        long raiz;
        arquivo.seek(0);
        raiz = arquivo.readLong();
        if(raiz!=-1)
            print1(raiz);
        System.out.println();
    }
    
    // Impressão recursiva
    private void print1(long pagina) throws IOException {
        
        // Retorna das chamadas recursivas
        if(pagina==-1)
            return;
        int i;

        // Lê o registro da página passada como referência no arquivo
        arquivo.seek(pagina);
        Pagina pa = new Pagina(ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
        pa.setBytes(buffer);
        
        // Imprime a página
        String endereco = String.format("%04d", pagina);
        System.out.print(endereco+"  " + pa.n +":"); // endereço e número de elementos
        for(i=0; i<maxElementos; i++) {
            System.out.print("("+String.format("%04d",pa.filhos[i])+") "+String.format("%2d",pa.chaves1[i])+","+String.format("%2d",pa.chaves2[i])+" ");
        }
        System.out.print("("+String.format("%04d",pa.filhos[i])+")");
        if(pa.proxima==-1)
            System.out.println();
        else
            System.out.println(" --> ("+String.format("%04d", pa.proxima)+")");
        
        // Chama recursivamente cada filho, se a página não for folha
        if(pa.filhos[0] != -1) {
            for(i=0; i<pa.n; i++)
                print1(pa.filhos[i]);
            print1(pa.filhos[i]);
        }
    }
       
}
