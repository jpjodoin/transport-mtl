package net.rhatec.amtmobile.types;

import android.text.format.Time;

/**
 * @author JP
 * @description: Cette objet contient un objet Time java ainsi que plusieurs
 *               informations visant � savoir si c'est un horaire sp�ciale ou
 *               bien pour handicap� etc.
 */
public class Temps
{
	public Time		m_Time;
	public boolean	m_PassageAIntervalle;
	public boolean	m_PourHandicape;		// In Java, boolean are false by
											// default
	public boolean	m_HoraireSpecial;
	public boolean	m_Lundi;
	public boolean	m_Mardi;
	public boolean	m_Mercredi;
	public boolean	m_Jeudi;
	public boolean	m_Vendredi;
	public boolean	m_Samedi;
	public boolean	m_Dimanche;
	public String 	m_str; //String Version...

	/**
	 * Constructeur de la fonct ion time. Prend un argument de type 1200 et le
	 * convertis en 12:00
	 * 
	 * @param strTime
	 *            String de texte du format des .dba
	 */
	public Temps(String _temps)
	{
		m_str = _temps;
		int tempsAvecCode = Integer.parseInt(_temps);

		m_PourHandicape = (tempsAvecCode & 0x1000) != 0;
		m_HoraireSpecial = (tempsAvecCode & 0x2000) != 0;
		m_Lundi = (tempsAvecCode & 0x4000) != 0;
		m_Mardi = (tempsAvecCode & 0x8000) != 0;
		m_Mercredi = (tempsAvecCode & 0x10000) != 0;
		m_Jeudi = (tempsAvecCode & 0x20000) != 0;
		m_Vendredi = (tempsAvecCode & 0x40000) != 0;
		m_Samedi = (tempsAvecCode & 0x80000) != 0;
		m_Dimanche = (tempsAvecCode & 0x100000) != 0;

		int tempsSansCode = tempsAvecCode & 0xFFF;
		if (tempsSansCode == 3006)
		{
			m_PassageAIntervalle = true;
		}
		String tempsFormater = String.valueOf(tempsSansCode);
		m_Time = new Time();
		if (tempsFormater.length() > 2)
		{
			m_Time.hour = Integer.parseInt(tempsFormater.substring(0, tempsFormater.length() - 2));
			m_Time.minute = Integer.parseInt(tempsFormater.substring(tempsFormater.length() - 2, tempsFormater.length()));

		} 
		else if (tempsFormater.length() == 2 || tempsFormater.length() == 1)
		{
			m_Time.hour = 0;
			m_Time.minute = tempsSansCode;
		} 
		else
		{
			m_Time.hour = 0;
			m_Time.minute = 0;
		}

	}

	/**
	 * @return the m_Time
	 */
	public Time getM_Time()
	{
		return m_Time;
	}

}
