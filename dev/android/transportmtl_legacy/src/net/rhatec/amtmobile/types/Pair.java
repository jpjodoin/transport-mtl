package net.rhatec.amtmobile.types;

public class Pair<T, U>
{
	public T	first;
	public U	second;

	public Pair(T f, U s)
	{
		this.first = f;
		this.second = s;
	}

	public Pair()
	{

	}

	public void setFirst(T f)
	{
		this.first = f;
	}

	public void setSecond(U s)
	{
		this.second = s;
	}
}