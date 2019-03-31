package user;

import java.io.*;

import crud.Arquivo;
import crud.hash_dinamica.implementacoes.HashDinamicaIntInt;
import entidades.Categoria;
import entidades.Produto;

public class Main {
	
	public static final String PRODUTOS_DATABASE_FILE_NAME = "produtos.db";
	public static final String PRODUTOS_INDEXES_FILE_NAME = "produtos.idx";
	public static final String PRODUTOS_INDEXES_DIR_FILE_NAME = "produtos.dir";
	
	public static final String CATEGORIAS_DATABASE_FILE_NAME = "categorias.db";
	public static final String CATEGORIAS_INDEXES_FILE_NAME = "categorias.idx";
	public static final String CATEGORIAS_INDEXES_DIR_FILE_NAME = "produtos.dir";
	
	public static Arquivo<Produto> databaseProduto;
	public static Arquivo<Categoria> databaseCategoria;
	public static HashDinamicaIntInt indiceComposto;
	
	public static final String COMPOSITE_INDEXES_FILE_NAME = "indice_composto.idx";
	public static final String COMPOSITE_INDEXES_DIR_FILE_NAME = "indice_composto.dir";
	
	public static CrudCategoria crudCategoria;
	public static CrudProduto crudProduto;
	
	public static void startFiles()
	{
		File produtosDatabaseFile = new File(PRODUTOS_DATABASE_FILE_NAME);
		File produtosIndexesFile = new File(PRODUTOS_INDEXES_FILE_NAME);
		File produtosIndexesDirFile = new File(PRODUTOS_INDEXES_DIR_FILE_NAME);
		
		File categoriasDatabaseFile = new File(CATEGORIAS_DATABASE_FILE_NAME);
		File categoriasIndexesFile = new File(CATEGORIAS_INDEXES_FILE_NAME);
		File categoriasIndexesDirFile = new File(CATEGORIAS_INDEXES_DIR_FILE_NAME);
		
		produtosDatabaseFile.delete();
		produtosIndexesFile.delete();
		produtosIndexesDirFile.delete();
		
		categoriasDatabaseFile.delete();
		categoriasIndexesFile.delete();
		categoriasIndexesDirFile.delete();
	}
	
	public static void startVariables()
	{
		try {
			databaseProduto = new Arquivo<Produto>(
				Produto.class.getConstructor(),
				PRODUTOS_DATABASE_FILE_NAME,
				PRODUTOS_INDEXES_DIR_FILE_NAME,
				PRODUTOS_INDEXES_FILE_NAME
			);
			
			databaseCategoria = new Arquivo<Categoria>(
				Categoria.class.getConstructor(),
				CATEGORIAS_DATABASE_FILE_NAME,
				CATEGORIAS_INDEXES_FILE_NAME,
				CATEGORIAS_INDEXES_DIR_FILE_NAME
			);
			
			indiceComposto = new HashDinamicaIntInt(
				COMPOSITE_INDEXES_DIR_FILE_NAME,
				COMPOSITE_INDEXES_FILE_NAME
			);
		}
		
		catch(NoSuchMethodException nsme) {
			nsme.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {		
		startFiles();
		startVariables();
	
		Crud crudMaster = new Crud(databaseProduto, databaseCategoria);
		crudMaster.menu();
	}
}