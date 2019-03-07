package user;

import java.io.*;


public class Main {
	
	public static final String DATABASE_FILE_NAME = "produtos.db";
	public static final String INDEXES_FILE_NAME = "indexes";
	
	public static void main(String[] args) throws IOException {
		File file = new File(DATABASE_FILE_NAME);
		File file1 = new File(INDEXES_FILE_NAME);
		
		file.delete();
		file1.delete();
		
		Crud.menu();
	}
}