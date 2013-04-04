package net.rhatec.amtmobile.providers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import net.rhatec.amtmobile.file.BufferedFileReader;
import net.rhatec.amtmobile.file.FileReaderIface;
import net.rhatec.amtmobile.helpers.FileHelpers;
import net.rhatec.amtmobile.types.Arret;
import net.rhatec.amtmobile.types.Autobus;
import net.rhatec.amtmobile.types.Horaire;
import net.rhatec.amtmobile.types.HoraireArret;
import net.rhatec.amtmobile.types.Pair;
import net.rhatec.amtmobile.types.TransportServiceInfo;

public class TransportProvider
{
	
	
	//static String	m_RootPath	= Environment.getExternalStorageDirectory().getAbsolutePath()+"/transportmtldata/";//"sdcard/transportmtldata/";
	public final static String NOKEYFOUNDINMAP = " ";
	static public String getRootPath(Context c)
	{
		String rootPath = "";
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
		String pref = preferences.getString("storage", "external");
		if(pref.equals("internal"))
		{
			rootPath = c.getFilesDir().getAbsolutePath();
		}
		else if(pref.equals("external2"))
		{
			rootPath = "/extSdCard";
		}
		else //external
		{			
			rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		rootPath += "/transportmtldata/";
		return rootPath;
	}

	/**
	 * M�thode qui parse les fichiers comme "listecircuit.dba" et retourne le
	 * numero de version
	 * 
	 * @param _strName
	 * @return Retourne le numero de version
	 */
	// Version;NomComplet;Type; Affichage Extra Info (0 si on ne l'affiche pas,
	// 1 si on l'affiche) ;
	static public String GetVersionNumber(Context c,String _strName)
	{
		String version = "0";
		try
		{
			BufferedFileReader in = FileHelpers.createBufferedFileInputStream(getRootPath(c) + _strName + "/" + "info.dba");
			if (in != null)
			{
				String strLine = in.readLine();
				String[] infoArray = strLine.split(";");
				if (infoArray.length != 0)
					version = infoArray[0];
				in.close();
			}

		} catch (IOException e)
		{
		} catch (NullPointerException e)
		{
		}

		return version;
	}

	static public TransportServiceInfo ObtenirTransportService(Context c,String _strName)
	{
		TransportServiceInfo service = null;
		try
		{
			BufferedFileReader in = FileHelpers.createBufferedFileInputStream(getRootPath(c) + _strName + "/" + "info.dba");
			if (in != null)
			{
				String strLine = in.readLine();
				String[] infoArray = strLine.split(";");
				// Version;NomComplet;Type; Affichage Extra
				// Info;AffichageRecherche;AffichageCarte;
				if (infoArray.length == 8)
					service = new TransportServiceInfo(_strName, infoArray[2], infoArray[0], "0", infoArray[3], infoArray[4].equals("1") ? true : false, infoArray[5].equals("1") ? true : false,
							infoArray[6].equals("1") ? true : false, infoArray[7].equals("1") ? true : false/*, infoArray[8].equals("1") ? true : false*/);
				in.close();
			}

		} catch (IOException e)
		{
		} catch (NullPointerException e)
		{
		}

		return service;
	}

	static public boolean DatabaseExist(Context c,String strName)
	{
		boolean fSuccess = false;
		BufferedFileReader in = FileHelpers.createBufferedFileInputStream(getRootPath(c) + strName + "/" + "info.dba");
		if (in != null)
		{
			try
			{
				String strLine = in.readLine();
				if (strLine != null && strLine.length() != 0)
					fSuccess = true;
				in.close();
			} catch (IOException e)
			{
			} catch (NullPointerException e)
			{
			}
		}
		return fSuccess;
	}

	/**
	 * M�thode qui parse les fichiers comme "listecircuit.dba" et g�n�re un
	 * vecteur d'autobus
	 * 
	 * @param _nomSocieteTransport
	 * @return Retourne un vecteur d'objet autobus
	 */
	static public Vector<Autobus> ObtenirListeCircuit(Context c, String _nomSocieteTransport)
	{
		Vector<Autobus> vAutobus = new Vector<Autobus>(50);
		try
		{
			BufferedFileReader in = FileHelpers.createBufferedFileInputStream(getRootPath(c) + _nomSocieteTransport + "/" + "listecircuit.dba");
			String strLine = in.readLine();
			while (strLine != null)
			{
				String[] strAutobus = strLine.split(";");
				if (strAutobus.length == 6) // Num�ro, Extrainfo circuit Direction, Nom, Extrainfo direction
					vAutobus.add(new Autobus(strAutobus[0], strAutobus[2], strAutobus[3], strAutobus[4], strAutobus[1], strAutobus[5]));
				
				strLine = in.readLine();
			}
			in.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return vAutobus;
	}
	
	
	

	/**
	 * M�thode qui parse les fichiers comme "listecircuit.dba" et g�n�re un
	 * vecteur d'autobus avec uniquement les autobus dans
	 * 
	 * @param strTransportNom
	 * @return Retourne un vecteur d'objet autobus pour une ligne specifique
	 */
	static public Vector<Autobus> ObtenirListeCircuitParNumero(Context c, String _nomSocieteTransport, String _noAutobus)
	{
		Vector<Autobus> vAutobus = new Vector<Autobus>(50);
		try
		{
			BufferedFileReader in = FileHelpers.createBufferedFileInputStream(getRootPath(c) + _nomSocieteTransport + "/" + "listecircuit.dba");
			String strLine = in.readLine();
			while (strLine != null)
			{
				String[] strAutobus = strLine.split(";");
				if (_noAutobus.equals(strAutobus[0]) && strAutobus.length == 6) // Num�ro, Extra info circuit, Direction, Nom, Extrainfo
					vAutobus.add(new Autobus(strAutobus[0], strAutobus[2], strAutobus[3], strAutobus[4], strAutobus[1], strAutobus[5]));
				strLine = in.readLine();
			}
			in.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return vAutobus;
	}

	/**
	 * Fonction qui retourne un vecteur avec la liste des arr�ts
	 * 
	 * @param _nomSocieteTransport
	 *            Nom du Service de transport en commun
	 * @param _strCircuit
	 *            No du circuit
	 * @param _strDirection
	 *            Direction du circuit
	 * @return Vecteur contenant les arr�ts des fichiers ex: "42n.dba"
	 */
	//TODO: Methode qui retourne les noms de fichier en fonction des arguments...
	public static Vector<Arret> ObtenirListeArret(Context c, String _nomSocieteTransport, String _strCircuit, String infoCircuitCode,String _strDirection, String infoDirectionCode)
	{
		Vector<Arret> vArret = new Vector<Arret>(10);
		try
		{
			BufferedFileReader in = FileHelpers.createBufferedFileInputStream(getRootPath(c) + _nomSocieteTransport + "/listecircuit/" + _strCircuit + infoCircuitCode + _strDirection + infoDirectionCode + ".dba");
			if (in != null)
			{
				String strLine = in.readLine();
				//ArretId;Inter1;Inter2;CodeTrainMetro;Lat;Long;PosInFile
				// 53833;Ridgewood;No 3635;0;1749348;
				while (strLine != null)
				{
					String[] strArret = strLine.split(";"); // Num�ro d'arr�t, Rue1, Rue2, Enum M�tro, Train ou	 Bus, Position dans fichier
					vArret.add(new Arret(strArret[0], strArret[1], strArret[2], strArret[3], strArret[4], strArret[5], strArret[6]));
					strLine = in.readLine();
				}
				in.close();
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return vArret;
	}

	/**
	 * Fonction qui retourne un arr�t dans une liste
	 * 
	 * @param societe
	 *            Nom du Service de transport en commun
	 * @param noCircuit
	 *            No du circuit
	 * @param direction
	 *            Direction du circuit
	 * @param infoDirection
	 *            Extrainfo du circuit
	 * @param noArret
	 *            Num�ro de l'arr�t d�sir�
	 * @return Vecteur contenant les arr�ts des fichiers ex: "42n.dba"
	 */
	public static Arret ObtenirUnArret(Context c, String societe, String noCircuit, String infoCircuit, String direction, String infoDirection, String noArret)
	{
		Arret arret = null;
		try
		{
			BufferedFileReader in = FileHelpers.createBufferedFileInputStream(getRootPath(c) +
					societe + "/listecircuit/" + noCircuit + infoCircuit +  direction  + infoDirection
					+ ".dba");
			String strLine = in.readLine();
			// 41351;DE LA CONCORDE;FACE AU MAISON DES ARTS;Si il y a un train ou m�tro � l'arr�t;position en octet dans le fichier
			while (strLine != null)
			{
				String[] strArret = strLine.split(";");
				if (noArret.contentEquals(strArret[0])) // On a trouv� l'arr�t
				{
					arret = new Arret(strArret[0], strArret[1], strArret[2], strArret[3], strArret[4], strArret[5], strArret[6]);
					strLine = null;
				} else
				{
					strLine = in.readLine();
				}
			}
			in.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return arret;
	}

	/**
	 * Retourne tous les horaires de tous les autobus d'un arr�t
	 * 
	 * @param _nomSocieteTransport
	 * @param _strArret
	 * @return HoraireArret
	 */
	public static HoraireArret ObtenirListeHoraireAutobus(Context c, String _nomSocieteTransport, String _strArret)
	{
		return ObtenirListeHoraireAutobus(c, _nomSocieteTransport, _strArret, -1); // Position dans le fichier inconnu
	}

	
	public static Vector<Horaire> ObtenirListeHorairePourUnAutobus(Context c, String _nomSocieteTransport, String noAutobus, String direction, String extrainfodirection, String _strArret, long _lineNumber, String extrainfocircuit)
	{
		Vector<Horaire> vHoraire = null;
		HoraireArret ha = ObtenirListeHoraireAutobus(c, _nomSocieteTransport, _strArret, _lineNumber);
		if(ha.ObtenirErreur() == HoraireArret.AUCUNE_ERREUR)
		{
			Vector<Autobus> busVect = ha.ObtenirAutobus();
			busloop: //Loop label
			for (Autobus a : busVect )
			{
				if(a.ObtenirNumero().equals(noAutobus) && a.ObtenirDirectionCode().equals(direction) && a.ObtenirInfoDirectionCode().equals(extrainfodirection) && a.ObtenirInfoCircuitCode().equals(extrainfocircuit))
				{
					vHoraire = a.ObtenirVecteurHoraire();
					break busloop; //We exit this loop
				}				
			}						
		}
		return vHoraire;			
	}
	
	
	
	/**
	 * Faster version when you already know at which line in the file is the
	 * data
	 * 
	 * @param societe
	 * @param noarret
	 * @return HoraireArret
	 * @warnings Doit lancer une exception ou bien retourner un bool. L'arret
	 *           peut ne plus exister !!!!
	 */
	public static HoraireArret ObtenirListeHoraireAutobus(Context c, String societe, String noarret, long posfichier)
	{
		Vector<Autobus> vAutobus = new Vector<Autobus>(3);
		HoraireArret vHoraireArret = new HoraireArret();
		FileReaderIface in = null;
		try
		{

			if (posfichier != -1)
			{
				in = FileHelpers.createRandomAccessFileAndSeek(getRootPath(c) + societe + "/listearret/horaire.dba", posfichier);
			} else
			{
				in = FileHelpers.createBufferedFileInputStream(getRootPath(c) + societe + "/listearret/horaire.dba");

			}

			if (in != null)
			{
				String strLine = in.readLine();
				while (strLine != null && !strLine.contains("a;" + noarret + ";")) // On trouve l'arret dans le fichier
				{
					strLine = in.readLine();
				}
				if (strLine == null)
					vHoraireArret.SetErreur(HoraireArret.ARRET_NON_TROUVE);
				else
				{
					vHoraireArret.SetBytePosition(in.getBytePosition() - strLine.length() - 2);
					String[] strArray = strLine.split(";");
					// a;45003;Gare Sainte Doroth�e ; ;1;		
				
					vHoraireArret.SetIntersection(strArray[2]);
					strLine = in.readLine();
					if (strLine != null && strLine.charAt(0) != 'a')
					{
						String[] strHoraire = strLine.split(";");
						while (strLine != null && strLine.charAt(0) != 'a')
						{
							if (strHoraire.length > 3)
							{
								//num�ro-extrainfocircuit-direction-extrainfodirection-nom-extrainfohoraire-heures
								String AutobusNoCourant = strHoraire[0];
								String AutobusDirCourant = strHoraire[2];
								String AutobusExtraInfoCircuit = strHoraire[1];
								String AutobusExtraInfoDirection = strHoraire[3];
								
								Autobus cAutobus = new Autobus(AutobusNoCourant, AutobusDirCourant, null, AutobusExtraInfoDirection, AutobusExtraInfoCircuit, "000000");

								/*
								 * a;45003;Gare Sainte Doroth�e ; ;1;
								 * 404;e;null;
								 * 1;;1536;1625;1711;1734;1749;1812;1828;1944;
								 * 804;e;null;3;;0823;0953;1023;1123;1211;1651;
								 * a;45004;Les Erables;Les Ormes;0;
								 */
								while  (strLine != null 
								    && (strHoraire[0].equals(AutobusNoCourant)) 
									&& (strHoraire[1].equals(AutobusExtraInfoCircuit)) 
									&& (strHoraire[2].equals(AutobusDirCourant)) 
									&& (strHoraire[3].equals(AutobusExtraInfoDirection))
									 && strLine.charAt(0) != 'a')
								{
									int length = strHoraire.length; //Faster if we avoid look up
									if(length > 5)
									{
										Horaire cHoraire = new Horaire(strHoraire[4], strHoraire[5]);
										//http://developer.android.com/guide/practices/design/performance.html
										
										for (int i = 6; i < length; ++i)
										{
											cHoraire.AjouterHeure(strHoraire[i]);
										}
										cAutobus.AjouterHoraire(cHoraire);
										strLine = in.readLine();
										if (strLine != null)
										{
											strHoraire = strLine.split(";");
										} else
										{
											strLine = null;
										}
									}
									
								}
								vAutobus.add(cAutobus);
							} else
							{
								vHoraireArret.SetErreur(HoraireArret.DONNEES_MAL_FORMATEE);
							}
						}
					} else
					{
						vHoraireArret.SetErreur(HoraireArret.DONNEES_MAL_FORMATEE);
					}
				}
				in.close();
			} else
			{
				vHoraireArret.SetErreur(HoraireArret.DEBARCADERE_SEULEMENT);
			}
		} catch (IOException e)
		{
			vHoraireArret.SetErreur(HoraireArret.IOEXCEPTION);
			e.printStackTrace();
		}

		vHoraireArret.SetAutobus(vAutobus);

		return vHoraireArret;

	}

	/**
	 * 
	 * @param _nomSocieteTransport
	 *            nom du reseau de transport
	 * @param infoDirectionCode
	 *            code de l<extra info
	 * @return Une map de l'extra
	 */
	public static String ObtenirPhraseInfoDirectionSpecifique(Context c, String _nomSocieteTransport, String infoDirectionCode)
	{
		return StringDeMapAvecCleSpecifique(getRootPath(c) + _nomSocieteTransport + "/" + "listeextrainfodirection.dba", infoDirectionCode);
	}

	public static String ObtenirPhraseInfoCircuitSpecifique(Context c, String _nomSocieteTransport, String infoCircuitCode)
	{
		return StringDeMapAvecCleSpecifique(getRootPath(c) + _nomSocieteTransport + "/" + "listeextrainfocircuit.dba", infoCircuitCode);
	}
	
	
	public static String StringDeMapAvecCleSpecifique(String _pathFichier, String cle)
	{
		BufferedFileReader in;
		String[] strArray =
		{ "", "" };
		String result = NOKEYFOUNDINMAP;
		try
		{
			in = FileHelpers.createBufferedFileInputStream(_pathFichier);
			if (in != null)
			{
				String strLine = in.readLine();
				// exemple de la structure dans le fichier 101;Trajet apr�s 21h;
				while (strLine != null)
				{
					strArray = strLine.split(";");
					strLine = in.readLine();
					if (strArray[0].equals(cle) == true)
					{
						result = strArray[1];
						strLine = null;
					}
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return result;
	}
	
	
	
	/**
	 * Retourne un hashmap des num�ros de circuit/Nom d'autobus
	 * 
	 * @param _nomSocieteTransport
	 * @return
	 */
	public static HashMap<String, String> ObtenirMapNomCircuit(Context c, String _nomSocieteTransport)
	{
		HashMap<String, String> vAutobus = new HashMap<String, String>();

		try
		{
			BufferedFileReader in = FileHelpers.createBufferedFileInputStream(getRootPath(c) + _nomSocieteTransport + "/listecircuit.dba");
			String strLine = in.readLine();
			while (strLine != null)
			{
				String[] strAutobus = strLine.split(";");
				vAutobus.put(strAutobus[0], strAutobus[3]);
				strLine = in.readLine();
			}
			in.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return vAutobus;
	}

	/**
	 * Rempli un hash map des phrase d'extra info des horaires d'une soci�t� de
	 * transport
	 * 
	 * @param _nomSocieteTransport
	 * @return Une map de l'extra enfo du reseau demand�
	 */
	public static HashMap<String, String> ObtenirPhraseInfoHoraire(Context c, String _nomSocieteTransport)
	{
		return obtenirHashMapFichier(getRootPath(c) + _nomSocieteTransport + "/" + "listeextrainfohoraire.dba");
	}
	
	public static HashMap<String, String> ObtenirPhraseInfoCircuit(Context c, String _nomSocieteTransport)
	{
		return obtenirHashMapFichier(getRootPath(c) + _nomSocieteTransport + "/" + "listeextrainfocircuit.dba");
	}

	/**
	 * 
	 * @param _nomSocieteTransport
	 * @return Une map de la correspondance jour f�ri�-date du reseau demand�
	 */
	public static HashMap<String, String> ObtenirMapJourFerie(Context c, String _nomSocieteTransport)
	{
		return obtenirHashMapFichier(getRootPath(c) + _nomSocieteTransport + "/" + "feries.dba");
	}


	public static HashMap<String, String> ObtenirPhraseInfoDirection(Context c, String _nomSocieteTransport)
	{
		return obtenirHashMapFichier(getRootPath(c) + _nomSocieteTransport + "/" + "listeextrainfodirection.dba");
	}

	
	public static ArrayList<Pair<String, String>> obtenirArrayListFichierMap(String _pathFichier)
	{
		ArrayList<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
		try
		{
			BufferedFileReader in = FileHelpers.createBufferedFileInputStream(_pathFichier);
			if (in != null)
			{
				String strLine = in.readLine();
				while (strLine != null)
				{
					String[] strArray = strLine.split(";");
					Pair<String, String> p = new Pair<String, String>(strArray[0], strArray[1]);
					list.add(p);
					strLine = in.readLine();
				}
				in.close();
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return list;
	}
	
	public static HashMap<String, String> obtenirHashMapFichier(String _pathFichier)
	{
		BufferedFileReader in;
		String[] strArray =
		{ "", "" };
		HashMap<String, String> ExtraInfoMap = new HashMap<String, String>();
		try
		{
			in = FileHelpers.createBufferedFileInputStream(_pathFichier);
			if (in != null)
			{
				String strLine = in.readLine();
				while (strLine != null)
				{
					strArray = strLine.split(";");
					ExtraInfoMap.put(strArray[0], strArray[1]);
					strLine = in.readLine();
				}
				in.close();
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return ExtraInfoMap;
	}
}
