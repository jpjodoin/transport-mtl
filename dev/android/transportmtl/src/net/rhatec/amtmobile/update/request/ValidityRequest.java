package net.rhatec.amtmobile.update.request;

import java.io.File;
import java.util.Vector;
import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.manager.ListeSocieteManager;
import net.rhatec.amtmobile.providers.TransportProvider;
import net.rhatec.amtmobile.types.TransportServiceBase;
import net.rhatec.amtmobile.types.TransportServiceInfo;
import android.content.Context;

public class ValidityRequest extends HttpRequest
{

	private Context mApplicationContext;
	public ValidityRequest(String updateServer, Context applicationContext)
	{
		super(updateServer, applicationContext);
		mApplicationContext = applicationContext;
	}

	@Override
	public String getRequest()
	{
		Vector<TransportServiceBase> vector = ListeSocieteManager.obtenirListe(mApplicationContext);
		String request = null;
		if (!vector.isEmpty())
		{
			StringBuilder uriBuilder = new StringBuilder("");
			// http://developer.android.com/guide/practices/design/performance.html
			//Fastest loop you can do
			for(TransportServiceBase a: vector)
			{
				String shortName = a.getShortName();
				uriBuilder.append(shortName + ";" + TransportProvider.GetVersionNumber(mApplicationContext, shortName) + ";");
			}

			request = uriBuilder.toString();
		}

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
			File RootFolder = new File(TransportProvider.getRootPath(mApplicationContext));
			RootFolder.mkdir();
			for (int i = 2; i < arraySize; i = i + 3)
			{
				TransportServiceInfo service = TransportProvider.ObtenirTransportService(mApplicationContext, strResult[i - 2]);
				service.setdebutValidite(strResult[i - 1]);
				service.setFinValidite(strResult[i]);
				ListeSocieteManager.ajouterSociete(mApplicationContext, service);
			}
		} else
			error = m_context.getString(R.string.UpdateManager_protocole_non_supporte);
		return error;

	}

	@Override
	public String getRequestType()
	{
		return SERVER_IN_GET_VALIDITY;
	}

	@Override
	public String getReponseType()
	{
		return SERVER_OUT_VALIDITY_STATUS;
	}

}
