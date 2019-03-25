package user;

import java.io.*;

import entidades.Produto;


public class Main {
	
	public static final String DATABASE_FILE_NAME_PRODUTOS = "produtos.db";
	public static final String INDEXES_FILE_NAME_PRODUTOS = "produtos.idx";
	public static final String DATABASE_FILE_NAME_CATEGORIAS = "categorias.db";
	public static final String INDEXES_FILE_NAME_CATEGORIAS = "produtos.idx";
	
	public static void main(String[] args) throws IOException {
		File databaseFileProdutos = new File(DATABASE_FILE_NAME_PRODUTOS);
		File indexesFileProdutos = new File(INDEXES_FILE_NAME_PRODUTOS);
		
		File databaseFileCategorias = new File(DATABASE_FILE_NAME_CATEGORIAS);
		File indexesFileCategorias = new File(INDEXES_FILE_NAME_CATEGORIAS);
		
		databaseFileProdutos.delete();
		indexesFileProdutos.delete();
		databaseFileCategorias.delete();
		indexesFileCategorias.delete();
		
		Produto produtos = new Produto();
		Categoria categoria = new Categoria();
		
		try {
			Crud<Produto> crudProduto = new Crud<>("crudProdutos.db", produtos.getClass().getDeclaredConstructor());
			crudProduto.menu(produtos);
		}
		catch(NoSuchMethodException nsme) {
			nsme.printStackTrace();
		}
	}
}