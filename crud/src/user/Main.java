package user;

import crud.Produto;
import crud.Arquivo;

import java.io.File;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		File file = new File("produto.db");
		file.delete();
		
		Arquivo arquivo = new Arquivo("produtos.db");
		Produto produto = new Produto("Geladeira", "Geladeira Duplex FrostFree 30kg", (float)1200.00);
		
		arquivo.writeObject(produto);
	}
}
