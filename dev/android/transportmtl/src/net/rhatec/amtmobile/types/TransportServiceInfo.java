package net.rhatec.amtmobile.types;

public class TransportServiceInfo extends TransportServiceBase
{
	String			type;
	private boolean affichageExtraInfoCircuit;
	private boolean	affichageExtraInfoDirection;
	private boolean	affichageRecherche;
	private boolean	affichageCarte;
	//private boolean afficherNoArret;

	public TransportServiceInfo(String _shortName, String _longName, String _debutValidite, String _finValidite, String _type, boolean _affichageExtraInfoDirection, boolean _affichageRecherche,
			boolean _affichageCarte, boolean _affichageExtraInfoCircuit/*, boolean _afficherNoArret*/)
	{
		super(_shortName, _longName, _debutValidite, _finValidite);
		affichageExtraInfoCircuit = _affichageExtraInfoCircuit;
		affichageExtraInfoDirection = _affichageExtraInfoDirection;
		affichageRecherche = _affichageRecherche;
		affichageCarte = _affichageCarte;
		//afficherNoArret = _afficherNoArret;
		type = _type;
	}

	@Override
	public String getLongName()
	{
		return longName;
	}

	@Override
	public void setLongName(String longName)
	{
		this.longName = longName;
	}

	@Override
	public String getShortName()
	{
		return shortName;
	}

	@Override
	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}

	@Override
	public String getdebutValidite()
	{
		return m_debutValidite;
	}

	@Override
	public void setdebutValidite(String debutValidite)
	{
		this.m_debutValidite = debutValidite;
	}

	@Override
	public String getFinValidite()
	{
		return m_finValidite;
	}

	@Override
	public void setFinValidite(String finValidite)
	{
		this.m_finValidite = finValidite;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public boolean isAffichageExtraInfoDirection()
	{
		return affichageExtraInfoDirection;
	}

	public boolean isAffichageExtraInfoCircuit()
	{
		return affichageExtraInfoCircuit;
	}

	public boolean isAffichageCarte()
	{
		return affichageCarte;
	}

	public void setAffichageCarte(boolean _affichageExtraInfo)
	{
		this.affichageExtraInfoDirection = _affichageExtraInfo;
	}

	public boolean displaySearchBox()
	{
		return affichageRecherche;
	}

	public void setAffichageRecherche(boolean _affichageRecherche)
	{
		this.affichageRecherche = _affichageRecherche;
	}

	static public String getKeyFromString(String serializable)
	{
		return serializable.split(";")[0];
	}

	@Override
	public String getKey()
	{
		return shortName;
	}
		
	public boolean isBus()
	{
		return type.equals("0");
	}
	
	public boolean isTrain()
	{
		return type.equals("1");
	}
	
	public boolean isMetro()
	{
		return type.equals("2");
	}

}
