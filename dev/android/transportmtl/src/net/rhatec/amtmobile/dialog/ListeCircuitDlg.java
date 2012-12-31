package net.rhatec.amtmobile.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.baseactivity.ListActivityWithMenu;
import net.rhatec.amtmobile.constants.TypeString;
import net.rhatec.amtmobile.helpers.StringHelpers;
import net.rhatec.amtmobile.providers.TransportProvider;
import net.rhatec.amtmobile.types.Autobus;
import net.rhatec.amtmobile.view.CircuitAdapter;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class ListeCircuitDlg extends ListActivityWithMenu implements OnItemClickListener
{
	ArrayList<HashMap<String, String>>	m_Liste					= new ArrayList<HashMap<String, String>>();
	String								m_NomTransport;
	int									m_CurrentlySelectedItem	= 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle b = this.getIntent().getExtras();
		ListView lView = getListView();
		lView.setLongClickable(false);
		lView.setClickable(true);
		//lView.setLayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);

		m_NomTransport = b.getString(TypeString.SOCIETECODE);

		Vector<Autobus> vListeCircuit = TransportProvider.ObtenirListeCircuit(m_NomTransport);
		if (vListeCircuit.size() != 0)
		{
			this.setTitle(getResources().getString(R.string.ListeCircuitDlg_Liste_de_circuit_pour) + " " + m_NomTransport.toUpperCase());
			Iterator<Autobus> iter = vListeCircuit.iterator();
			HashMap<String, String> mapInfoDirectionPhrase = TransportProvider.ObtenirPhraseInfoDirection(m_NomTransport);
			HashMap<String, String> mapInfoCircuitPhrase = TransportProvider.ObtenirPhraseInfoCircuit(m_NomTransport);
			String LastBus = "";
			HashMap<String, String> item = null;
			int colors[] = new int[vListeCircuit.size()];
			int i = 0;
			while (iter.hasNext())
			{
				Autobus strBus = iter.next();				
				if (LastBus.contentEquals(strBus.ObtenirNumero())) //TODO: Use bundle array instead of String to split !!!
				{
					colors[i] = strBus.ObtenirCouleur();
					item.put(TypeString.NOMAUTOBUSARRAY, item.get(TypeString.NOMAUTOBUSARRAY) + ";" + strBus.ObtenirNom());
					item.put(TypeString.DIRECTIONARRAY, item.get(TypeString.DIRECTIONARRAY) + ";" + StringHelpers.charDirectionToViewableString(strBus.ObtenirDirectionCode(), this.getBaseContext()));
					String infoDirectionCode = strBus.ObtenirInfoDirectionCode();
					String infoDirectionPhrase = null;
					if (infoDirectionCode != null)
						infoDirectionPhrase = mapInfoDirectionPhrase.get(infoDirectionCode);
					
					String infoCircuitCode = strBus.ObtenirInfoCircuitCode();
					String infoCircuitPhrase = null;
					if (infoCircuitCode != null)
						infoCircuitPhrase = mapInfoCircuitPhrase.get(infoCircuitCode);
					
					item.put(TypeString.INFODIRECTIONCODEARRAY, item.get(TypeString.INFODIRECTIONCODEARRAY) + ";" + infoDirectionCode);
					item.put(TypeString.INFODIRECTIONPHRASEARRAY, item.get(TypeString.INFODIRECTIONPHRASEARRAY) + ";" + infoDirectionPhrase);					
					item.put(TypeString.INFOCIRCUITCODEARRAY, item.get(TypeString.INFOCIRCUITCODEARRAY) + ";" + infoCircuitCode);
					item.put(TypeString.INFOCIRCUITPHRASEARRAY, item.get(TypeString.INFOCIRCUITPHRASEARRAY) + ";" + infoCircuitPhrase);
				} 
				else
				{
					colors[i++] = strBus.ObtenirCouleur();
					item = new HashMap<String, String>();
					item.put(TypeString.NOCIRCUIT, strBus.ObtenirNumero());
					item.put(TypeString.NOMAUTOBUS, strBus.ObtenirNom()); //Affichage dans la liste de circuit
					item.put(TypeString.NOMAUTOBUSARRAY, strBus.ObtenirNom()); //Affichage dans la liste de direction
					item.put(TypeString.DIRECTIONARRAY, StringHelpers.charDirectionToViewableString(strBus.ObtenirDirectionCode(), this.getBaseContext())); 
					String infoDirectionCode = strBus.ObtenirInfoDirectionCode();
					String infoDirectionPhrase = "null";
					if (infoDirectionCode != null)
						infoDirectionPhrase = mapInfoDirectionPhrase.get(infoDirectionCode);
					
					String infoCircuitCode = strBus.ObtenirInfoCircuitCode();
					String infoCircuitPhrase = null;
					if (infoCircuitCode != null)
						infoCircuitPhrase = mapInfoCircuitPhrase.get(infoCircuitCode);
					
					item.put(TypeString.INFODIRECTIONCODEARRAY, infoDirectionCode);
					item.put(TypeString.INFODIRECTIONPHRASEARRAY, infoDirectionPhrase);					
					item.put(TypeString.INFOCIRCUITCODEARRAY,  infoCircuitCode);
					item.put(TypeString.INFOCIRCUITPHRASEARRAY, infoCircuitPhrase);
					m_Liste.add(item);
					LastBus = strBus.ObtenirNumero();
				}
				lView.setOnItemClickListener(this);

			}
			//TODO: Try to have a big finger mode with setTheme
/*
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			boolean bigFinger = preferences.getBoolean("bigFingerMode", false);*/
			CircuitAdapter liste = new CircuitAdapter(this, m_Liste, R.layout.itembuslist, new String[]
			{ TypeString.NOCIRCUIT, TypeString.NOMAUTOBUS }, new int[]
			{ R.id.TextView01, R.id.TextView02 }, colors/*, bigFinger*/);

			setListAdapter(liste);
		} else
		{
			Intent iError = new Intent(this, DatabaseNotFoundDlg.class);
			this.startActivity(iError);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) //TODO: Use bundle StringArray instead of String to split !!!
	{
		Bundle b = new Bundle();
		Intent iCircuitDirection = new Intent(ListeCircuitDlg.this, AccesCircuitDlg.class);
		HashMap<String, String> item = m_Liste.get(position);
		b.putString(TypeString.SOCIETECODE, m_NomTransport);
		b.putString(TypeString.NOCIRCUIT, item.get( TypeString.NOCIRCUIT));
		b.putString(TypeString.NOMAUTOBUSARRAY, item.get(TypeString.NOMAUTOBUSARRAY));
		b.putString(TypeString.DIRECTIONARRAY, item.get(TypeString.DIRECTIONARRAY));
		b.putString(TypeString.INFODIRECTIONPHRASEARRAY, item.get(TypeString.INFODIRECTIONPHRASEARRAY));
		b.putString(TypeString.INFODIRECTIONCODEARRAY, item.get(TypeString.INFODIRECTIONCODEARRAY));
		b.putString(TypeString.INFOCIRCUITPHRASEARRAY, item.get(TypeString.INFOCIRCUITPHRASEARRAY));
		b.putString(TypeString.INFOCIRCUITCODEARRAY, item.get(TypeString.INFOCIRCUITCODEARRAY));

		iCircuitDirection.putExtras(b);
		this.startActivity(iCircuitDirection);

	}

}