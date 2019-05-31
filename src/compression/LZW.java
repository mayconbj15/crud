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
	private static final byte[] ZERO = new byte[] { (byte) 0, (byte) 0 };
	
	private byte[] currentBytes;
	private ByteBuffer shortBuffer;
	private ByteArrayInputStream inputStream;
	private ByteArrayOutputStream currentWord;
	private ByteArrayOutputStream outputStream;
	private int lastIndexOnDictionary;
	private int currentIndexOnDictionary;
	
	private static final ArrayList<byte[]> dictionary = new ArrayList<byte[]>();
	{
		for (byte i = 0; i != -1; i++) // muito cuidado com a condição de parada do for
		{
			dictionary.add( new byte[] { i } );
		}
		
		dictionary.add( new byte[] { (byte) -1 } );
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
		
		ByteBuffer shortBuffer = ByteBuffer.allocate(Short.BYTES);
		outputStream = new ByteArrayOutputStream(array.length * Short.BYTES);
		currentWord = new ByteArrayOutputStream();
		inputStream = new ByteArrayInputStream(array);
		int currentByte = inputStream.read();
		
		for (int i = 0; i < array.length; i++)
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
				shortBuffer.reset();
			}
			
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			currentWord.reset(); // reseta o contador de bytes escritos (remoção lógica)
		}
		
		return outputStream.toByteArray();
	}
	
	private int decodeAndWrite(byte[] currentBytes)
	{
		shortBuffer.put(currentBytes); // escreve os bytes do índice da palavra no buffer
		currentIndexOnDictionary = shortBuffer.getShort(); // obtém o índice da palavra no dicionário
		
		byte[] data = dictionary.get(currentIndexOnDictionary); // obtém a palavra
		
		dictionary.add( MyArray.concatArrays(data, ZERO) );
		
		int index = MyArray.first(dictionary, (it) -> Arrays.equals(currentBytes, it));
		
		if (byteRead != -1)
		{
			currentWord.write(byteRead); // escreve o byte lido na palavra atual
			currentBytes = currentWord.toByteArray(); // pegue o array de bytes da palavra atual
			
			// procura pela primeiro item do dicionário que seja igual ao array de bytes da palavra atual
			index = MyArray.first(dictionary, (it) -> Arrays.equals(currentBytes, it));
		}
		
		return index;
	}
	
	public byte[] decode(byte[] array)
	{
		clearDictionary();
		
		shortBuffer = ByteBuffer.allocate(Short.BYTES);
		outputStream = new ByteArrayOutputStream(array.length * Short.BYTES);
		currentWord = new ByteArrayOutputStream();
		inputStream = new ByteArrayInputStream(array);
		
		try
		{
			byte[] currentBytes = inputStream.readNBytes(2);
			
			for (int i = 0; i < array.length; i++)
			{
				// concatena o byte atual à corrente de saída e testa
				// se os bytes dela correspondem a algum item do dicionário
				currentIndexOnDictionary = (currentByte);
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
					shortBuffer.reset();
				}
				
				catch (IOException e)
				{
					e.printStackTrace();
				}
				
				currentWord.reset(); // reseta o contador de bytes escritos (remoção lógica)
			}
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}
}
