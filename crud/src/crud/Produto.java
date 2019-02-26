package crud;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.RandomAccessFile;
import java.util.Random;

public class Produto{
    private short id;
    protected String nome;
    protected String descricao;
    protected float preço;

    public Disco(){
        this(-1, "", "", 0.0);
    }

    public Disco(String nome, String artista, short anoLancamento, String genero, String gravadora) {
        this.idDisco = -1;
        this.nome = nome;
        this.artista = artista;
        this.anoLancamento = anoLancamento;
        this.genero = genero;
        this.gravadora = gravadora;
    }
    
    public short getIdDisco(){
        return this.idDisco;
    }
    public void setIdDisco(int idDisco){
        this.idDisco = (short)idDisco;
    }

    public byte[] setByteArray() throws IOException{
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(array);
        
        try{
            //dataStream.writeShort(disco.getIdDisco());
            dataStream.writeUTF(this.nome);
            dataStream.writeUTF(this.artista);
            dataStream.writeShort(this.anoLancamento);
            dataStream.writeUTF(this.genero);
            dataStream.writeUTF(this.gravadora);

            dataStream.close();
            array.close();

        } catch(IOException e){
            e.printStackTrace();
        }

        return array.toByteArray();
    }
    
    public void fromByteArray(byte[] byteArray, short idDisco){
        ByteArrayInputStream byteArrayStream = new ByteArrayInputStream(byteArray);
        DataInputStream dataStream = new DataInputStream(byteArrayStream);

        try{
            this.idDisco = idDisco;
            this.nome = dataStream.readUTF();
            this.artista = dataStream.readUTF();
            this.anoLancamento = dataStream.readShort();
            this.genero = dataStream.readUTF();
            this.gravadora = dataStream.readUTF();
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
        return "ID: " + this.idDisco + "Disco: " + this.nome + '\n' + "Artista: " + this.artista + '\n'
            + "Ano de lançamento: " + this.anoLancamento + '\n' + "Genero: " + this.genero + '\n' + "Gravadora: " + this.gravadora;
    }
}

