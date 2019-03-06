package user;

import util.*;
import java.io.*;

//import crud.Produto;
//import crud.Arquivo;

public class Main {
    
    public static final String DATABASE_FILE_NAME = "produtos.db";
    public static final String INDEXES_FILE_NAME = "indexes";
    
    public static void main(String[] args) throws IOException {
        File file = new File(DATABASE_FILE_NAME);
        File file1 = new File(INDEXES_FILE_NAME);
        
        file.delete();
        file1.delete();
        
        Crud.menu();
        
        /*ArrayList<Produto> list = new ArrayList<Produto>();
        
        Arquivo arquivo = new Arquivo("produtos.db");
        Produto produto = new Produto("Geladeira", "Geladeira Duplex FrostFree 30kg", (float)1200.00);
        Produto produto1 = new Produto("Ps4", "500gb HD", (float)1800.00);
        Produto produto2 = new Produto("TV", "4k Full HD Master", (float)1800.00);
        
        arquivo.writeObject(produto);
        arquivo.writeObject(produto1);
        arquivo.writeObject(produto2);
        
        short searchedID = 1;
        IO.println("Procurando entidade com id = " + searchedID);
        IO.println(arquivo.readObject(searchedID));
        
        IO.println("Removendo entidade com id = " + searchedID);
        arquivo.deleteObject(searchedID);
        

        IO.println("Listando bando de dados");

        int alterar = 7;
        IO.println("Alterando entidade com id = " + alterar);
        arquivo.changeObject(alterar);
        
        list = arquivo.list();

        
        list = arquivo.list();
        IO.println(list.size());
        for(int i=0; i<list.size(); i++) {
            IO.println(list.get(i));
        }*/
    }
}