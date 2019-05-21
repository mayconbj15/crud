package gerenciadores;

public class Pair<TYPE1, TYPE2>
{
	public TYPE1 obj1;
	public TYPE2 obj2;
	
	public Pair(TYPE1 obj1, TYPE2 obj2)
	{
		this.obj1 = obj1;
		this.obj2 = obj2;
	}
	
	@Override
	public String toString()
	{
		return "{" + obj1 + ", " + obj2 + "}";
	}
}