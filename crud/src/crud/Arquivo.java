package crud;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

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
			accessFile.writeInt(byteArray.length);
			accessFile.write(byteArray);

			accessFile.close();
		} catch (FileNotFoundException fNFE) {
			fNFE.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Produto readObject() throws IOException{
        Produto produto = new Produto();
        accessFile = new RandomAccessFile(this.name, "rw");

        int gap = 0;

       // byte[] byteArray = new byte[accessFile.readInt()]; // byteArray inutil por enquanto mas pretendo implementá-lo

        accessFile.seek(2);

        while(accessFile.getFilePointer() < accessFile.length()){
            produto.setId(accessFile.readShort()); 
            gap = accessFile.readInt(); //usar essa informação para criar o vetor de bytes
            //System.out.println("Gap: " + gap);
            produto.nome = accessFile.readUTF();
            produto.descricao = accessFile.readUTF();
            produto.preco = accessFile.readFloat();

            //System.out.println("Pos: " + accessFile.getFilePointer());
            //accessFile.seek(accessFile.getFilePointer() + gap);
          
        }
       

        return produto;
    }

	public Produto readObject(int id) {
		Produto produto = new Produto();
		int gap = 0;

		try {
			accessFile = new RandomAccessFile(this.name, "rw");

			accessFile.seek(2);

			while (accessFile.getFilePointer() < accessFile.length()) {
				short thisId = accessFile.readShort();
				if (thisId == (short) id) {
					byte[] byteArray = new byte[accessFile.readInt()];
					accessFile.read(byteArray);

					produto.fromByteArray(byteArray, thisId);

					accessFile.seek(accessFile.length());
				} else {
					System.out.println("Pula");
					System.out.println("Pos: " + accessFile.getFilePointer());
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
	// ultimo id usado - [tamanho da entidade]entidade - [id - nome+tamanho etc..]
}