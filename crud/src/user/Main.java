package user;

import crud.Arquivo;
import crud.hash_dinamica.implementacoes.HashDinamicaIntInt;
import entidades.*;
import serializaveis.SerializavelAbstract;
import util.Files;

public class Main {
	
	// sufixos de arquivos de base de dados, índices simples e índices compostos
	public static final String DATABASE_FILE_SUFFIX		= ".db";
	public static final String INDEX_FILE_SUFFIX		= ".idx";
	public static final String INDEX_DIR_FILE_SUFFIX	= ".dir";

	// prefixos de arquivos de entidades
	public static final String COMPRAS_FILE_NAME			= "compras";
	public static final String PRODUTOS_FILE_NAME			= "produtos";
	public static final String CLIENTES_FILE_NAME			= "clientes";
	public static final String CATEGORIAS_FILE_NAME			= "categorias";
	public static final String ITENS_COMPRADOS_FILE_NAME	= "itensComprados";
	
	// prefixos de arquivos de índices compostos
	public static final String CLIENTE_COMPRA_FILE_NAME			= "clienteCompra";
	public static final String CATEGORIA_PRODUTO_FILE_NAME		= "categoriaProduto";
	public static final String COMPRA_ITEM_VENDIDO_FILE_NAME	= "compraItemComprado";
	public static final String PRODUTO_ITEM_VENDIDO_FILE_NAME	= "produtoItemComprado";

	// objetos para o gerenciamento dos registros de cada entidade
	public static Arquivo<Compra>		databaseCompra;
	public static Arquivo<Produto>		databaseProduto;
	public static Arquivo<Cliente>		databaseCliente;
	public static Arquivo<Categoria>	databaseCategoria;
	public static Arquivo<ItemComprado>	databaseItemComprado;
	
	// objetos para o gerenciamento dos índices compostos
	public static HashDinamicaIntInt indiceClienteCompra;
	public static HashDinamicaIntInt indiceCategoriaProduto;
	public static HashDinamicaIntInt indiceCompraItemVendido;
	public static HashDinamicaIntInt indiceProdutoItemVendido;

	// objetos para gerenciamento de menus e interligação com os
	// objetos que gerenciam os registros de cada entidade
	public static CrudCompra crudCompra;
	public static CrudProduto crudProduto;
	public static CrudCliente crudCliente;
	public static CrudCategoria crudCategoria;
	public static CrudItemComprado crudItemComprado;
	
	private static boolean deleteDatabaseFile(String fileName)
	{
		return Files.deleteSuffixedFile(fileName, DATABASE_FILE_SUFFIX);
	}
	
	private static void deleteDatabaseFiles()
	{
		deleteDatabaseFile(COMPRAS_FILE_NAME);
		deleteDatabaseFile(PRODUTOS_FILE_NAME);
		deleteDatabaseFile(CLIENTES_FILE_NAME);
		deleteDatabaseFile(CATEGORIAS_FILE_NAME);
		deleteDatabaseFile(ITENS_COMPRADOS_FILE_NAME);
	}
	
	private static boolean deleteIndexFiles(String fileName)
	{
		// como a hash dinâmica gera um arquivo para o diretório e um
		// arquivo para os buckets, é necessário deletar ambos
		return Files.deleteSuffixedFile(fileName, INDEX_FILE_SUFFIX) &&
			Files.deleteSuffixedFile(fileName, INDEX_DIR_FILE_SUFFIX);
	}
	
	private static void deleteIndexFiles()
	{
		// índices simples
		deleteIndexFiles(COMPRAS_FILE_NAME);
		deleteIndexFiles(PRODUTOS_FILE_NAME);
		deleteIndexFiles(CLIENTES_FILE_NAME);
		deleteIndexFiles(CATEGORIAS_FILE_NAME);
		deleteIndexFiles(ITENS_COMPRADOS_FILE_NAME);
		
		// índices compostos
		deleteIndexFiles(CLIENTE_COMPRA_FILE_NAME);
		deleteIndexFiles(CATEGORIA_PRODUTO_FILE_NAME);
		deleteIndexFiles(COMPRA_ITEM_VENDIDO_FILE_NAME);
		deleteIndexFiles(PRODUTO_ITEM_VENDIDO_FILE_NAME);
	}
	
	private static void deleteFiles()
	{
		deleteDatabaseFiles();
		deleteIndexFiles();
	}
	
	private static <T extends SerializavelAbstract & Entidade> Arquivo<T>
	startDatabase(Class<T> dbClass, String fileName)
	{
		Arquivo<T> db = null;
		
		try
		{
			db = new Arquivo<T>(
				dbClass.getConstructor(),
				fileName + DATABASE_FILE_SUFFIX,
				fileName + INDEX_DIR_FILE_SUFFIX,
				fileName + INDEX_FILE_SUFFIX
			);
		}
		
		catch (NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}
		
		return db;
	}
	
	private static void startDatabases()
	{
		databaseCompra		= startDatabase(Compra.class		, COMPRAS_FILE_NAME);
		databaseProduto		= startDatabase(Produto.class		, PRODUTOS_FILE_NAME);
		databaseCliente		= startDatabase(Cliente.class		, CLIENTES_FILE_NAME);
		databaseCategoria	= startDatabase(Categoria.class		, CATEGORIAS_FILE_NAME);
		databaseItemComprado = startDatabase(ItemComprado.class	, ITENS_COMPRADOS_FILE_NAME);
	}
	
	private static HashDinamicaIntInt startCompositeIndex(String fileName)
	{
		HashDinamicaIntInt compositeIndex = null;
		
		try
		{
			compositeIndex = new HashDinamicaIntInt(
				fileName + INDEX_DIR_FILE_SUFFIX,
				fileName + INDEX_FILE_SUFFIX
			);
		}
		
		catch (NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}
		
		return compositeIndex;
	}
	
	private static void startCompositeIndexes()
	{
		indiceClienteCompra = startCompositeIndex(CLIENTE_COMPRA_FILE_NAME);
		indiceCategoriaProduto = startCompositeIndex(CATEGORIA_PRODUTO_FILE_NAME);
		indiceCompraItemVendido = startCompositeIndex(COMPRA_ITEM_VENDIDO_FILE_NAME);
		indiceProdutoItemVendido = startCompositeIndex(PRODUTO_ITEM_VENDIDO_FILE_NAME);
	}
	
	private static void startVariables()
	{
		startDatabases();
		startCompositeIndexes();
	}
	
	private static boolean closeFiles()
	{
		return
			databaseCompra.close() &&
			databaseCliente.close() &&
			databaseProduto.close() &&
			databaseCategoria.close() &&
			databaseItemComprado.close() &&
			
			indiceClienteCompra.fechar() &&
			indiceCategoriaProduto.fechar() &&
			indiceCompraItemVendido.fechar() &&
			indiceProdutoItemVendido.fechar();
	}
	
	public static void main(String[] args) {
		
		deleteFiles();
		startVariables();
	
		Crud crudMaster = new Crud(
			databaseCompra,
			databaseProduto,
			databaseCliente,
			databaseCategoria,
			databaseItemComprado
		);
		
		crudMaster.menu();
		
		closeFiles();
	}
}