# CRUD
**Repositório do CRUD de AEDs III**

O projeto traz a implementação de um sistema de gerenciamento para supermercados. Inclui produtos, categorias, compras, clientes, funcionários e um sistema de autenticação.

O projeto utiliza algumas estruturas de dados para indexamento de registros e é capaz de criar índices/conexões entre diferentes tipos de entidades.

Além disso, o gerenciamento da base de dados foi implementado incluindo algoritmos de criptografia e compactação que ajudam na segurança e manutenção dos dados.

O arquivo .jar disponibilizado no repositório é funcional e pode ser testado. Cabe um alerta: a base de dados é excluída a cada execução.

Relacionamento entre as entidades no banco de dados:

CATEGORIA       ->	PRODUTO         (1:N)

PRODUTO         ->	CATEGORIA       (1:1)

PRODUTO         ->	ITEM_COMPRADO   (1:N)

ITEM_COMPRADO   ->	PRODUTO         (1:1)

COMPRA          ->	ITEM_COMPRADO   (1:N)

ITEM_COMPRADO   ->	COMPRA          (1:1)

CLIENTE         ->	COMPRA          (1:N)

COMPRA          ->	CLIENTE         (1:1)
