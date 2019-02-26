package crud;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;

public class Produto{
    private short id;
    protected String nome;
    protected String descricao;
    protected float preco;

    public Produto(){
        this((short)-1, "", "", (float)0.0);
    }

    public Produto(String nome, String descricao, float preco) {
    	 this((short)-1, nome, descricao, preco);
    }
    
    public Produto(short id, String nome, String descricao, float preco) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
    }
    
    public short getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = (short)id;
    }

    public byte[] setByteArray() throws IOException{
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(array);
        
        try{
        	/*decidir nao colocar o id no array de bytes para facilicar na questão da busca*/
            //dataStream.writeShort(disco.getIdDisco());
            dataStream.writeUTF(this.nome);
            dataStream.writeUTF(this.descricao);
            dataStream.writeFloat(this.preco);
 
            dataStream.close();
            array.close();

        } catch(IOException e){
            e.printStackTrace();
        }

        return array.toByteArray();
    }
    
    public void fromByteArray(byte[] byteArray, short id){
        ByteArrayInputStream byteArrayStream = new ByteArrayInputStream(byteArray);
        DataInputStream dataStream = new DataInputStream(byteArrayStream);

        try{
            this.id = id;
            this.nome = dataStream.readUTF();
            this.descricao = dataStream.readUTF();
            this.preco = dataStream.readFloat();
        } catch(IOException e){
            e.printStackTrace();
        }
  
    }

    /*public void writeObject(DataOutputStream file) throws IOException{
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteArray);
      
        try{
            dataStream.writeUTF(this.nome);
            dataStream.writeUTF(this.artista);
            dataStream.writeShort(this.anoLancamento);
            dataStream.writeUTF(this.genero);
            dataStream.writeUTF(this.gravadora);

            file.writeInt(dataStream.size());
            file.write(byteArray.toByteArray());

            byteArray.close();
            dataStream.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public Disco readObject() throws IOException{
        Disco disco = new Disco();
        RandomAccessFile reader = new RandomAccessFile("musicas.db", "rw");

        return disco;
    }

    public Disco readObject(DataInputStream file, Disco disco) throws IOException{
        int tam = file.readInt();
        byte[] byteArray = new byte[tam];
        if (file.read(byteArray) != tam){
            System.out.println("Deu ruim");
        }
        ByteArrayInputStream byteArrayStream = new ByteArrayInputStream(byteArray);
        DataInputStream dataStream = new DataInputStream(byteArrayStream);

        try{
            disco.nome = dataStream.readUTF();
            disco.artista = dataStream.readUTF();
            disco.anoLancamento = dataStream.readShort();
            disco.genero = dataStream.readUTF();
            disco.gravadora = dataStream.readUTF();
            
            byteArrayStream.close();
            dataStream.close();
        } catch(IOException e){
            e.printStackTrace();
        }
        return disco;
    }*/

    /*
    public void writeObjectRandom() throws FileNotFoundException{
        try{
            RandomAccessFile writer = new RandomAccessFile("dadosRandom.db", "rw");
            writer.writeUTF(this.nome);
            writer.writeUTF(this.artista);
            writer.writeShort(this.anoLancamento);
            writer.writeUTF(this.genero);
            writer.writeUTF(this.gravadora);
            writer.write(this.byteArray);

            writer.close();
        } catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public Disco readObjectRandom(Disco disco2) throws FileNotFoundException{
        RandomAccessFile reader = new RandomAccessFile("dadosRandom.db", "rw");

        try{
            reader.readByte();
            disco2.nome = reader.readUTF();
            disco2.artista = reader.readUTF();
            disco2.anoLancamento = reader.readShort();
            disco2.genero = reader.readUTF();
            disco2.gravadora = reader.readUTF();

            reader.close();
        } catch(IOException e){
            e.printStackTrace();
        }

        return disco2;
    }*/

    public String toString(){
        return "ID: " + this.id + "Nome: " + this.nome + '\n' + "Descrição: " + this.descricao + '\n'
        		+ "Preço: " + this.preco + "\n";
    }
}

