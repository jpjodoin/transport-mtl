package net.rhatec.amtmobile.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.constants.TypeString;
import net.rhatec.amtmobile.file.BufferedFileReader;
import net.rhatec.amtmobile.helpers.FileHelpers;
import net.rhatec.amtmobile.helpers.StringHelpers;
import net.rhatec.amtmobile.providers.TransportProvider;
import net.rhatec.amtmobile.types.Arret;
import net.rhatec.amtmobile.types.DateHelpers;
import net.rhatec.amtmobile.types.Favoris;
import net.rhatec.amtmobile.types.FavorisGroupe;
import net.rhatec.amtmobile.types.FavorisNode;
import net.rhatec.amtmobile.types.Message;
import net.rhatec.amtmobile.types.Pair;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

//TODO: Refactoring

public class FavorisManager
{
	final public static short	nombreHoraireParLigne	= 3;
	final public static short FavorisCurrentVersion = 1;
	public static Message ajouterFavoris(/*String strTransportService, String strAutobus, String strDirection, String strArret, String strIntersection, long nLigneFavoris, String codeExtraInfo*/Favoris favoris)
	{
		Message retour = new Message(false, R.string.Horaire_ajout_favoris_nok);
		try
		{
			// On v�rifie si le fichier existe.
			BufferedFileReader in = FileHelpers.createBufferedFileInputStream(TransportProvider.getRootPath() + "favoris.dba", 4096);// 8*512 char
			if (in != null)
			{
				String favorisAAJouter = favoris.Serialize();//= strTransportService + ";" + strAutobus + ";" + strDirection + ";" + strArret + ";" + strIntersection + ";" + nLigneFavoris + ";" + codeExtraInfo;
				in.close();
				OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(TransportProvider.getRootPath() + "favoris.dba", true), "8859_1");
				out.write(favorisAAJouter);
				out.close();
				retour = new Message(true, R.string.Horaire_ajout_favoris_ok);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return retour;

	}

	/**
	 * @function: ObtenirFavoris
	 * @description: Retourne un tableau de HashMap avec les informations pour
	 *               chaque element ajoutes au favoris
	 * @author: Hocine
	 * @params[in]: Context de l'application
	 * @params[out]: Tableau de HashMap ou chque HashMapContient le nom du
	 *               transporteur, noAutobus, Direction, NoArret, Intersection
	 *               LigneFavoris ExtraInfo.
	 */
	
	public static ArrayList<FavorisNode> obtenirFavoris(Context context)
	{
		ArrayList<FavorisNode> vFavoris = new ArrayList<FavorisNode>(8);
		BufferedFileReader in;
		try
		{
			in = FileHelpers.createBufferedFileInputStream(TransportProvider.getRootPath() + "favoris.dba", 4096);// 8			
			if (in != null)
			{
				String strLine = in.readLine();
				//Favoris courant = null;
				while (strLine != null)
				{
					if(strLine.charAt(0) == 'f')
					{
						strLine = ajouterFavoris(in, strLine, vFavoris);
				
					}
					else if(strLine.charAt(0) == 'g')
					{
						strLine = ajouterFavorisGroupe(in, strLine, vFavoris);

					}
					else
						strLine = in.readLine();
				}
				in.close();
			}

		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return vFavoris;

	}
	
	public static String ajouterFavorisGroupe(BufferedFileReader in, String firstLine, ArrayList<FavorisNode> vFavoris) throws IOException
	{
		FavorisGroupe f = new FavorisGroupe();
		StringBuilder sb = new StringBuilder(2048);
		sb.append(firstLine);
		String strLine = in.readLine();
		while(strLine != null && strLine.charAt(0) != 'e')
		{	
			sb.append("\n"+strLine);
			strLine = in.readLine();
		}
		f.UnSerialize(sb.toString());
		vFavoris.add(f);
		return strLine;
	}
	
	public static String ajouterFavoris(BufferedFileReader in, String firstLine, ArrayList<FavorisNode> vFavoris) throws IOException
	{
		Favoris f = new Favoris();
		StringBuilder sb = new StringBuilder(2048);
		sb.append(firstLine);
		String strLine = in.readLine();
		while(strLine != null && strLine.charAt(0) == 'h')
		{	
			sb.append("\n"+strLine);
			strLine = in.readLine();
		}
		f.UnSerialize(sb.toString(), true, -1);
		vFavoris.add(f);
		return strLine;
	}
	//Todo: Add id
	public static String SerializeBookmarkHeader(Favoris f)
	{
		StringBuilder sb = new StringBuilder(512);
		sb.append(f.m_strTransportService).append(';').append(f.m_strNoBus).append(';').append(f.m_codeInfoDirection.equals("null") ? " " : TransportProvider.ObtenirPhraseInfoDirectionSpecifique(f.m_strTransportService, f.m_codeInfoDirection)).append(';').append(f.m_strNoArret).append(';').append(f.m_strIntersection);
		return sb.toString();
	}
	
	public static HashMap<String, String> UnserializeBookmarkHeader(String header)
	{
		String[] parameters = header.split(";");
		HashMap<String, String> item = new HashMap<String, String>();
		item.put(TypeString.SOCIETECODE, parameters[0]);
		item.put(TypeString.NOCIRCUIT, parameters[1]);
		item.put(TypeString.INFODIRECTIONPHRASE, parameters[2]);
		item.put(TypeString.NOARRET, parameters[3]);
		item.put(TypeString.INTERSECTIONSCONCAT, parameters[4]);
		return item;
		
	}
	
	

	
	public static boolean updateFavoris(ArrayList<FavorisNode> bookmarkList)
	{
		boolean result = false;
		try
		{
			BufferedFileReader in = FileHelpers.createBufferedFileInputStream(TransportProvider.getRootPath() + "favoris.dba", 4096);// 8
																																		// *
																																		// 512caracteres
			if (in != null)
			{
				//String strLine = in.readLine();
				OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(TransportProvider.getRootPath() + "favorisTemp.dba", false), "8859_1");
				for(FavorisNode bookmark : bookmarkList)
				{
					out.write(bookmark.Serialize());
				}				
				in.close();
				out.close();
				File ancienFavoris = new File(TransportProvider.getRootPath() + "favoris.dba");
				File nouveauFavoris = new File(TransportProvider.getRootPath() + "favorisTemp.dba");
				result = nouveauFavoris.renameTo(ancienFavoris) ? true : false;
				in.close();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return result;
	}
	



	/**
	 * Supprime les favoris totalement.
	 * 
	 */
	public static int supprimerFichierFavoris()
	{
		File favoris = new File(TransportProvider.getRootPath() + "favoris.dba");
		return favoris.delete() ? R.string.SupprimerTousFavorisOk : R.string.SupprimerTousFavorisNOk;
	}

	public static void supprimerFavorisSociete(Vector<String> shortName)
	{
		try
		{
			BufferedFileReader in = FileHelpers.createBufferedFileInputStream(TransportProvider.getRootPath() + "favoris.dba", 4096);
			if (in != null)
			{
				String strLine = in.readLine();
				OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(TransportProvider.getRootPath() + "favorisTemp.dba", false), "8859_1");
				boolean toDelete = false;
				while (strLine != null)
				{
					if(strLine.length() > 0 && strLine.charAt(0) == 'f')
					{
						toDelete = false;
						String array[] = strLine.split(";");
						nameloop: //Loop label
						for (String name : shortName )
						{
							if(array[1].equals(name))
							{
								toDelete = true;
								break nameloop; //We exit this loop
							}
						}
					}
					
					if (!toDelete)
					{
						out.write(strLine + "\n");
					}

					strLine = in.readLine();
				}
				File ancienFavoris = new File(TransportProvider.getRootPath() + "favoris.dba");
				File nouveauFavoris = new File(TransportProvider.getRootPath() + "favorisTemp.dba");
				nouveauFavoris.renameTo(ancienFavoris);
				in.close();
				out.close();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	static public void updateFavorisVersion(int currentFavorisVersion, Context context)
	{
		while(currentFavorisVersion != FavorisManager.FavorisCurrentVersion)
		{
			switch(currentFavorisVersion)
			{
			case 0:
				supprimerFichierFavoris();	//�craser fichier de favoris en cas d'�chec
				currentFavorisVersion = 1;
				break;
			
			default:
				supprimerFichierFavoris();	//�craser fichier de favoris en cas d'�chec
				currentFavorisVersion = 1;				
			}
			
			
		}

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context); 
		Editor edit = preferences.edit();
		edit.putInt("FavorisVersion", FavorisManager.FavorisCurrentVersion);
		edit.commit();

		
	}
	
	
	static void updateFavoris(Vector<String> strNameArray) throws IOException
	{
		String[] strArray;
		HashSet<String> strNameMap = new HashSet<String>(strNameArray.size());

		for (String name: strNameArray)
		{
			strNameMap.add(name);
		}
		try
		{
			BufferedFileReader in = FileHelpers.createBufferedFileInputStream(TransportProvider.getRootPath() + "favoris.dba", 4096);// 8
																																		// *
																																		// 512caracteres
			if (in != null)
			{
				String strLine = in.readLine();

				FileOutputStream fos = new FileOutputStream(TransportProvider.getRootPath() + "favorisTemp.dba", false);
				OutputStreamWriter out = new OutputStreamWriter(fos, "8859_1");


				Arret arret = null;
				while (strLine != null)
				{
					if(strLine.charAt(0) == 'f')
					{
						strArray = strLine.split(";");
						if (strArray.length == 9 && strNameMap.contains(strArray[1])) // On doit mettre � jour
						{
							arret = TransportProvider.ObtenirUnArret(strArray[1], strArray[2], strArray[8], strArray[3], strArray[7], strArray[4]);
							if(arret != null)
							{
								Favoris f = new Favoris();
								f.m_strTransportService = strArray[1];
								f.m_strNoBus = strArray[2];
								f.m_strDirection = strArray[3];
								f.m_strNoArret = strArray[4];
								f.m_strIntersection = arret.ObtenirNomRue1()+"/"+arret.ObtenirNomRue2();
								f.m_nLigneFavoris = Long.parseLong(arret.ObtenirPositionDansFichier());
								f.m_codeInfoDirection = strArray[7];
								f.m_codeInfoCircuit = strArray[8];
								f.m_vHoraire = TransportProvider.ObtenirListeHorairePourUnAutobus(f.m_strTransportService, f.m_strNoBus,  f.m_strDirection, f.m_codeInfoDirection, f.m_strNoArret, f.m_nLigneFavoris, f.m_codeInfoCircuit);
								if(f.m_vHoraire != null)
								{
									out.write(f.Serialize());
								}
							}
							else
							{
								
							}
							//We want to get ride of the old h so we ignore them
							strLine = in.readLine();
							while(strLine != null && strLine.charAt(0) == 'h')
							{	
								strLine = in.readLine();							
							}
						}
						else
						{
							out.write(strLine + "\n");
							strLine = in.readLine();
							while(strLine != null && strLine.charAt(0) == 'h')
							{								
								out.write(strLine + "\n");
								strLine = in.readLine();
							}
						}
					}
					else
					{
						if(strLine.length()>0)
							out.write(strLine + "\n");
						strLine = in.readLine(); //Ignore data						
					}	
				}

				in.close();
				out.close();
				// Swappage du nouveau fichier des favoris
				File ancienFavoris = new File(TransportProvider.getRootPath() + "favoris.dba");
				File nouveauFavoris = new File(TransportProvider.getRootPath() + "favorisTemp.dba");
				nouveauFavoris.renameTo(ancienFavoris);
			}

		} catch (IOException e)
		{
			throw e;
		}
	}

}
