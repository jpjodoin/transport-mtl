package net.rhatec.amtmobile.types;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileManager
{
	/**
	 * Vérifie si le fichier existe et le crée dans le cas contraire.
	 * 
	 */
	public static boolean creerFichierSiExistePas(String nomFichier)
	{
		boolean success = false;
		try
		{	
			File f = new File(nomFichier);
			
			if(!f.exists()) //We will create the directories
			{
				String path = f.getParent();
				if(path != null)
				{
					File fPath = new File(path);
					fPath.mkdirs(); //We create the directories and the file				
					FileOutputStream fos = new FileOutputStream(nomFichier, true);
					fos.close();
					success = true;				
				}
				
				
			}
			
			//
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	// Delete directory
	public static boolean deleteDir(String strFile)
	{
		// Declare variables variables
		File fDir = new File(strFile);
		String[] strChildren = null;
		boolean bRet = false;

		// Validate directory
		if (fDir.isDirectory())
		{
			// -- Get children
			strChildren = fDir.list();

			// -- Go through each
			for (String childName: strChildren)
			{
				bRet = deleteDir(new File(fDir, childName).getAbsolutePath());
				if (!bRet)
				{
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return fDir.delete();
	}

	public static boolean extraireDba(String _srcFilePath, String _destFolder)
	{
		try
		{
			ZipInputStream zin = new ZipInputStream(new FileInputStream(_srcFilePath));
			ZipEntry entry;

			BufferedOutputStream dest = null;

			while ((entry = zin.getNextEntry()) != null)
			{
				String strFileName = entry.getName();

				int count;
				if (strFileName.charAt(strFileName.length() - 1) == '/')
				{
					File folder = new File(_destFolder + strFileName);
					folder.mkdir();
				} else
				{
					byte data[] = new byte[2048];
					FileOutputStream fos = new FileOutputStream(_destFolder + strFileName);
					dest = new BufferedOutputStream(fos, 2048);
					while ((count = zin.read(data, 0, 2048)) != -1)
					{
						dest.write(data, 0, count);
					}
					dest.flush();
					dest.close();
				}
			}

			zin.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

}
