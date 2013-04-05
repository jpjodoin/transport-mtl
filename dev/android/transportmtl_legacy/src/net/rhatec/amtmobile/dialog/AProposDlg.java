package net.rhatec.amtmobile.dialog;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.baseactivity.ActivityWithMenu;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

/*
 * Boîte de dialogue de recherche d'arrêt, de mise à jour et possédant un accès à la liste d'autobus 
 */
public class AProposDlg extends ActivityWithMenu
{

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apropos);
		TextView lblRhatec = (TextView) findViewById(R.id.WebRhatec);
		TextView lblDavid = (TextView) findViewById(R.id.WebDavidCerat);
		TextView lblSupport = (TextView) findViewById(R.id.EmailSupport);
		Linkify.addLinks(lblRhatec, Linkify.WEB_URLS);
		Linkify.addLinks(lblDavid, Linkify.WEB_URLS);
		Linkify.addLinks(lblSupport, Linkify.EMAIL_ADDRESSES);

	}
}
