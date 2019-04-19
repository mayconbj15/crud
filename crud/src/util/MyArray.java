package util;

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
}
