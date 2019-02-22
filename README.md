# crud
**Repositório do CRUD  de AEDS3**

Criar o seu primeiro CRUD (Create, Read, Update, Delete) em um arquivo.

Vocês devem implementar um sistema  para fazer inclusão, alteração, exclusão e busca de produtos em um arquivo indexado. Os atributos básicos dessa entidade são:

**idProduto
Nome do Produto
Descrição
Preço**

Acrescente pelo menos mais dois atributos que o seu grupo ache interessante. 

Como índice, vocês podem usar uma tabela hash extensível (recomendada) ou uma árvore B+.

**ROTEIRO PARA IMPLEMENTAÇÃO**
- Criem um método principal que faça o papel de um menu. Esse menu deve ter as 4 operações obrigatórias: inclusão, alteração, exclusão e consulta de produtos. Não se preocupem com o uso de interfaces gráficas. Esse método principal também pode ser o responsável por abrir os arquivos de dados e índice, que serão mantidos abertos durante toda a execução do sistema.

- Criem uma classe Produto. Essa classe deve ter pelo menos um construtor e métodos para a criação de representações em byte dos dados do produto. Esses métodos serão usados na leitura e na escrita dos objetos nos arquivos, considerando que os arquivos são fluxos de bytes. Eles podem ser nomeados como toByteArray() e fromByteArray().

- Criar o método de inclusão, que deve ter a seguinte sequência de passos:
  - [ ] Ler os dados do novo produto
  - [ ] Confirmar a inclusão do produto
  - [ ] Ler o último ID usado no cabeçalho do arquivo
  - [ ] Incrementar 1 a esse valor, gerando um novo ID. Atualizar o cabeçalho
  - [ ] Criar um novo objeto Produto com os dados digitados e o novo ID gerado
  - [ ] Criar um novo vetor de bytes com os dados do produto (usando o método toByteArray() do produto)
  - [ ] Mover o ponteiro para o fim do arquivo
  - [ ] Armazenar esse endereço 
  - [ ] Escrever o campo lápide vazio
  - [ ] Escrever o tamanho do vetor de bytes (usando o método writeInt())
  - [ ] Escrever o próprio vetor de bytes
  - [ ] Criar uma nova entrada no índice, passando o ID do produto e o seu endereço

- Criar o método de busca por ID, usando a seguinte sequência de passos:
  - [ ] Ler o ID do produto desejado
  - [ ] Buscar o endereço do produto no índice, passando o ID
  - [ ] Localizar, no arquivo de produtos, o endereço retornado
  - [ ] Ler o lápide
  - [ ] Ler o tamanho do vetor de bytes
  - [ ] Ler o vetor de bytes
  - [ ] Se o campo lápide não estiver marcado (registro excluído), criar um novo objeto Produto vazio e carregar o vetor de bytes usando   o   seu método fromByteArray()
  - [ ] Ainda se não for um registro excluído, escrever os dados
  - [ ] Retornar

- Criar o método de exclusão, da seguinte forma:
  - [ ] Repetir todos os passos de (a) a (h) da busca acima
  - [ ] Solicitar a confirmação de exclusão do usuário
  - [ ] Se confirmado, retornar ao início do registro (endereço retornado pelo índice) e marcar o campo lápide (por exemplo, escrevendo um   * nesse campo)
  - [ ] Excluir o produto do índice por meio do seu ID

- Criar o método de alteração, por meio da combinação de passos dos métodos acima:
  - [ ] Usar os passos da exclusão para localizar e apresentar o produto, mas, ao invés de apenas confirmar a exclusão, fazer a leitura dos   novos dados do produto
  - [ ] Caso a alteração seja confirmada pelo usuário, marcar esse produto como excluído (por meio do campo lápide) e, usando os mesmos passos da inclusão, inserir o "novo" produto do fim do arquivo
  - [ ]Atualizar o endereço do produto no índice







