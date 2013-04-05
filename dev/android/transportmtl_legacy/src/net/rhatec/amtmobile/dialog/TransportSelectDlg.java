package net.rhatec.amtmobile.dialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.baseactivity.ListActivityWithMenu;
import net.rhatec.amtmobile.constants.TypeString;
import net.rhatec.amtmobile.manager.ListeSocieteManager;
import net.rhatec.amtmobile.providers.TransportProvider;
import net.rhatec.amtmobile.types.Pair;
import net.rhatec.amtmobile.types.TransportServiceBase;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/*
 * Activit� qui sert de point d'entr� � l'application. Contient des boutons pour choisir le fournisseur de transport en commun
 */

public class TransportSelectDlg extends ListActivityWithMenu implements OnItemClickListener
{
	ArrayList<HashMap<String, String>>	m_Liste				= new ArrayList<HashMap<String, String>>();
	ArrayList<TransportServiceBase>		m_ListeNonValide	= new ArrayList<TransportServiceBase>();

	int									m_dateCourante;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		createView();
	
		
		
	}
	
	void createView()
	{
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			m_dateCourante = Integer.parseInt(dateFormat.format(Calendar.getInstance().getTime()));

			populateListWithAvailableTransport();

			if (m_Liste.isEmpty()) // Clarifier MAJ d'horaire
			{
				Intent iMiseAJour = new Intent(this, MiseAJourDlg.class);
				this.startActivity(iMiseAJour);
				finish(); // �vite que l'activit� vide soit dans le stack d'activit� :)
			} else
			{
				this.setTitle(getResources().getString(R.string.main_lbl_choix_transporteur));
				ListView lView = getListView();
				lView.setLongClickable(false);
				lView.setClickable(true);
				lView.setOnItemClickListener(this);

				SimpleAdapter liste = new SimpleAdapter(this, m_Liste, R.layout.itemservicetransport, new String[]
				{ TypeString.SOCIETEPHRASE }, new int[]
				{ R.id.NomService });
				setListAdapter(liste);
				if(!m_ListeNonValide.isEmpty())
				{
					Boolean warningValidity = preferences.getBoolean("warningValidity", true);

					if (warningValidity)
					{
						int numNonValide = m_ListeNonValide.size();
						String[] dbaNonValide = new String[numNonValide * 3];
						for (int i = 0; i < numNonValide; ++i)
						{
							TransportServiceBase transport = m_ListeNonValide.get(i);
							Pair<String, String> date = transport.getValiditeDate(this);
							dbaNonValide[i * 3] = transport.getShortName();
							dbaNonValide[i * 3 + 1] = date.first;
							dbaNonValide[i * 3 + 2] = date.second;
						}
						// V�rifier condition
						Intent iActivity = new Intent(this, AvertissementValiditeDlg.class);
						Bundle b = new Bundle();
						b.putStringArray(TypeString.DBAOBSOLETEARRAY, dbaNonValide);
						iActivity.putExtras(b);
						this.startActivity(iActivity);
					}
				}			
			
		}
		
	}
	
	private void populateListWithAvailableTransport()
	{
		Vector<TransportServiceBase> transportVector = ListeSocieteManager.obtenirListe(this);
		for(TransportServiceBase t:transportVector)
		{
			if (!t.isValid(m_dateCourante))
				m_ListeNonValide.add(t);
			HashMap<String, String> item = new HashMap<String, String>();
			item.put(TypeString.SOCIETECODE, t.getShortName());
			item.put(TypeString.SOCIETEPHRASE, t.getLongName());
			item.put(TypeString.FINVALIDITESOCIETE, t.getFinValidite());
			m_Liste.add(item);
		}		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{

		String transportService = m_Liste.get(position).get(TypeString.SOCIETECODE);
		Bundle b = new Bundle();
		if (TransportProvider.DatabaseExist(this, transportService))
		{
			Intent iArret = new Intent(this, RechercheArretDlg.class);
			b.putString(TypeString.SOCIETECODE, transportService);
			b.putString(TypeString.FINVALIDITESOCIETE, m_Liste.get(position).get(TypeString.FINVALIDITESOCIETE));
			iArret.putExtras(b);
			this.startActivity(iArret);
		} else
		{
			Intent iError = new Intent(this, DatabaseNotFoundDlg.class);
			this.startActivity(iError);
		}
	}	
}