package net.rhatec.amtmobile.types;

import java.util.Vector;

public class Favoris implements FavorisNode
{
	public String	m_strIntersection;
	public String	m_strNoArret;
	public String	m_strNoBus;
	public String	m_strDirection;
	public String	m_strTransportService;
	public long m_nLigneFavoris;
	public String m_codeInfoDirection;
	public String m_codeInfoCircuit;
	public Vector<Horaire> m_vHoraire;

	//String strTransportService, String strAutobus, String strDirection, String strArret, String strIntersection, long nLigneFavoris, String codeExtraInfo
	public Favoris()
	{
		
	}
	
	public Favoris(String strTransportService, String strNoBus, String strDirection, String strNoArret, String strIntersection, long nLigneFavoris, String codeExtraInfo, Vector<Horaire> vHoraire, String strExtraInfoCircuit)
	{
		m_strIntersection = strIntersection;
		m_strNoArret = strNoArret;
		m_strNoBus = strNoBus;
		m_strDirection = strDirection;
		m_strTransportService = strTransportService;
		m_nLigneFavoris = nLigneFavoris;
		m_codeInfoDirection = codeExtraInfo;
		m_codeInfoCircuit = strExtraInfoCircuit;
		m_vHoraire = vHoraire;
	}

	public String ObtenirTransportService()
	{
		return m_strTransportService;
	}

	public String ObtenirNomIntersection()
	{
		return m_strIntersection;
	}

	public String ObtenirNumeroArret()
	{
		return m_strNoArret;
	}

	public String ObtenirNumeroAutobus()
	{
		return m_strNoBus;
	}

	public String ObtenirDirection()
	{
		return m_strDirection;
	}
	
	@Override
	public String Serialize()
	{
		StringBuilder sb = new StringBuilder(2048); //Regarder pour taille...
		sb.append("f;").append(m_strTransportService).append(";").append(m_strNoBus).append(";").append(m_strDirection).append(";").append(m_strNoArret).append(";").append(m_strIntersection).append(";").append(m_nLigneFavoris).append(";").append(m_codeInfoDirection).append(";").append(m_codeInfoCircuit).append("\n");
		for (Horaire h : m_vHoraire)
		{
			sb.append(h.Serialize());
		}	
		
		return sb.toString();
	}
	
	public boolean UnSerialize(String str, boolean ajouterHoraire, int horaireSpecifique)
	{
		boolean favorisValide = false;
		String[] horairearray = str.split("\n");
		int length = horairearray.length;
		m_vHoraire = new Vector<Horaire>(length-1);
		if(length>0)
		{
			String Info[] = horairearray[0].split(";");
			if(Info.length == 9 && Info[0].equals("f"))
			{
				m_strTransportService = Info[1];
				m_strNoBus = Info[2];
				m_strDirection = Info[3];
				m_strNoArret = Info[4];
				m_strIntersection = Info[5];
				m_nLigneFavoris = Long.valueOf(Info[6]);
				m_codeInfoDirection = Info[7];
				m_codeInfoCircuit = Info[8];
				if(ajouterHoraire)
				{
					if(horaireSpecifique == -1)
					{
						for(int i = 1; i<length; ++i)
						{
							Horaire h = new Horaire();
							if(h.UnSerialize(horairearray[i]))
								m_vHoraire.add(h);					
						}
					}
					else
					{
						Horaire h = new Horaire();
						if(h.UnSerialize(horairearray[++horaireSpecifique]))
							m_vHoraire.add(h);			
					}
				}

				favorisValide = true;
			}				
		}
		return favorisValide;		
	}

	@Override
	public FavorisType getType() {
		return FavorisType.Favoris;
	}
	
	
}
