package net.rhatec.amtmobile.helpers;

import java.util.Vector;

import net.rhatec.amtmobile.R;

import android.content.Context;

public class StringHelpers
{
	static public String charDirectionToViewableString(String _strChar, Context _context)
	{

		String strDirection = null;
		switch (_strChar.charAt(0))
		{
		case 'n':
			strDirection = _context.getString(R.string.General_Nord);
			break;
		case 's':
			strDirection = _context.getString(R.string.General_Sud);
			break;
		case 'e':
			strDirection = _context.getString(R.string.General_Est);
			break;
		case 'o':
			strDirection = _context.getString(R.string.General_Ouest);
			break;
		case 'c':
			strDirection = _context.getString(R.string.General_Centre);
			break;
		case 'x':
			strDirection = _context.getString(R.string.General_Ext);
			break;
		case 'a':
			strDirection = " "; // Extra info, la direction n'est donc pas
								// pertinente
			break;
		default:
			strDirection = _strChar;
		}
		return strDirection;
	}

	static public String stringDirectionToChar(String strDirection, Context _context)
	{

		String strDirectionChar = null;
		if (strDirection.contains(_context.getString(R.string.General_Nord)))
			strDirectionChar = "n";
		else if (strDirection.contains(_context.getString(R.string.General_Sud)))
			strDirectionChar = "s";
		else if (strDirection.contains(_context.getString(R.string.General_Est)))
			strDirectionChar = "e";
		else if (strDirection.contains(_context.getString(R.string.General_Ouest)))
			strDirectionChar = "o";
		else if (strDirection.contains(_context.getString(R.string.General_Ext)))
			strDirectionChar = "x";
		else if (strDirection.contains(_context.getString(R.string.General_Centre)))
			strDirectionChar = "c";
		else if (strDirection.contains(" ")) // Extra info, la direction n'est donc pas pertinente
			strDirectionChar = "a";

		return strDirectionChar;
	}

	/**
	 * Fonction qui prend un vecteur de string et qui les separer avec un espace
	 * Ou bien un saut de ligne apres X strings
	 * 
	 * @param newLineAfterPosition
	 *            Position apres laquelle on met un "\n"
	 * @param vect
	 *            vecteur de string
	 * @return string avec les Strings contenu dans le vecteur
	 */
	static public String putSpaceOrNewLineBetweenStrings(Vector<String> vect, int newLineAfterPosition, String lineSeparator)
	{
		StringBuffer stringRetour = new StringBuffer(); 
		if(!vect.isEmpty())
		{
			stringRetour.append(vect.elementAt(0));
			int vectSize = vect.size();
			for (int i = 1; i < vectSize; ++i)
			{
				if (i % newLineAfterPosition == 0)
					stringRetour.append(lineSeparator);
				else
						stringRetour.append(" ");
				
				stringRetour.append(vect.elementAt(i));
			}			
		}
		
		return stringRetour.toString();
	}
}
