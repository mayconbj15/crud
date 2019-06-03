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
	private static final byte[] ZERO = new byte[] { (byte) 0 };
	
	private final ArrayList<byte[]> dictionary = new ArrayList<byte[]>();
	{
		// muito cuidado com a condição de parada do for,
		// o Java não tem tipo unsigned
		for (byte i = 0; i != -1; i++)
		{
			dictionary.add( new byte[] { i } );
		}
		
		dictionary.add( new byte[] { (byte) -1 } );
	}

	private byte[] currentData;
	private byte[] lastData;
	private byte[] currentBytes;
	private ByteBuffer shortBuffer = ByteBuffer.allocate(Short.BYTES);
	{
		// cria uma marca de torno para a função reset()
		shortBuffer.mark();
	}
	private ByteArrayInputStream inputStream;
	private ByteArrayOutputStream currentWord;
	private ByteArrayOutputStream outputStream;
	private int numberOfBytesRead;
	private int lastIndexOnDictionary;
	private int currentIndexOnDictionary;
	
	private void clearDictionary()
	{
		int size = dictionary.size();
		
		for (int i = 256, j = 1; i < size; i++, j++)
		{
			// vai removendo os últimos elementos
			dictionary.remove(size - j);
		}
	}
	
	/**
	 * Adiciona {@code byteRead} à palavra atual e checa se essa nova
	 * palavra existe no dicionário.
	 * 
	 * @param byteRead Byte lido.
	 * 
	 * @return -1 se a nova palavra não existir no dicionário ou
	 * se o byte lido for -1. Caso contrário, retorna o índice
	 * da nova palavra no dicionário.
	 */
	
	private int addByteAndGetWordIndexOnDictionary(int byteRead)
	{
		int index = -1;
		
		if (byteRead != -1)
		{
			// escreve o byte lido na palavra atual
			currentWord.write(byteRead);
			currentBytes = currentWord.toByteArray();
			
			// procura pela primeiro item do dicionário
			// que seja igual ao array de bytes da palavra atual
			index = MyArray.first(dictionary, (it) -> Arrays.equals(currentBytes, it));
		}
		
		return index;
	}
	
	private void writeIndexOnOutput(int index)
	{
		shortBuffer.putShort((short) index);
		shortBuffer.reset();
		
		try
		{
			shortBuffer.get(currentData, 0, 2);
			outputStream.write(currentData);
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		shortBuffer.reset();
	}
	
	public byte[] compress(byte[] array)
	{
		clearDictionary();
		byte[] encodedBytes;
		
		outputStream = new ByteArrayOutputStream(array.length * Short.BYTES);
		currentWord = new ByteArrayOutputStream();
		inputStream = new ByteArrayInputStream(array);
		currentData = new byte[2];
		shortBuffer.reset();
		int currentByte = inputStream.read();
		
		for (int i = 0; i < array.length; i++)
		{
			currentIndexOnDictionary = addByteAndGetWordIndexOnDictionary(currentByte);
			lastIndexOnDictionary = -1;
			
			while (currentIndexOnDictionary != -1)
			{
				lastIndexOnDictionary = currentIndexOnDictionary;
				currentByte = inputStream.read();
				currentIndexOnDictionary = addByteAndGetWordIndexOnDictionary(currentByte);
			}

			if (lastIndexOnDictionary >= 0)
			{
				dictionary.add(currentBytes); // adiciona a nova palavra ao dicionário 
				
				writeIndexOnOutput(lastIndexOnDictionary); // escreve o índice da palavra codificada
				
				currentWord.reset(); // reseta o contador de bytes escritos (remoção lógica)
			}
		}
		
		encodedBytes = outputStream.toByteArray();
		
		try { outputStream.close(); }
		catch (IOException e) { e.printStackTrace(); }
		
		return encodedBytes;
	}
	
	/**
	 * Lê os próximos 2 bytes da corrente de entrada e decodifica-os
	 * em um short que é o índice da palavra no dicionário.
	 * 
	 * @return índice da palavra no dicionário.
	 */
	
	private int readAndDecodeIndex()
	{
		numberOfBytesRead = inputStream.read(currentBytes, 0, 2);
		
		shortBuffer.reset();
		shortBuffer.put(currentBytes); // escreve os bytes do índice da palavra no buffer
		shortBuffer.reset();
		
		return shortBuffer.getShort(); // obtém o índice da palavra no dicionário
	}
	
	/**
	 * Acessa o dicionário no índice {@code currentIndexOnDictionary} e
	 * obtém os bytes.
	 */
	
	private byte[] getIndex()
	{
		// obtém a palavra decodificada
		return currentData = dictionary.get(currentIndexOnDictionary);
	}
	
	/**
	 * Adiciona no dicionário a palavra decodificada + 1 byte zerado.
	 */
	
	private byte[] AddUnkownWordToDictionary()
	{
		lastIndexOnDictionary = dictionary.size();
		
		byte[] dataPlusTrash = MyArray.concatArrays(currentData, ZERO);
		
		dictionary.add(dataPlusTrash);
		
		return dataPlusTrash;
	}
	
	/**
	 * Atualiza o último byte da penúltima palavra lida com o primeiro
	 * byte da última palavra lida.
	 */
	
	private byte[] updateLastWord()
	{
		lastData = dictionary.get(lastIndexOnDictionary); // obtém a palavra com lixo
		
		// substitui o lixo pelo primeiro byte da palavra decodificada
		lastData[lastData.length - 1] = currentData[0];
		
		return lastData;
	}
	
	public byte[] decompress(byte[] array)
	{
		clearDictionary();
		
		outputStream = new ByteArrayOutputStream();
		currentWord = new ByteArrayOutputStream();
		inputStream = new ByteArrayInputStream(array);
		currentBytes = new byte[2];
		
		try
		{
			currentIndexOnDictionary = readAndDecodeIndex();
			getIndex();
			AddUnkownWordToDictionary();
			outputStream.write(currentData);
			
			for (int i = 2; numberOfBytesRead == 2 && i < array.length; i += 2)
			{
				currentIndexOnDictionary = readAndDecodeIndex();
				getIndex();
				updateLastWord();
				AddUnkownWordToDictionary();
				outputStream.write(currentData);
			}
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}
}
