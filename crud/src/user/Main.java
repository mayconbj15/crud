package user;

import crud.Produto;
import crud.Arquivo;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

public class Main {
	
	public static void print(Object msg)
	{
		System.out.print(msg);
	}
	
	public static void println(Object msg)
	{
		print(msg + System.lineSeparator());
	}
	
	public static void main(String[] args) throws IOException {
		File file = new File("produto.db");
		File file1 = new File("indexes");
		
		file.delete();
		file1.delete();
		
		ArrayList<Produto> list = new ArrayList<Produto>();
		
		Arquivo arquivo = new Arquivo("produtos.db");
		Produto produto = new Produto("Geladeira", "Geladeira Duplex FrostFree 30kg", (float)1200.00);
		Produto produto1 = new Produto("Ps4", "500gb HD", (float)1800.00);
		Produto produto2 = new Produto("TV", "4k Full HD Master", (float)1800.00);
		
		arquivo.writeObject(produto);
		arquivo.writeObject(produto1);
		arquivo.writeObject(produto2);
		
		short searchedID = 1;
		println("Procurando entidade com id = " + searchedID);
		println(arquivo.readObject(searchedID));
		
		println("Removendo entidade com id = " + searchedID);
		arquivo.deleteObject(searchedID);
		

		println("Listando bando de dados");

		int alterar = 7;
		println("Alterando entidade com id = " + alterar);
		arquivo.changeObject(alterar);
		
		list = arquivo.list();

		
		list = arquivo.list();
		println(list.size());
		for(int i=0; i<list.size(); i++) {
			println(list.get(i));
		}
	}
}
