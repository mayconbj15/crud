package compression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class LZW
{
	private static final ArrayList<byte[]> dictionary = new ArrayList<byte[]>();
	{
		for (byte i = 0; i < 255; i++)
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
	
	public byte[] encode(byte[] array)
	{
		clearDictionary();
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(array);
		
		
		
		return outputStream.toByteArray();
	}
}
