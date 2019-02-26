package user;

import crud.Produto;
import crud.Arquivo;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

public class Main {
	public static void main(String[] args) throws IOException {
		File file = new File("produto.db");
		file.delete();
		
		ArrayList<Produto> list = new ArrayList<Produto>();
		
		Arquivo arquivo = new Arquivo("produtos.db");
		Produto produto = new Produto("Geladeira", "Geladeira Duplex FrostFree 30kg", (float)1200.00);
		Produto produto1 = new Produto("Ps4", "500gb HD", (float)1800.00);
		Produto produto2 = new Produto("TV", "4k Full HD Master", (float)1800.00);
		
		arquivo.writeObject(produto);
		arquivo.writeObject(produto1);
		arquivo.writeObject(produto2);
		
		System.out.println(arquivo.readObject(1));
		
		list = arquivo.list();
		
		for(int i=0; i<list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
}
