package user;

import java.io.*;

import crud.Produto;


public class Main {
	
	public static final String DATABASE_FILE_NAME = "produtos.db";
	public static final String INDEXES_FILE_NAME = "indexes";
	
	public static void main(String[] args) throws IOException {
		File file = new File(DATABASE_FILE_NAME);
		File file1 = new File(INDEXES_FILE_NAME);
		
		file.delete();
		file1.delete();
		
		Produto produtos = new Produto();
		
		try {
			Crud<Produto> crudProduto = new Crud<Produto>("crudProdutos.db", produtos.getClass().getDeclaredConstructor());
			crudProduto.menu(produtos);
		}
		catch(NoSuchMethodException nsme) {
			nsme.printStackTrace();
		}
	}
}