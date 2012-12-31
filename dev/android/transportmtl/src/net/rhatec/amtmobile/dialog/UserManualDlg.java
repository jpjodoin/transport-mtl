package net.rhatec.amtmobile.dialog;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.baseactivity.ActivityWithMenu;


public class UserManualDlg extends ActivityWithMenu implements OnClickListener
{

	private WebView m_WebView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usermanual);
		final Activity activity = this;
		this.setTitle(this.getResources().getString(R.string.ManuelUtilisateur_Titre));
		m_WebView = (WebView) findViewById(R.id.UserManualWebView);
		m_WebView.loadUrl("file:///android_asset/"+activity.getResources().getString(R.string.ManuelUtilisateur_NomFichier));
		
		Button bTerminate = (Button) findViewById(R.id.bTerminer);
		bTerminate.setOnClickListener(this);
		
	}
	

	@Override
	public void onClick(View v)
	{
		if(v.getId()==R.id.bTerminer)
			finish();		
	}
}
