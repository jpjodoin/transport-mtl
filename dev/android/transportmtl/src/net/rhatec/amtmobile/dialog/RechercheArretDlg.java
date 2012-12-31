package net.rhatec.amtmobile.dialog;

import java.util.HashMap;
import java.util.Vector;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.baseactivity.ActivityWithMenu;
import net.rhatec.amtmobile.constants.TypeString;
import net.rhatec.amtmobile.providers.TransportProvider;
import net.rhatec.amtmobile.types.Autobus;
import net.rhatec.amtmobile.types.Pair;
import net.rhatec.amtmobile.types.TransportServiceInfo;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * Bo�te de dialogue de recherche d'arr�t, de mise � jour et poss�dant un acc�s � la liste d'autobus 
 */
public class RechercheArretDlg extends ActivityWithMenu implements OnClickListener
{
	String			m_DialogMessage;
	MiseAJourDlg	m_MiseAJourDlg;
	EditText		m_TextNoCircuit;
	String			m_TransportName;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle b = this.getIntent().getExtras();
		m_TransportName = b.getString(TypeString.SOCIETECODE);		
		TransportServiceInfo currentService = TransportProvider.ObtenirTransportService(m_TransportName);
		if (currentService != null) 
		{
			String finValidite = b.getString(TypeString.FINVALIDITESOCIETE);
			currentService.setFinValidite(finValidite);
			setContentView(R.layout.arret);

			this.setTitle(getResources().getString(R.string.RechercheArretDlg_rechercher_arret_pour) + " " + m_TransportName.toUpperCase());

			if(currentService.displaySearchBox()) 
			{
				Button bRecherche = (Button) findViewById(R.id.buttonGo);
				bRecherche.setOnClickListener(this);
				m_TextNoCircuit = (EditText) findViewById(R.id.txtboxArret);
				m_TextNoCircuit.setOnClickListener(this);
				m_TextNoCircuit.setMaxWidth(m_TextNoCircuit.getMeasuredWidth());
				m_TextNoCircuit.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event)
					{
						if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
						{
							StartSearch();
							return true;
						}
						return false;
					}
				});
			} 
			else
			{
				LinearLayout layout = (LinearLayout) findViewById(R.id.SearchLayout);
				layout.setVisibility(View.GONE);
			}

			Button bCarteReseau = (Button) findViewById(R.id.bCarte);
			if (currentService.isAffichageCarte())
			{
				bCarteReseau.setOnClickListener(this);
			} 
			else
			{
				bCarteReseau.setVisibility(View.GONE);
			}

			Button bListe = (Button) findViewById(R.id.bListeAutobus);
			bListe.setOnClickListener(this);
			
			//Validité
			Pair<String, String> pair = currentService.getValiditeDate(this);
			TextView lblValidite = (TextView) findViewById(R.id.lblValidite);
			lblValidite.setText(getResources().getString(R.string.RechercheArretDlg_Base_de_donnees_valide_du) + " " + pair.first + " " + getResources().getString(R.string.RechercheArretDlg_au)
					+ " " + pair.second);

		} 
		else
		{
			this.setTitle(getResources().getString(R.string.RechercheArretDlg_aucune_mise_a_jour) + " " + m_TransportName.toUpperCase());
			setContentView(R.layout.nodatabase);
			Button bUpdate = (Button) findViewById(R.id.bUpdate);
			bUpdate.setOnClickListener(this);
		}

	}

	/*
	 * Quand l'activit� de recherche d'arr�t retourne, elle lance un code
	 * d'erreur si il n'y a pas d'erreur ce qui nous permet d'afficher une bo�te
	 * de dialogue (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode >= RESULT_FIRST_USER)
		{
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(R.string.arret_inconnu_titlebox);
			alertDialog.setMessage(this.getString(R.string.arret_inconnu_msg));
			alertDialog.setButton(this.getString(R.string.general_ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					return;
				}
			});
			alertDialog.show();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.bUpdate:
			Intent iMiseAJour = new Intent(this, MiseAJourDlg.class);
			this.startActivity(iMiseAJour);
			break;
		case R.id.bListeAutobus:
			Intent iListeCircuit = new Intent(RechercheArretDlg.this, ListeCircuitDlg.class);
			Bundle b = new Bundle();
			b.putString(TypeString.SOCIETECODE, m_TransportName);
			iListeCircuit.putExtras(b);
			this.startActivity(iListeCircuit);
			break;

		case R.id.bCarte:
			Intent iUpgrade = new Intent(this, CarteViewerDlg.class);
			Bundle b2 = new Bundle();
			b2.putString(TypeString.SOCIETECODE, m_TransportName);
			iUpgrade.putExtras(b2);
			this.startActivity(iUpgrade);
			break;

		// On effectue la recherche de l'arr�t entr� par l'utilisateur
		case R.id.buttonGo:
			StartSearch();
		default:

		}
	}

	void StartSearch()
	{
		String busNumberStr = m_TextNoCircuit.getText().toString();
		// ligne 1 -99 -141 -999 etc
		if (busNumberStr.length() <= 3 && busNumberStr.contentEquals("") == false)
		{
			// TODO finir l'implementation de la recherche de circuit
			Vector<Autobus> vAutobus = TransportProvider.ObtenirListeCircuitParNumero(m_TransportName, busNumberStr);

			if (vAutobus.size() != 0)
			{
				Intent iHoraire = new Intent(this, AccesCircuitDlg.class);
				Bundle bHoraire = new Bundle();
				bHoraire.putString(TypeString.SOCIETECODE, m_TransportName);
				bHoraire.putString(TypeString.NOCIRCUIT, busNumberStr);
				
				
				
				StringBuffer directionArrayBuffer = new StringBuffer();
				StringBuffer infoDirectionCodeArrayBuffer = new StringBuffer();
				StringBuffer infoDirectionPhraseArrayBuffer = new StringBuffer();
				StringBuffer infoCircuitCodeArrayBuffer = new StringBuffer();
				StringBuffer infoCircuitPhraseArrayBuffer = new StringBuffer();
				StringBuffer nomAutobusArrayBuffer = new StringBuffer();
				HashMap<String, String> mapInfoDirection = TransportProvider.ObtenirPhraseInfoDirection(m_TransportName);
				HashMap<String, String> mapInfoCircuit = TransportProvider.ObtenirPhraseInfoCircuit(m_TransportName);
				for (Autobus autobus : vAutobus)
				{
					nomAutobusArrayBuffer.append(autobus.ObtenirNom() + ";");
					directionArrayBuffer.append(autobus.ObtenirDirectionCode() + ";");
					String infoDirectionCode = autobus.ObtenirInfoDirectionCode();
					
					String infoDirectionPhrase = null;
					if (infoDirectionCode != null) //TODO Validate this...						
						infoDirectionPhrase = mapInfoDirection.get(infoDirectionCode);
					
					infoDirectionCodeArrayBuffer.append(infoDirectionCode + ";");
					infoDirectionPhraseArrayBuffer.append(infoDirectionPhrase + ";");
					
					String infoCircuitCode = autobus.ObtenirInfoCircuitCode();
					infoCircuitCodeArrayBuffer.append(infoCircuitCode + ";");
					String infoCircuitPhrase = null;
					if (infoCircuitCode != null) //TODO Validate this...						
						infoCircuitPhrase = mapInfoCircuit.get(infoCircuitCode);
										
					infoCircuitCodeArrayBuffer.append(infoCircuitCode + ";");
					infoCircuitPhraseArrayBuffer.append(infoCircuitPhrase + ";");	
				}
				bHoraire.putString(TypeString.DIRECTIONARRAY, directionArrayBuffer.toString()); //TODO Change these to StringArray
				bHoraire.putString(TypeString.NOMAUTOBUSARRAY, nomAutobusArrayBuffer.toString());
				bHoraire.putString(TypeString.INFODIRECTIONCODEARRAY, infoDirectionCodeArrayBuffer.toString());
				bHoraire.putString(TypeString.INFODIRECTIONPHRASEARRAY, infoDirectionPhraseArrayBuffer.toString());
				bHoraire.putString(TypeString.INFOCIRCUITCODEARRAY, infoCircuitCodeArrayBuffer.toString());
				bHoraire.putString(TypeString.INFOCIRCUITPHRASEARRAY, infoCircuitPhraseArrayBuffer.toString());
				iHoraire.putExtras(bHoraire);
				this.startActivityForResult(iHoraire, 1);

			} else
			{
				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle(getResources().getString(R.string.RechercheArretDlg_Autobus_inconnu));
				alertDialog.setMessage(getResources().getString(R.string.RechercheArretDlg_Autobus_inconnu));
				alertDialog.setButton(this.getString(R.string.general_ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						return;
					}
				});
				alertDialog.show();
			}

		} 
		else if (busNumberStr.contentEquals("") == false) //Recherche d'arr�t
		{
			Intent iHoraire = new Intent(this, HoraireDlg.class);

			Bundle bHoraire = new Bundle();
			bHoraire.putString(TypeString.SOCIETECODE, m_TransportName);
			bHoraire.putString(TypeString.NOCIRCUIT, TypeString.INCONNU);
			bHoraire.putString(TypeString.NOARRET, busNumberStr);
			bHoraire.putString(TypeString.DIRECTION, TypeString.INCONNU);
			bHoraire.putString(TypeString.INFODIRECTIONCODE, TypeString.INCONNU);
			bHoraire.putString(TypeString.INFOCIRCUITCODE, TypeString.INCONNU);
			bHoraire.putString(TypeString.POSITIONOCTET, "0");
			// from file
			iHoraire.putExtras(bHoraire);
			this.startActivityForResult(iHoraire, 1);

		}
	}

}
