package net.rhatec.amtmobile.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;

import android.content.Context;

import net.rhatec.amtmobile.file.BufferedFileReader;
import net.rhatec.amtmobile.helpers.FileHelpers;
import net.rhatec.amtmobile.providers.TransportProvider;
import net.rhatec.amtmobile.types.FileManager;
import net.rhatec.amtmobile.types.TransportServiceBase;
import net.rhatec.amtmobile.types.TransportServiceInfo;

public class ListeSocieteManager
{
	// TODO: faire des m�thodes g�n�ral pour �a et favoris

	public static Vector<TransportServiceBase> obtenirListe(Context c)
	{
		Vector<TransportServiceBase> transportVector = new Vector<TransportServiceBase>(10);
		BufferedFileReader in = FileHelpers.createBufferedFileInputStream(TransportProvider.getRootPath(c) + "listetransport.dba", 512);// 8
		int numCorrupted = 0;	
		String corruptedName = null;
	
		try
		{
			if (in != null)
			{
				String strLine = in.readLine();
				while (strLine != null)
				{
					String[] transport = strLine.split(";");
					if(transport.length == 4)
					{
						transportVector.add(new TransportServiceBase(transport[0], transport[1], transport[2], transport[3]));						
					}
					else
					{
						++numCorrupted;	
						if(transport.length>0)
							corruptedName = transport[0];
					}
					
					strLine = in.readLine();
				}
				in.close();
				if(numCorrupted != 0)
				{
					if(numCorrupted == 1)
					{
						supprimerSociete(c, corruptedName);
						Vector<String> v = new Vector<String>(1);
						v.add(corruptedName);
						FavorisManager.supprimerFavorisSociete(c, v);
						FileManager.deleteDir(TransportProvider.getRootPath(c) + corruptedName + "/");

					}
					else
					{
						FileManager.deleteDir(TransportProvider.getRootPath(c));
						transportVector.clear();
						
					}
				}
				
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return transportVector;
	}

	public static void ajouterSociete(Context c, TransportServiceBase _service)
	{

		boolean found = false;
		FileManager.creerFichierSiExistePas(TransportProvider.getRootPath(c) + "listetransport.dba");
		BufferedFileReader in = FileHelpers.createBufferedFileInputStream(TransportProvider.getRootPath(c) + "listetransport.dba", 512);// 8
		// *
		// 512carcateres
		try
		{
			if (in != null)
			{
				FileOutputStream fos = new FileOutputStream(TransportProvider.getRootPath(c) + "listetransportTemp.dba", false);
				OutputStreamWriter out = new OutputStreamWriter(fos, "8859_1");
				String strLine = in.readLine();
				while (strLine != null)
				{

					if (TransportServiceInfo.getKeyFromString(strLine).equals(_service.getKey())) //Null ptr possible
					{
						out.write(_service.serialize() + "\n");
						found = true;
					} else
					{
						out.write(strLine + "\n");
					}
					strLine = in.readLine();
				}
				in.close();
				if (!found)
				{
					out.write(_service.serialize() + "\n");  //Nul ptr possible
				}
				out.close();
				File ancienListeTransport = new File(TransportProvider.getRootPath(c) + "listetransport.dba");
				File nouveauListeTransport = new File(TransportProvider.getRootPath(c) + "listetransportTemp.dba");
				nouveauListeTransport.renameTo(ancienListeTransport);
				System.out.println("Transport Society Successfully added !");
			}

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	public static void supprimerSociete(Context c, String _nomCourt)
	{
		FileManager.creerFichierSiExistePas(TransportProvider.getRootPath(c) + "listetransport.dba");
		BufferedFileReader in = FileHelpers.createBufferedFileInputStream(TransportProvider.getRootPath(c) + "listetransport.dba", 512);// 8
																																		// *
																																		// 512carcateres
		try
		{
			if (in != null)
			{
				FileOutputStream fos = new FileOutputStream(TransportProvider.getRootPath(c) + "listetransportTemp.dba", false);
				OutputStreamWriter out = new OutputStreamWriter(fos, "8859_1");
				String strLine = in.readLine();
				while (strLine != null)
				{
					if (!TransportServiceInfo.getKeyFromString(strLine).equals(_nomCourt))
					{
						out.write(strLine + "\n");
					}
					strLine = in.readLine();
				}
				out.close();
				File ancienListeTransport = new File(TransportProvider.getRootPath(c) + "listetransport.dba");
				File nouveauListeTransport = new File(TransportProvider.getRootPath(c) + "listetransportTemp.dba");
				nouveauListeTransport.renameTo(ancienListeTransport);
				System.out.println("Transport Society Successfully delete !");
			}

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
