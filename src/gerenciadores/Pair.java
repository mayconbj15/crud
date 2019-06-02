package gerenciadores;

public class Pair<TYPE1, TYPE2>
{
	public TYPE1 key;
	public TYPE2 value;
	
	public Pair(TYPE1 key, TYPE2 value)
	{
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String toString()
	{
		return "{" + key + ", " + value + "}";
	}
}