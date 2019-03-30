package user;

import java.io.*;

import crud.Arquivo;
import crud.ArvoreBMais_ChaveComposta;
import entidades.Categoria;
import entidades.Produto;


public class Main {
	
	public static final String DATABASE_FILE_NAME_PRODUTOS = "produtos.db";
	public static final String INDEXES_FILE_NAME_PRODUTOS = "produtos.idx";
	public static final String DATABASE_FILE_NAME_CATEGORIAS = "categorias.db";
	public static final String INDEXES_FILE_NAME_CATEGORIAS = "categorias.idx";
	
	public static Arquivo<Produto> databaseProduto;
	public static Arquivo<Categoria> databaseCategoria;
	public static ArvoreBMais_ChaveComposta indiceComposto;
	
	public static final int COMPOSITE_TREE_ORDER = 21;
	public static final String COMPOSITE_INDEXES_FILE_NAME = "indice_composto.idx";
	
	public static CrudCategoria crudCategoria;
	public static CrudProduto crudProduto;
	
	public static void startFiles()
	{
		File databaseFileProdutos = new File(DATABASE_FILE_NAME_PRODUTOS);
		File indexesFileProdutos = new File(INDEXES_FILE_NAME_PRODUTOS);
		
		File databaseFileCategorias = new File(DATABASE_FILE_NAME_CATEGORIAS);
		File indexesFileCategorias = new File(INDEXES_FILE_NAME_CATEGORIAS);
		
		databaseFileProdutos.delete();
		indexesFileProdutos.delete();
		databaseFileCategorias.delete();
		indexesFileCategorias.delete();
	}
	
	public static void startVariables()
	{
		try {
			databaseProduto = new Arquivo<Produto>(
				Produto.class.getConstructor(),
				DATABASE_FILE_NAME_PRODUTOS,
				INDEXES_FILE_NAME_PRODUTOS
			);
			
			
			databaseCategoria = new Arquivo<Categoria>(
					Categoria.class.getConstructor(),
					DATABASE_FILE_NAME_CATEGORIAS,
					INDEXES_FILE_NAME_CATEGORIAS
				);
			
			indiceComposto = new ArvoreBMais_ChaveComposta(
				COMPOSITE_TREE_ORDER,
				COMPOSITE_INDEXES_FILE_NAME
			);
		}
		
		catch(NoSuchMethodException nsme) {
			nsme.printStackTrace();
		}
		
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {		
		startFiles();
		startVariables();
	
		Crud crudMaster = new Crud(databaseProduto, databaseCategoria);
		crudMaster.menu();
		
		
	}
}