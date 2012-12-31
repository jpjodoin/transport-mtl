package net.rhatec.amtmobile.types;

/**
 * Classe Objet Permettant de stocker des informations sur les arr�ts
 * 
 * @author Jean-Philippe Jodoin
 * 
 */

public class Arret
{
	private String	m_strNumeros;
	private String	m_strNomRue1;
	private String	m_strNomRue2;
	private int m_latitude;
	private int m_longitude;
	private String	m_strPositionInFile;
	private boolean	m_fHasMetro;
	private boolean	m_fHasTrain;

	/**
	 * Constructeur permettant de construire un arr�t
	 * 
	 * @param strNumeros
	 *            # de l'arr�t
	 * @param strNomRue1
	 *            nom de la rue 1
	 * @param strNomRue2
	 *            nom de la rue 2
	 */
	public Arret(String strNumeros, String strNomRue1, String strNomRue2, String strMetroTrain, String lat, String longitude, String strPositionInFile)
	{
		m_strNumeros = strNumeros;
		m_strNomRue1 = strNomRue1;
		m_strNomRue2 = strNomRue2;
		m_strPositionInFile = strPositionInFile;
		int nAutobus = Integer.valueOf(strMetroTrain);
		m_fHasTrain = ((nAutobus & 0x1) != 0);
		m_fHasMetro = ((nAutobus & 0x2) != 0);
		m_latitude =  Integer.parseInt(lat);
		m_longitude = Integer.parseInt(longitude);
	}

	
	public int ObtenirLatitude()
	{
		return m_latitude;
	}
	
	public int ObtenirLongitude()
	{
		return m_longitude;
	}
	
	/**
	 * Retourne # de l'arret
	 * 
	 * @return # de l'arret
	 */
	public String ObtenirNumero()
	{
		return m_strNumeros;
	}

	/**
	 * Retourne nom de la rue 1 de l'intersection
	 * 
	 * @return Nom de Rue 1
	 */
	public String ObtenirNomRue1()
	{
		return m_strNomRue1;
	}

	/**
	 * Retourne nom de la rue 2 de l'intersection
	 * 
	 * @return Nom de Rue 2
	 */
	public String ObtenirNomRue2()
	{
		return m_strNomRue2;
	}

	/**
	 * Retourne la position dans le fichier d'horaire en octet
	 * 
	 * @return Position dans fichier
	 */
	public String ObtenirPositionDansFichier()
	{
		return m_strPositionInFile;
	}

	public boolean ObtenirTrain()
	{
		return m_fHasTrain;
	}

	public boolean ObtenirMetro()
	{
		return m_fHasMetro;
	}

}
