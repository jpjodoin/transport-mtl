package net.rhatec.amtmobile.helpers;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.rhatec.amtmobile.file.BufferedFileReader;
import net.rhatec.amtmobile.file.RandomFileReader;
import net.rhatec.amtmobile.types.FileManager;

public class FileHelpers
{

	static public BufferedFileReader createBufferedFileInputStream(String _fileName)
	{
		return createBufferedFileInputStream(_fileName, -1);
	}

	static public BufferedFileReader createBufferedFileInputStream(String _fileName, int _size)
	{
		BufferedFileReader inputReader;
		try
		{
			FileManager.creerFichierSiExistePas(_fileName);
			if (_size != -1)
				inputReader = new BufferedFileReader(_fileName, _size);
			else
				inputReader = new BufferedFileReader(_fileName); //TODO Adjust size for default
		} catch (UnsupportedEncodingException e)
		{
			inputReader = null;
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			inputReader = null;
			e.printStackTrace();
		}

		return inputReader;
	}

	static public RandomFileReader createRandomAccessFileAndSeek(String _fileName, long _position)
	{
		RandomFileReader inputReader = null;
		try
		{
			inputReader = new RandomFileReader(_fileName);
			inputReader.seek(_position);
		}

		catch (FileNotFoundException e)
		{
			inputReader = null;
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return inputReader;
	}

}
