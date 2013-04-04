package net.rhatec.amtmobile.dialog;

import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.manager.EulaManager;
import net.rhatec.amtmobile.manager.ListeSocieteManager;
import net.rhatec.amtmobile.manager.UpdateManager;
import net.rhatec.amtmobile.manager.VersionManager;
import net.rhatec.amtmobile.providers.TransportProvider;
import net.rhatec.amtmobile.types.FileManager;

public class MainDlg extends Activity implements EulaManager.OnEulaAgreedTo
{

	public final int 					MANUEL 				= 1;
	public final String 				FIRST_RUN_PREFERENCE = "ISFIRSTRUN";
	ProgressDialog						m_Dialog;
	String								m_TransportLock;
	int									m_StateProgress;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if(EulaManager.show(this))
		{
			createView();
		}
		
		
	}
	
	void createView()
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		if (preferences.getBoolean(FIRST_RUN_PREFERENCE, true))
		{
			startActivityForResult(new Intent(this, UserManualDlg.class), 1);
			preferences.edit().putBoolean(FIRST_RUN_PREFERENCE, false).commit();
			VersionManager.loadVersionFromFile(this); //Chargement des no de version enregistr� dans un fichier dans les pr�f�rences
		}
		else
		{
			
			
			
			
			m_TransportLock = UpdateManager.ReadLock(this); // En cas de corruption de la base de donn�e lors de la mise � jour, il y aura un nom
			if (m_TransportLock != null)
			{
				m_Dialog = ProgressDialog.show(this, "",
						getResources().getString(R.string.MainDlg_DBACorruption_Pre) + " " + m_TransportLock + getResources().getString(R.string.MainDlg_DBACorruption_Post), true);
				RemoveLock();
			}
			

			VersionManager.upgradeIfNeeded(this);	
	
		
			
			
			//Forward to Accueil choisi
			String pref = preferences.getString("startPage", "select-transport");
			Intent iMiseAJour;
			if(pref.equals("select-transport"))
				iMiseAJour = new Intent(this, TransportSelectDlg.class); 

			else
				iMiseAJour = new Intent(this, FavorisDlg.class); 
					
			startActivity(iMiseAJour);
			finish(); // �vite que l'activit� vide soit dans le stack d'activit� :)
		}		
	}
	

	void RemoveLock()
	{
		final Context c = this;
		Thread runningThread = new Thread() {
			@Override
			public void run()
			{
				m_StateProgress = 0;
				mHandler.post(mUpdateStatusDBA);
				ListeSocieteManager.supprimerSociete(c,m_TransportLock);
				FileManager.deleteDir(TransportProvider.getRootPath(c) + m_TransportLock);
				UpdateManager.RemoveLock(c);
				m_StateProgress = 1;
				mHandler.post(mUpdateStatusDBA);
			}

		};
		runningThread.start();
	}

	final Handler	mHandler			= new Handler();

	final Runnable	mUpdateStatusDBA	= new Runnable() {
											@Override
											public void run()
											{
												if (m_StateProgress == 0)
													m_Dialog.show();
												else
													m_Dialog.dismiss();
											}
										};



	@Override
	public void onEulaAgreedTo()
	{
		createView();
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	if(MANUEL == requestCode)
    		createView();
    }
}
