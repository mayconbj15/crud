package user;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import crud.Arquivo;
import crud.hash_dinamica.implementacoes.HashDinamicaIntInt;
import crud.hash_dinamica.implementacoes.HashDinamicaStringInt;
import entidades.*;
import serializaveis.SerializavelAbstract;
import util.Files;
import util.IO;

public class Main {
	
	// sufixos de arquivos de base de dados, índices simples e índices compostos
	public static final String DATABASE_FILE_SUFFIX		= ".db";
	public static final String INDEX_FILE_SUFFIX		= ".idx";
	public static final String INDEX_DIR_FILE_SUFFIX	= ".dir";
	
	// diretórios das entidades e dos índices compostos
	public static final String ENTITIES_FOLDER			= "Entities\\";
	public static final String COMPOSITE_INDEXES_FOLDER	= "CompositeIndexes\\";

	// prefixos de arquivos de entidades
	public static final String COMPRAS_FILE_NAME			= "compras";
	public static final String PRODUTOS_FILE_NAME			= "produtos";
	public static final String CLIENTES_FILE_NAME			= "clientes";
	public static final String CATEGORIAS_FILE_NAME			= "categorias";
	public static final String FUNCIONARIOS_FILE_NAME		= "funcionarios";
	public static final String ITENS_COMPRADOS_FILE_NAME	= "itensComprados";
	
	// prefixos de arquivos de índices compostos
	public static final String CLIENTE_COMPRA_FILE_NAME				= "clienteCompra";
	public static final String CATEGORIA_PRODUTO_FILE_NAME			= "categoriaProduto";
	public static final String COMPRA_ITEM_COMPRADO_FILE_NAME		= "compraItemComprado";
	public static final String PRODUTO_ITEM_COMPRADO_FILE_NAME		= "produtoItemComprado";
	public static final String NOME_CLIENTE_ID_CLIENTE_FILE_NAME	= "nomeClienteIdCliente";

	// objetos para o gerenciamento dos registros de cada entidade
	public static Arquivo<Compra>		databaseCompra;
	public static Arquivo<Produto>		databaseProduto;
	public static Arquivo<Cliente>		databaseCliente;
	public static Arquivo<Categoria>	databaseCategoria;
	public static Arquivo<Funcionario>	databaseFuncionario;
	public static Arquivo<ItemComprado>	databaseItemComprado;
	
	// objetos para o gerenciamento dos índices compostos
	public static HashDinamicaIntInt 	indiceClienteCompra;
	public static HashDinamicaIntInt 	indiceCategoriaProduto;
	public static HashDinamicaIntInt 	indiceCompraItemComprado;
	public static HashDinamicaIntInt 	indiceProdutoItemComprado;
	public static HashDinamicaStringInt indiceEmailUsuarioIdUsuario;

	// objetos para gerenciamento de menus e interligação com os
	// objetos que gerenciam os registros de cada entidade
	public static CrudCompra crudCompra;
	public static CrudProduto crudProduto;
	public static CrudCliente crudCliente;
	public static CrudCategoria crudCategoria;
	public static CrudFuncionario crudFuncionario;
	public static CrudItemComprado crudItemComprado;
	
	private static String getEntityFolderPath(String entityName)
	{
		return ENTITIES_FOLDER + entityName + "\\";
	}
	
