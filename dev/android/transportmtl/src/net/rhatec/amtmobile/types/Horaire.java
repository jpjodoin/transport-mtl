package net.rhatec.amtmobile.types;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import android.text.format.DateFormat;
import android.text.format.Time;
import android.content.Context;

/**
 * @author JP
 * @description: Par horaire on attend , SAMEDI, DIMANCHE, SEMAINE, c'est un
 *               type d'horaire. Cette objet horaire contient: - un nom
 *               d'horaire (SAMEDI, DIMANCHE, SEMAINE) - de l'extraInfo pour
 *               l'horaire - un tableau d'objet Temps qui n'est qu'un objet Time
 *               Java avec plusieurs informations visant � savoir si c'est un
 *               horaire sp�ciale ou bien pour handicap� etc.
 * 
 */
public class Horaire
{
	private String				m_strHoraireNom;
	private String				m_strHoraireExtraInfo;

	private ArrayList<Temps>	m_TimeArray;
	static final int			heureChaqueSixMinute	= 30;
	static final int			minuteChaqueSixMinute	= 06;

	public Horaire()
	{

	}
	
	/**
	 * Constructeur
	 * 
	 * @param strNom
	 *            Nom de l'horaire (exemple: 1 2 3 )
	 */
	
	public Horaire(String strNom, String strExtraInfoHoraire)
	{
		m_TimeArray = new ArrayList<Temps>(20);
		m_strHoraireNom = strNom;
		m_strHoraireExtraInfo = strExtraInfoHoraire;

	}
	
	public String Serialize()
	{
		StringBuilder sb = new StringBuilder(m_TimeArray.size()*5+m_strHoraireNom.length()+m_strHoraireExtraInfo.length()); //TODO: V�rifier que la taille est ok
		sb.append("h;");
		sb.append(m_strHoraireNom).append(";").append(m_strHoraireExtraInfo).append(";");
		for (Temps t : m_TimeArray)
		{
			sb.append(t.m_str).append(";");
		}
		sb.append("\n");
		return sb.toString();
	
	}
	
	public boolean UnSerialize(String str)
	{
		boolean IsValid = false; 
		String array[] = str.split(";");
		int arraySize = array.length;
		
		if(array.length>3 && array[0].charAt(0) == 'h')
		{
			m_TimeArray = new ArrayList<Temps>(array.length);
			m_strHoraireNom = array[1];
			m_strHoraireExtraInfo = array[2];
			for(int i = 3; i < arraySize; ++i)
			{
				m_TimeArray.add(new Temps(array[i]));
			}		
			
			IsValid = true;
		}
		return IsValid;
		
	}

	/**
	 * Renvoie le nom de l'horaire
	 * 
	 * @return Retourne le nom de l'horaire (Exemple: 1 2 3 20101225)
	 */
	public String ObtenirNom()
	{
		return m_strHoraireNom;
	}

	/**
	 * Renvoie le extrainfo de l'horaire
	 * 
	 * @return Ex: Terminus speciale xyz
	 */
	public String ObtenirExtraInfo()
	{
		return m_strHoraireExtraInfo;
	}

	/**
	 * Prend un argument de type 1200 (qui sera convertis par time en 12:00) et
	 * l'ajoute � un vecteur
	 * 
	 * @param strData
	 *            String de texte du format des .dba
	 */
	public void AjouterHeure(String strData)
	{
		Temps temps = new Temps(strData);

		m_TimeArray.add(temps);
	}

	/**
	 * Retourne le vecteur des heures de l'horaire
	 * 
	 * @return Vecteur des heures de l'horaire
	 */
	// public Vector<Time> ObtenirVecteurHeure()
	public ArrayList<Temps> ObtenirVecteurHeure()
	{
		return m_TimeArray;
	}

