package net.rhatec.amtmobile.update.request;

import java.io.IOException;
import java.io.InputStream;

import net.rhatec.amtmobile.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;

public abstract class HttpRequest
{
	public static final String	SERVER_OUT_APPLICATION_STATUS	= "0";
	public static final String	SERVER_OUT_DATABASE_STATUS		= "1";
	public static final String	SERVER_OUT_WARNING				= "2";
	public static final String	SERVER_OUT_ERROR				= "3";
	public static final String	SERVER_OUT_VALIDITY_STATUS		= "4";

	public static final String	SERVER_IN_APPLICATION_STATUS	= "0";
	public static final String	SERVER_IN_DATABASE_STATUS		= "1";
	public static final String	SERVER_IN_GET_DATABASE			= "2";
	public static final String	SERVER_IN_GET_VALIDITY			= "3";
	public static final String	SERVER_IN_FUTUR_REQUEST			= "4";
	protected Context			m_context;
	private String				m_StartOfRequest;

	HttpRequest(String updateServer, Context applicationContext)
	{
		m_StartOfRequest = updateServer + "update.php?l=" + applicationContext.getResources().getConfiguration().locale.getLanguage();
		m_context = applicationContext;
	}

	abstract String getRequest();

	abstract String response(String response);

	abstract String getRequestType();

	abstract String getReponseType();

	public String execute()
	{
		String error = "";
		HttpClient httpClient = new DefaultHttpClient();
		StringBuilder uriBuilder = new StringBuilder(m_StartOfRequest);
		uriBuilder.append("&" + getRequestType() + "=");
		String requestString = getRequest();
		if (requestString == null) // We want to skip this request at this time
			return "";

		uriBuilder.append(requestString);
		HttpGet request = new HttpGet(uriBuilder.toString());
		HttpResponse response;
		try
		{
			response = httpClient.execute(request);
			// Point possible d'annulation
			InputStream ResponseInputStream = response.getEntity().getContent();
			long content_size = response.getEntity().getContentLength();
			byte[] content = new byte[(int) content_size];
			ResponseInputStream.read(content);
			String Result = new String(content, "8859_1");

			if (Result != null)
			{
				int pos = Result.indexOf(";");
				//TODO: Throw exception en cas de mauvaise réponse ici...
				String requestType = Result.substring(0, pos);
				if (pos != -1 && requestType.contentEquals(getReponseType()))
				{
					error = response(Result.substring(pos + 1, Result.length()));
				} else if (requestType.contentEquals(SERVER_OUT_WARNING))
				{
					// Todo Call Warning Dialog
				} else if (requestType.contentEquals(SERVER_OUT_ERROR))
				{

					error = Result.substring(pos + 1, Result.length() - (pos + 1));
				}
			}
		} catch (ClientProtocolException e)
		{
			error = "ClientProtocolException: " + e.getMessage();
		} catch (IOException e)
		{			
			error = m_context.getString(R.string.UpdateManager_serveur_down) + "\n\n"+ m_context.getString(R.string.UpdateManager_more_information)+ "\nIOException: " + e.getMessage();
		} catch (ArrayIndexOutOfBoundsException e)
		{
			error = m_context.getResources().getString(R.string.UpdateManager_donnees_recus_format_incompatbile) + "\n"
					+ m_context.getResources().getString(R.string.UpdateManager_Essayer_mettre_a_jour_application);

		} catch (NumberFormatException e)
		{
			error = m_context.getResources().getString(R.string.UpdateManager_donnees_recus_format_incompatbile) + "\n"
					+ m_context.getResources().getString(R.string.UpdateManager_Essayer_mettre_a_jour_application);

		} catch (Exception e)
		{
			// TODO: Throw back runtime exception ??
			error = m_context.getResources().getString(R.string.UpdateManager_donnees_recus_format_incompatbile) + "\n"
					+ m_context.getResources().getString(R.string.UpdateManager_Essayer_mettre_a_jour_application);
		}
		
		m_context = null; //We derefence the context to avoid leak
		
		return error;
	}
}
