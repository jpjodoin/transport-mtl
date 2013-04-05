package net.rhatec.amtmobile.update.request;

import java.util.Vector;

import android.content.Context;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.providers.TransportProvider;
import net.rhatec.amtmobile.types.UpdateStruct;

public class DBVersionRequest extends HttpRequest
{
	private Context mApplicationContext;
	public DBVersionRequest(String updateServer, Context applicationContext)
	{
		super(updateServer, applicationContext);
		mApplicationContext = applicationContext;
	}

	private Vector<UpdateStruct>	m_vUpdate	= new Vector<UpdateStruct>();

	public Vector<UpdateStruct> getUpdateVector()
	{
		return m_vUpdate;
	}

	@Override
	public String getRequest()
	{
		// Vector<TransportServiceBase> vector =
		// ListeCircuitManager.ObtenirListe();
		String request = ""; // We want to search for all of them
		/*
		 * if(!vector.isEmpty()) { StringBuilder uriBuilder = new
		 * StringBuilder(""); for(int i=0; i<vector.size(); ++i) { String
		 * shortName = vector.get(i).getShortName(); uriBuilder.append(shortName
		 * + ";" + TransportProvider.GetVersionNumber(shortName)+ ";"); }
		 * request = uriBuilder.toString(); }
		 */

		return request;
	}

	@Override
	public String response(String response)
	{
		String error = "";
		String[] strResult = response.split(";");
		int arraySize = strResult.length;
		if ((arraySize % 3) == 0)
		{
			for (int i = 0; i < arraySize; i = i + 3)
			{
				if (!strResult[i + 2].contentEquals(TransportProvider.GetVersionNumber(mApplicationContext, strResult[i])))
				{
					UpdateStruct struct = new UpdateStruct();
					struct.m_strShortName = strResult[i];
					struct.m_nSize = strResult[i + 1];
					struct.m_IsChecked = TransportProvider.DatabaseExist(mApplicationContext, strResult[i]);
					m_vUpdate.add(struct);
				}
			}
		} else
			error = m_context.getString(R.string.UpdateManager_protocole_non_supporte);
		return error;
	}

	@Override
	public String getRequestType()
	{
		return SERVER_IN_DATABASE_STATUS;
	}

	@Override
	public String getReponseType()
	{
		return SERVER_OUT_DATABASE_STATUS;
	}

}
