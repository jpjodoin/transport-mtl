package net.rhatec.amtmobile.types;

public enum TransportType
{
	BUS("0"), TRAIN("1"), METRO("2");

	/** L'attribut qui contient la valeur associé à l'enum */
	private final String	value;

	/** Le constructeur qui associe une valeur à l'enum */
	private TransportType(String value)
	{
		this.value = value;
	}

	/** La méthode accesseur qui renvoit la valeur de l'enum */
	public String getValue()
	{
		return this.value;
	}

}
