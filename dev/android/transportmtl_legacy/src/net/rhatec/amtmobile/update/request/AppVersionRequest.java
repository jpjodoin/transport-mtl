package net.rhatec.amtmobile.update.request;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppVersionRequest extends HttpRequest
{


	public AppVersionRequest(String updateServer, Context applicationContext)
	{
		super(updateServer, applicationContext);

	}

	@Override
	public String getRequest()
	{
		String applicationVersion = "0";
		PackageManager pm = m_context.getPackageManager();
		try
		{
			PackageInfo pi;
			// Version
			pi = pm.getPackageInfo(m_context.getPackageName(), 0);
			applicationVersion = pi.versionName;

		} catch (NameNotFoundException e)
		{
			applicationVersion = "0";
			e.printStackTrace();
		}
		return applicationVersion;
	}

	@Override
	public String response(String response)
	{
		return "";
	}

	@Override
	public String getRequestType()
	{
		return SERVER_IN_APPLICATION_STATUS;
	}

	@Override
	public String getReponseType()
	{
		return SERVER_OUT_APPLICATION_STATUS;
	}

}
