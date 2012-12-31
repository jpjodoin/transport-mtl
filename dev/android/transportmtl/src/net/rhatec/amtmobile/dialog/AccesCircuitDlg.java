package net.rhatec.amtmobile.dialog;

import java.util.ArrayList;
import java.util.HashMap;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.constants.TypeString;
import net.rhatec.amtmobile.helpers.StringHelpers;
import net.rhatec.amtmobile.providers.TransportProvider;
import net.rhatec.amtmobile.types.TransportServiceInfo;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class AccesCircuitDlg extends ListActivity implements OnItemClickListener
{
	String								m_NomTransport;
	String								m_NoAutobus;
	ArrayList<HashMap<String, String>>	m_Liste	= new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setTitle(getResources().getString(R.string.AccesCircuitDlg_veuillez_une_choisir_direction));
		Bundle b = this.getIntent().getExtras();

		m_NomTransport = b.getString(TypeString.SOCIETECODE);
		m_NoAutobus = b.getString(TypeString.NOCIRCUIT);
		String direction = b.getString(TypeString.DIRECTIONARRAY);
		TransportServiceInfo service = TransportProvider.ObtenirTransportService(m_NomTransport);
		boolean AfficherExtraInfo = service.isAffichageExtraInfoDirection();
		ListView listView = getListView();
		listView.setOnItemClickListener(this);		
		
		String[] directionArray, infoCircuitCodeArray, infoCircuitPhraseArray, infoDirectionCodeArray, infoDirectionCodePhraseArray, nomAutobusArray;
		
		//TODO: Change these for String Array !!!
		if (direction.contains(";"))
		{
			directionArray = direction.split(";");
			infoCircuitCodeArray = b.getString(TypeString.INFOCIRCUITCODEARRAY).split(";");
			infoCircuitPhraseArray = b.getString(TypeString.INFOCIRCUITPHRASEARRAY).split(";");
			infoDirectionCodeArray = b.getString(TypeString.INFODIRECTIONCODEARRAY).split(";");
			infoDirectionCodePhraseArray = b.getString(TypeString.INFODIRECTIONPHRASEARRAY).split(";");
			nomAutobusArray = b.getString(TypeString.NOMAUTOBUSARRAY).split(";");
		}
		else //Only 1 item... TODO: Check if we can use split if there is only 1 item...
		{
			directionArray = new String[1];
			infoCircuitCodeArray = new String[1];
			infoCircuitPhraseArray = new String[1];
			infoDirectionCodeArray = new String[1];
			infoDirectionCodePhraseArray = new String[1];
			nomAutobusArray = new String[1];
			directionArray[0] = direction;			
			infoCircuitCodeArray[0] = b.getString(TypeString.INFOCIRCUITCODEARRAY);
			infoCircuitPhraseArray[0] = b.getString(TypeString.INFOCIRCUITPHRASEARRAY);
			infoDirectionCodeArray[0] = b.getString(TypeString.INFODIRECTIONCODEARRAY);
			infoDirectionCodePhraseArray[0] = b.getString(TypeString.INFODIRECTIONPHRASEARRAY);
			nomAutobusArray[0] = b.getString(TypeString.NOMAUTOBUSARRAY);
			
		}		
		
		int numberOfItem = directionArray.length;
		for (int i = 0; i < numberOfItem; ++i)
		{
			HashMap<String, String> item = new HashMap<String, String>();
			item.put(TypeString.NOCIRCUIT, m_NoAutobus);
			
			item.put(TypeString.DIRECTION, StringHelpers.charDirectionToViewableString(directionArray[i], this.getBaseContext()));
			String infoDirectionPhrase = infoDirectionCodePhraseArray[i];
			if (infoDirectionPhrase.equals("null"))
				infoDirectionPhrase = "";
			String infoCircuitPhrase = infoCircuitPhraseArray[i];
			if(infoCircuitPhrase == null || infoCircuitPhrase.equals("null"))
				infoCircuitPhrase = "";
			item.put(TypeString.NOMAUTOBUS, nomAutobusArray[i] + " " + infoCircuitPhrase);
			item.put(TypeString.INFOCIRCUITPHRASE, infoCircuitPhrase);
			item.put(TypeString.INFOCIRCUITCODE, infoCircuitCodeArray[i]);										
			item.put(TypeString.INFODIRECTIONPHRASE, infoDirectionPhrase);
			item.put(TypeString.INFODIRECTIONCODE, infoDirectionCodeArray[i]);
			m_Liste.add(item);
		}	

		SimpleAdapter liste;
		if (AfficherExtraInfo) //TODO : Afficher Circuit extra info ??
		{
			liste = new SimpleAdapter(this, m_Liste, R.layout.itembuslistextrainfo, new String[]
			{ TypeString.NOCIRCUIT, TypeString.NOMAUTOBUS, TypeString.DIRECTION, TypeString.INFODIRECTIONPHRASE }, new int[]
			{ R.id.TextView01, R.id.TextView02, R.id.TextView03, R.id.ExtraInfo });
		} else
		{
			liste = new SimpleAdapter(this, m_Liste, R.layout.itembuslistdirection, new String[]
			{ TypeString.NOCIRCUIT, TypeString.NOMAUTOBUS, TypeString.DIRECTION }, new int[]
			{ R.id.TextView01, R.id.TextView02, R.id.TextView03 });
		}
		listView.setAdapter(liste);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id)
	{
		Bundle b = new Bundle();
		Intent iDirectionArret = new Intent(AccesCircuitDlg.this, ListeArretDlg.class);
		HashMap<String, String> item = m_Liste.get(position);
		b.putString(TypeString.SOCIETECODE, m_NomTransport);
		b.putString(TypeString.NOCIRCUIT, m_NoAutobus);
		b.putString(TypeString.DIRECTION, StringHelpers.stringDirectionToChar(item.get(TypeString.DIRECTION), this.getBaseContext()));
		b.putString(TypeString.INFODIRECTIONCODE, item.get(TypeString.INFODIRECTIONCODE));
		b.putString(TypeString.INFOCIRCUITCODE, item.get(TypeString.INFOCIRCUITCODE));
		iDirectionArret.putExtras(b);
		this.startActivity(iDirectionArret);

	}

}
