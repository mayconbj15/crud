package util;

import java.util.ArrayList;
import java.util.function.Function;

public class MyArray
{
	public static int indexOf(int element, int[] array)
	{
		int index = -1;
		
		if (array != null)
		{
			for (int i = 0; index == -1 && i < array.length; i++)
			{
				if (array[i] == element)
				{
					index = i;
				}
			}
		}
		
		return index;
	}
	
	public static boolean contains(int element, int[] array)
	{
		return indexOf(element, array) != -1;
	}

	/**
	 * Procura pelo primeiro item em {@code array} que
	 * satisfazer uma certa condição.
	 * 
	 * @param array Lista de itens.
	 * @param comparator Método que receberá cada item
	 * de {@code array} e retornará um booleano dizendo
	 * se esse item satisfaz a condição ou não.
	 * 
	 * @return -1 caso nenhum elemento satisfaça a condição.
	 * Caso contrário, o índice do primeiro elemento que
	 * satisfazer a condição.
	 */
	
	public static <T> int first(ArrayList<T> array, Function<T, Boolean> comparator)
	{
		int size = array.size();
		int index = -1;
		
		// percorre o ArrayList
		for (int i = 0; index == -1 && i < size; i++)
		{
			// checa se o objeto casa com o padrão desejado
			if ( comparator.apply(array.get(i)) )
			{
				index = i;
			}
		}
		
		return index;
	}
	
	public static byte[] concatArrays(byte[] array1, byte[] array2)
	{
		byte[] newArray = new byte[array1.length + array2.length];
		
		System.arraycopy(array1, 0, newArray, 0, array1.length);
		System.arraycopy(array2, 0, newArray, array1.length, array2.length);
		
		return newArray;
	}
}
