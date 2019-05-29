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
		return array != null && indexOf(element, array) != -1;
	}

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
}