	private static <T extends SerializavelAbstract & Entidade> Arquivo<T>
	startDatabase(Class<T> dbClass, String fileName)
	{
		Arquivo<T> db = null;
		String folderPath = getEntityFolderPath(fileName);
		
		try
		{
			db = new Arquivo<T>(
				dbClass.getConstructor(),
				folderPath + fileName + DATABASE_FILE_SUFFIX,
				folderPath + fileName + INDEX_DIR_FILE_SUFFIX,
				folderPath + fileName + INDEX_FILE_SUFFIX
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
		databaseFuncionario	= startDatabase(Funcionario.class	, FUNCIONARIOS_FILE_NAME);
		databaseItemComprado = startDatabase(ItemComprado.class	, ITENS_COMPRADOS_FILE_NAME);
	}
	
	private static String getCompositeIndexFolderPath(String fileName)
	{
		return COMPOSITE_INDEXES_FOLDER;
	}
	
	private static <T> T startCompositeIndex(
		String fileName,
		Constructor<T> compositeIndexConstructor)
	{
		T compositeIndex = null;
		String folderPath = getCompositeIndexFolderPath(fileName);
		
		try
		{
			compositeIndex = compositeIndexConstructor.newInstance(
				folderPath + fileName + INDEX_DIR_FILE_SUFFIX,
				folderPath + fileName + INDEX_FILE_SUFFIX
			);
		}
		
		catch (InstantiationException |
			IllegalAccessException |
			IllegalArgumentException |
			InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
		return compositeIndex;
	}
	
	private static HashDinamicaStringInt startCompositeIndexStringInt(String fileName)
	{
		HashDinamicaStringInt compositeIndex = null;
		
		try
		{
			compositeIndex =
				startCompositeIndex(
					fileName,
					HashDinamicaStringInt.class.getConstructor(
						String.class,
						String.class
					)
				);
		}
		
		catch (NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}
		
		return compositeIndex;
	}
	
	private static HashDinamicaIntInt startCompositeIndexIntInt(String fileName)
	{
		HashDinamicaIntInt compositeIndex = null;
		
		try
		{
			compositeIndex =
				startCompositeIndex(
					fileName,
					HashDinamicaIntInt.class.getConstructor(
						String.class,
						String.class
					)
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
		indiceClienteCompra = startCompositeIndexIntInt(CLIENTE_COMPRA_FILE_NAME);
		indiceCategoriaProduto = startCompositeIndexIntInt(CATEGORIA_PRODUTO_FILE_NAME);
		indiceCompraItemComprado = startCompositeIndexIntInt(COMPRA_ITEM_COMPRADO_FILE_NAME);
		indiceProdutoItemComprado = startCompositeIndexIntInt(PRODUTO_ITEM_COMPRADO_FILE_NAME);
		indiceEmailUsuarioIdUsuario = startCompositeIndexStringInt(NOME_CLIENTE_ID_CLIENTE_FILE_NAME);
	}
	
	private static void startCRUDs()
	{
		crudCompra = new CrudCompra(databaseCompra);
		crudCliente = new CrudCliente(databaseCliente);
		crudProduto = new CrudProduto(databaseProduto);
		crudCategoria = new CrudCategoria(databaseCategoria);
		crudFuncionario = new CrudFuncionario(databaseFuncionario);
		crudItemComprado = new CrudItemComprado(databaseItemComprado);
	}
	
	private static void startVariables()
	{
		startDatabases();
		startCompositeIndexes();
		startCRUDs();
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
			indiceCompraItemComprado.fechar() &&
			indiceProdutoItemComprado.fechar() &&
			indiceEmailUsuarioIdUsuario.fechar();
	}
	
	public static void createEntitiesFolders()
	{
		Files.createFolders(
			ENTITIES_FOLDER + COMPRAS_FILE_NAME,
			ENTITIES_FOLDER + CLIENTES_FILE_NAME,
			ENTITIES_FOLDER + PRODUTOS_FILE_NAME,
			ENTITIES_FOLDER + CATEGORIAS_FILE_NAME,
			ENTITIES_FOLDER + FUNCIONARIOS_FILE_NAME,
			ENTITIES_FOLDER + ITENS_COMPRADOS_FILE_NAME
		);
	}
	
	public static void createCompositeIndexesFolder()
	{
		Files.createFolders(COMPOSITE_INDEXES_FOLDER);
	}
	
	public static void createFolders()
	{
		createEntitiesFolders();
		createCompositeIndexesFolder();
	}
	
	private static void deleteFiles()
	{
		Files.delete(ENTITIES_FOLDER);
		Files.delete(COMPOSITE_INDEXES_FOLDER);
	}
	
	public static void main(String[] args) {
		deleteFiles();
		createFolders();
		startVariables();
		
		// email da conta padrao para funcionario: F@F
		// senha da conta padrao: f
		
		// email da conta padrao para funcionario: C@C
		// senha da conta padrao: c
		Crud.menu();
		
		closeFiles();
	}
}