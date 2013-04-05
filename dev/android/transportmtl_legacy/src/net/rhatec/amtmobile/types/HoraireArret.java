package net.rhatec.amtmobile.types;

import java.util.Vector;

/**
 * @author JP
 * @description: Cette objet contient les informations pertinentes pour chaque
 *               arrêt: - Nom de l'intersection - Un vecteur d'objet Autobus
 *               passant à cet arrêt - La ligne où se situe l'horaire d'intérêt
 */

public class HoraireArret
{
	Vector<Autobus>			m_vAutobus;
	String					m_strIntersection;
	int						m_Error;
	long					m_BytesPosition;				// Ligne où se situe
															// l'horaire
															// d'intérêt

	public static final int	AUCUNE_ERREUR			= 0;
	public static final int	FICHIER_INTROUVABLE		= 1;
	public static final int	IOEXCEPTION				= 2;
	public static final int	DONNEES_MAL_FORMATEE	= 3;
	public static final int	ARRET_NON_TROUVE		= 4;
	public static final int	DEBARCADERE_SEULEMENT	= 5;

	/**
	 * @function: HoraireArret
	 * @description: Constructeur initialise le nombre d'erreur à 0. Cette objet
	 *               contient les informations pertinentes pour chaque arrêt: -
	 *               Nom de l'intersection - Un vecteur d'objet Autobus passant
	 *               à cet arrêt - La ligne où se situe l'horaire d'intérêt
	 * @author:JP
	 * @params[in]: none
	 * @params[out]: none
	 */
	public HoraireArret()
	{
		m_Error = AUCUNE_ERREUR;
	}

	/**
	 * @function: ObtenirIntersection
	 * @description:
	 * @author: Hocine
	 * @params[in]: none
	 * @params[out]: intersection en string
	 */

	public String ObtenirIntersection()
	{
		return m_strIntersection;
	}

	/**
	 * @function: ObtenirAutobus
	 * @description:
	 * @author: Hocine
	 * @params[in]: none
	 * @params[out]: Vecteur d'objet Auobus
	 */
	public Vector<Autobus> ObtenirAutobus()
	{
		return m_vAutobus;
	}

	public void SetIntersection(String strIntersection)
	{
		m_strIntersection = strIntersection;
	}

	public void SetAutobus(Vector<Autobus> vAutobus)
	{
		m_vAutobus = vAutobus;
	}

	public void SetErreur(int eError)
	{
		m_Error = eError;
	}

	public int ObtenirErreur()
	{
		return m_Error;
	}

	public long ObtenirBytesPosition()
	{
		return m_BytesPosition;
	}

	public void SetBytePosition(long l)
	{
		m_BytesPosition = l;
	}

}
