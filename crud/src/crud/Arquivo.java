package crud;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.util.ArrayList;

//colocar no array list e fazer o index para buscar por nome 

public class Arquivo {
	private String name;
	public short lastID;
	RandomAccessFile accessFile;

	public Arquivo(String nameFile) {
		this.name = nameFile;
		this.lastID = -1;
	}

	public short getLastID() {
		return this.lastID;
	}

	public void setLastID(short lastID) {
		this.lastID = lastID;
	}
	
	public void writeObject(Produto produto) throws IOException {
		try {
			accessFile = new RandomAccessFile(this.name, "rw");
			
			
			produto.setId(getLastID() + 1);
			accessFile.writeShort(produto.getId());
			setLastID(produto.getId());
			
			byte[] byteArray = produto.setByteArray();

			// go to final of file
			accessFile.seek(accessFile.length());
			
			accessFile.writeShort(produto.getId()); // write id
			accessFile.writeChar(' '); //lapide
			accessFile.writeInt(byteArray.length);
			accessFile.write(byteArray);

			accessFile.close();
		} catch (FileNotFoundException fNFE) {
			fNFE.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * MÈtodo para listar todo o arquivo [EM CONSTRU«√O]
	 * @return ArrayList
	 * @throws IOException
	 */
	public ArrayList<Produto> list() throws IOException{
        ArrayList<Produto> listProdutos = new ArrayList<Produto>();
		Produto produto = new Produto();
        accessFile = new RandomAccessFile(this.name, "rw");

        int gap = 0;

       // byte[] byteArray = new byte[accessFile.readInt()]; // byteArray inutil por enquanto mas pretendo implement√°-lo

        accessFile.seek(2);

        while(accessFile.getFilePointer() < accessFile.length()){
            short id = accessFile.readShort(); 
            if(accessFile.readChar() != '*') {
            	 gap = accessFile.readInt();
                 byte[] byteArray = new byte[gap];
                 
                 produto.fromByteArray(byteArray, id);
                 listProdutos.add(produto);
            } else {
            	gap = accessFile.readInt();
            }
           
            System.out.println("Pos: " + accessFile.getFilePointer());
            accessFile.seek(accessFile.getFilePointer() + gap);
        }
       
        return listProdutos;
    }

	public Produto readObject(int id) {
		Produto produto = new Produto();
		int gap = 0;

		try {
			accessFile = new RandomAccessFile(this.name, "rw");

			accessFile.seek(2);

			while (accessFile.getFilePointer() < accessFile.length()) {
				short thisId = accessFile.readShort();
				if(accessFile.readChar() != '*' && thisId == id) {
					byte[] byteArray = new byte[accessFile.readInt()];
					accessFile.read(byteArray);

					produto.fromByteArray(byteArray, thisId);

					accessFile.seek(accessFile.length());
				} else {
					gap = accessFile.readInt();
					accessFile.seek(accessFile.getFilePointer() + gap);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return produto;
	}
	
	//estrutura do vetor de byte
	// ultimo id usado - [id] - [lapide] - [tamanho da entidade] -> entidade
}