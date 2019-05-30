package compression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import util.MyArray;

public class LZW
{
	private byte[] currentBytes;
	private ByteArrayInputStream inputStream;
	private ByteArrayOutputStream currentWord;
	private ByteArrayOutputStream outputStream;
	
	private static final ArrayList<byte[]> dictionary = new ArrayList<byte[]>();
	{
		for (byte i = 0; i < 255; i++) // muito cuidado com a condição de parada do for
		{
			dictionary.add( new byte[] { i } );
		}
		
		dictionary.add( new byte[] { (byte) 255 } );
	}
	
	private void clearDictionary()
	{
		int size = dictionary.size();
		
		for (int i = 256, j = 1; i < size; i++, j++)
		{
			dictionary.remove(size - j); // vai removendo os últimos elementos
		}
	}
	
	private int writeAndGetIndexOnDictionary(int byteRead)
	{
		int index = -1;
		
		if (byteRead != -1)
		{
			currentWord.write(byteRead); // escreve o byte lido na palavra atual
			currentBytes = currentWord.toByteArray(); // pegue o array de bytes da palavra atual
			
			// procura pela primeiro item do dicionário que seja igual ao array de bytes da palavra atual
			index = MyArray.first(dictionary, (it) -> Arrays.equals(currentBytes, it));
		}
		
		return index;
	}
	
	public byte[] encode(byte[] array)
	{
		clearDictionary();
		
		ByteBuffer shortBuffer = ByteBuffer.allocate(2);
		outputStream = new ByteArrayOutputStream(array.length);
		currentWord = new ByteArrayOutputStream();
		inputStream = new ByteArrayInputStream(array);
		int currentByte = inputStream.read();
		int lastIndexOnDictionary;
		int currentIndexOnDictionary;
		
		if (currentByte != -1)
		{
			// concatena o byte atual à corrente de saída e testa
			// se os bytes dela correspondem a algum item do dicionário
			currentIndexOnDictionary = writeAndGetIndexOnDictionary(currentByte);
			lastIndexOnDictionary = -1;
			
			while (currentIndexOnDictionary != -1)
			{
				lastIndexOnDictionary = currentIndexOnDictionary;
				currentByte = inputStream.read();
				currentIndexOnDictionary = writeAndGetIndexOnDictionary(currentByte);
			}

			dictionary.add(currentBytes);
			shortBuffer.putShort((short) lastIndexOnDictionary);
			
			try
			{
				outputStream.write( shortBuffer.array() );
			}
			
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			currentWord.reset(); // reseta o contador de bytes escritos (remoção lógica)
			currentWord.write(currentByte);
		}
		
		return outputStream.toByteArray();
	}
}