	/**
	 * Retourne un string contenant nbrPassage suivant selon l'heure actuelle
	 * 
	 * @param nbrPassage
	 *            Nombre de passage a obtenir
	 * @param apresMinuit
	 *            est ce qu'on est apres minuit ?
	 * @return nbrPassage de l<arret d<autobus prochain
	 */
	public Vector<String> ObtenirProchainPassage(int nbrPassage, boolean apresMinuit, Context context)
	{
		Vector<String> prochainArret = new Vector<String>(nbrPassage);
		Calendar unCalendrier = Calendar.getInstance();

		Time actuelTime = new Time();
		actuelTime.hour = unCalendrier.get(Calendar.HOUR_OF_DAY);
		actuelTime.minute = unCalendrier.get(Calendar.MINUTE);

		int i = 0;
		if (apresMinuit == true)
		{
			// On ne va considerer que les horaires apres minuit
			if (this.obtenirPositionApresMinuit() != -1)
				i = this.obtenirPositionApresMinuit();
		}
		while (i < m_TimeArray.size() && nbrPassage > prochainArret.size())
		{

			Time tTemps = m_TimeArray.get(i).m_Time;

			if (m_TimeArray.get(i).m_PassageAIntervalle)
			{
				// est ce que le prochain passage hormis les 3 points (...) a
				// d�j� eu lieu ?
				Time tTempsProchainVecteur = m_TimeArray.get(i + 1).m_Time;

				if (tTempsProchainVecteur.hour != heureChaqueSixMinute)
				{ // On veut pas de ... comme referentiel a t + 1
					if ((actuelTime.hour == tTempsProchainVecteur.hour && actuelTime.minute < tTempsProchainVecteur.minute) || actuelTime.hour < tTempsProchainVecteur.hour)
					{
						prochainArret.add("... ");

					}
				}

			} else if ((actuelTime.hour == tTemps.hour && actuelTime.minute <= tTemps.minute) || actuelTime.hour < tTemps.hour)
			{
				if (DateFormat.is24HourFormat(context))
				{
					prochainArret.add(tTemps.format("%H:%M"));
				} else
				{
					// Formatage am/pm
					prochainArret.add(tTemps.format("%I:%M%p"));
				}

			}
			i++;
		}

		// On va s'occuper des heure de la journee suivantes
		// TODO Analyser le traitement complet de cette fonction est voir si y a
		// pas moyen de faire mieux.

		if (apresMinuit == false)
		{
			int positionApresMinuit = obtenirPositionApresMinuit();

			if (positionApresMinuit != -1 && nbrPassage > prochainArret.size())
			{
				Time tTemps;
				int timeArraySize = m_TimeArray.size();
				int sizeProchainArray = prochainArret.size();
				for (int z = positionApresMinuit; z < timeArraySize && nbrPassage > sizeProchainArray; ++z)
				{
					tTemps = m_TimeArray.get(z).m_Time;

					if (DateFormat.is24HourFormat(context))
					{
						prochainArret.add(tTemps.format("%H:%M"));
					} else
					{
						// Formatage am/pm
						prochainArret.add(tTemps.format("%I:%M%p"));
					}

				}
			}

		}

		return prochainArret;
	}

	public int ObtenirPositionProchainPassage()
	{
		int position = 0;
		Calendar unCalendrier = Calendar.getInstance();

		Time actuelTime = new Time();
		actuelTime.hour = unCalendrier.get(Calendar.HOUR_OF_DAY);
		actuelTime.minute = unCalendrier.get(Calendar.MINUTE);

		int i = 0;

		while (i < m_TimeArray.size() && position == 0)
		{

			Time tTemps = m_TimeArray.get(i).m_Time;

			if (m_TimeArray.get(i).m_PassageAIntervalle)
			{
				// est ce que le prochain passage hormis les 3 points (...) a
				// d�j� eu lieu ?
				Time tTempsProchainVecteur = m_TimeArray.get(i + 1).m_Time;

				if (tTempsProchainVecteur.hour != heureChaqueSixMinute)
				{ // On veut pas de ... comme referentiel a t + 1
					if ((actuelTime.hour == tTempsProchainVecteur.hour && actuelTime.minute < tTempsProchainVecteur.minute) || actuelTime.hour < tTempsProchainVecteur.hour)
					{
						position = i;
					}
				}

			} else if ((actuelTime.hour == tTemps.hour && actuelTime.minute < tTemps.minute) || actuelTime.hour < tTemps.hour)
			{
				position = i;
			}
			i++;
		}

		return position;
	}

	/**
	 * Retourne la position du changement dans une journee entre 23h59(jour1) et
	 * 00h01(jour2)
	 * 
	 * @return position dans le vecteur du debut de la journee suivante
	 */
	private int obtenirPositionApresMinuit()
	{
		int heure = 99;
		int minute = 99;
		int positionApresMinuit = -1;
		int timeArraySize = m_TimeArray.size()-1; //Boucle descendante
		for (int i = timeArraySize; i >= 0; --i)
		{
			Time tTemps = m_TimeArray.get(i).m_Time;
			// 99 >= tTemps.hour, 99 23h39 0h09 0h40 1h35 et 1h12
			// L'algo consiste � trouver une heure plus petite que 99h99 ou bien
			// l'heure la plus petite que l'heure enregistr�. On determine
			// ensuite si l'heure precedente et plus grande
			// que l'heure la plus petite qu'on possede. Si c'est le cas, nous
			// avons la premiere heure apres minuit.
			// Complexite O(n) dans le pire cas et O(4) dans le reste.
			if (heure >= tTemps.hour || (heure == tTemps.hour && minute > tTemps.minute))
			{
				// On a trouve une heure plus petite
				heure = tTemps.hour;
				minute = tTemps.minute;

				if (i > 1)
				{
					tTemps = m_TimeArray.get(i - 1).m_Time;

					if (heure < tTemps.hour)
					{
						positionApresMinuit = i;
						return positionApresMinuit;
					}
				}
			}

		}

		return positionApresMinuit;

	}

}
