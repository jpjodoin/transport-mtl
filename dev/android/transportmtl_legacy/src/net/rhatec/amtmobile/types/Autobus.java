package net.rhatec.amtmobile.types;

import java.io.Serializable;
import java.util.Vector;

/**
 * @author JP
 * @description: Un autobus poss�de un nom, une direction, un num�ro et de
 *               l'extrainfo (C EST QUOI). Chaque autobus poss�de un vecteur
 *               d'horaire. Par horaire on entend SAMEDI, DIMANCHE, SEMAINE etc.
 */
public class Autobus implements Serializable
{
	private static final long	serialVersionUID	= 1L;
	Vector<Horaire>				m_vHoraire;
	String						m_strNumero;
	String						m_strDirection;
	String						m_strNom;
	String						m_strDirectionExtraInfo;
	String						m_strCircuitExtraInfo;
	int						m_Color;

	/**
	 * 
	 * @param strNumero
	 * @param strDirection
	 * @param strNom
	 * @param strExtraInfo
	 */
	public Autobus(String strNumero, String strDirection, String strNom, String strDirectionExtraInfo, String strCircuitExtraInfo, String color)
	{
		m_strNumero = strNumero;
		m_strDirection = strDirection;
		m_strNom = strNom;
		m_strDirectionExtraInfo = strDirectionExtraInfo;
		m_strCircuitExtraInfo = strCircuitExtraInfo;
		m_Color = Integer.parseInt(color, 16); 
		m_vHoraire = new Vector<Horaire>(3);
	}

	
	public int ObtenirCouleur()
	{
		return m_Color;
	}
	
	/**
	 * 
	 * @param hHoraire
	 */
	public void AjouterHoraire(Horaire hHoraire)
	{
		m_vHoraire.add(hHoraire);
	}

	/**
	 * 
	 * @return
	 */
	public Vector<Horaire> ObtenirVecteurHoraire()
	{
		return m_vHoraire;
	}

	/**
	 * 
	 * @return
	 */
	public String ObtenirNom()
	{
		return m_strNom;
	}

	/**
	 * 
	 * @return
	 */
	public String ObtenirDirectionCode()
	{
		return m_strDirection;
	}

	/**
	 * 
	 * @return
	 */
	public String ObtenirNumero()
	{
		return m_strNumero;
	}

	/**
	 * TODO QUE represente le int
	 * 
	 * @return
	 */
	public String ObtenirInfoDirectionCode()
	{
		return m_strDirectionExtraInfo;
	}
	
	public String ObtenirInfoCircuitCode()
	{
		return m_strCircuitExtraInfo;
	}
}
