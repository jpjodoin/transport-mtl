package net.rhatec.amtmobile.types;

import net.rhatec.amtmobile.R;

import android.content.Context;

public class TransportServiceBase implements SerializableObject
{
	protected String	longName;
	protected String	shortName;
	protected String	m_debutValidite;
	protected String	m_finValidite	= "0";

	/*
	public TransportServiceBase(String _toDeserialize)
	{
		String[] transport = _toDeserialize.split(";");
		if(transport.length == 4)
		{
			shortName = transport[0];
			longName = transport[1];
			m_debutValidite = transport[2];
			m_finValidite = transport[3];
		}

		
	}*/

	protected TransportServiceBase()
	{
	}

	public TransportServiceBase(String _shortName, String _longName, String _debutValidite, String _finValidite)
	{
		shortName = _shortName;
		longName = _longName;
		m_debutValidite = _debutValidite;
		m_finValidite = _finValidite;
	}

	public String getLongName()
	{
		return longName;
	}

	public void setLongName(String longName)
	{
		this.longName = longName;
	}

	public String getShortName()
	{
		return shortName;
	}

	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}

	public String getdebutValidite()
	{
		return m_debutValidite;
	}

	public void setdebutValidite(String debutValidite)
	{
		this.m_debutValidite = debutValidite;
	}

	public String getFinValidite()
	{
		return m_finValidite;
	}

	public void setFinValidite(String finValidite)
	{
		this.m_finValidite = finValidite;
	}

	// date courante dans le format :yyyyMMdd
	public boolean isValid(int dateCourante)
	{
		boolean result = false;
		if (m_finValidite.equals("0")) // Validite inconnu
		{
			result = true;
		} else
		{
			int dateFin = Integer.parseInt(m_finValidite);

			if (dateCourante <= dateFin)
				result = true;
		}

		return result;
	}

	public Pair<String, String> getValiditeDate(Context context)
	{
		return new Pair<String, String>(formatDate(m_debutValidite, context), formatDate(m_finValidite, context));
	}

	// Affichage seulement
	private String formatDate(String _date, Context _context)
	{
		String resultat = _context.getString(R.string.TransportServiceBase_Date_Inconnu);
		if (!_date.equals("0") && _date.length() == 8)
		{
			StringBuffer Stringdate = new StringBuffer(_date.substring(6, 8) + "/");
			Stringdate.append(_date.substring(4, 6) + "/");
			Stringdate.append(_date.substring(0, 4));
			resultat = Stringdate.toString();
		}
		return resultat;
	}

	@Override
	public String serialize()
	{
		StringBuffer buf = new StringBuffer(shortName + ";");
		buf.append(longName + ";");
		buf.append(m_debutValidite + ";");
		buf.append(m_finValidite + ";");
		return buf.toString();

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

}
