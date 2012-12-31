package net.rhatec.amtmobile.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class TransportContentProvider extends ContentProvider
{

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0)
	{
		// TODO retourner MIME Type
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1)
	{
		// TODO Auto-generated method stub. On ne veut pas qu'il puisse insérer
		// des données
		return null;
	}

	@Override
	public boolean onCreate()
	{
		// On retourne vrai si le provider a été créer avec succès
		return true;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3)
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
