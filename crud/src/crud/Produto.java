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

    public String toString(){
        return "ID: " + this.id + '\n' + "Nome: " + this.nome + '\n' + "Descrição: " + this.descricao + '\n'
        		+ "Preço: " + this.preco + "\n";
    }
}

