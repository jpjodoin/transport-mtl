package net.rhatec.amtmobile.manager;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import net.rhatec.amtmobile.file.BufferedFileReader;
import net.rhatec.amtmobile.helpers.FileHelpers;
import net.rhatec.amtmobile.providers.TransportProvider;
import net.rhatec.amtmobile.types.Pair;
import net.rhatec.amtmobile.types.VersionObjectType;

public class VersionManager 
{
	final static String FILENAME = "versionManager.dba";
	final static int NO_VERSION = 0;
	
	public static void loadVersionFromFile(Context c)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
	    File f = new File(TransportProvider.getRootPath(c)+"version.dba");
		if(f.exists())
		{
			preferences.edit().putInt(VersionObjectType.BOOKMARKS_VERSION.getKeyName(), 1);
			preferences.edit().putInt(VersionObjectType.DATABASE_VERSION.getKeyName(), 1);
			f.delete();
		}
		
		ArrayList<Pair<String, String>> list = TransportProvider.obtenirArrayListFichierMap(TransportProvider.getRootPath(c) +FILENAME);
		for(Pair<String,String> p : list)
		{
			preferences.edit().putInt(p.first, Integer.valueOf(p.second));
		}
		preferences.edit().commit();
	}
	
	public static void upgradeIfNeeded(Context c)
	{
		File RootFolder = new File(TransportProvider.getRootPath(c));
		RootFolder.mkdir();
		int currentAppDBAVersion = VersionObjectType.DATABASE_VERSION.getCurrentAppVersion();
		int currentDBAVersion = getCurrentDbaVersion(VersionObjectType.DATABASE_VERSION, c);
		if(currentDBAVersion < currentAppDBAVersion) //Version not uptodate
		{
			//DeleteFolderTask t = new DeleteFolderTask("sdcard/AmtTransportDatabase/"); //Commented for 0.961 since this can cause a Freeze for unknown reason
			//t.execute(); //Commented for 0.961 since this can cause a Freeze for unknown reason
			//ModalWaitDialog d = new ModalWaitDialog();
			//d.runTask(c, c.getResources().getString(R.string.PreferenceDlg_remise_a_zero_favoris_patienter), t);
			setVersion(VersionObjectType.DATABASE_VERSION, currentAppDBAVersion, c);
			setVersion(VersionObjectType.BOOKMARKS_VERSION, currentAppDBAVersion, c);
			//Delete Everything and next time we will do upgrade code for favorite and update
		}
		
	}
	
		
	
	public static int getCurrentDbaVersion(VersionObjectType type, Context c)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
		return preferences.getInt(type.getKeyName(), NO_VERSION);		
	}
	
	public static void setVersion(VersionObjectType type, int version, Context c)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
		String filePath = TransportProvider.getRootPath(c) + FILENAME;
		
		preferences.edit().putInt(type.getKeyName(), version).commit();
		BufferedFileReader in = FileHelpers.createBufferedFileInputStream(filePath);
		String keyName = type.getKeyName();
		
		
		HashMap<String, String> versionMap = new HashMap<String, String>();
		if (in != null)
		{
			try 
			{
				boolean found = false;
				String line = in.readLine();
				while (line != null)
				{
					String[] array = line.split(";");
					if(array.length == 2)
					{
						
						if(array[0].equals(keyName))
						{
							versionMap.put(array[0], String.valueOf(version));
							found = true;
						}
						else
							versionMap.put(array[0], array[1]);
					
					}
					line = in.readLine();
				}
				if(!found)
					versionMap.put(keyName, String.valueOf(version));

				in.close();
				OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath, false), "8859_1");
			    for (Map.Entry<String, String> entry: versionMap.entrySet()) 
			    {
			        out.write(entry.getKey() + ";" + entry.getValue() + "\n");
			    }
				

				out.close();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}		
		}		
	}
}
