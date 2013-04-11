package net.rhatec.amtmobile.dialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.baseactivity.ActivityWithMenu;
import net.rhatec.amtmobile.constants.TypeString;
import net.rhatec.amtmobile.helpers.MenuCreator;
import net.rhatec.amtmobile.helpers.StringHelpers;
import net.rhatec.amtmobile.manager.FavorisManager;
import net.rhatec.amtmobile.providers.TransportProvider;
import net.rhatec.amtmobile.types.Autobus;
import net.rhatec.amtmobile.types.DateHelpers;
import net.rhatec.amtmobile.types.Favoris;
import net.rhatec.amtmobile.types.Horaire;
import net.rhatec.amtmobile.types.HoraireArret;
import net.rhatec.amtmobile.types.Message;
import net.rhatec.amtmobile.types.Pair;
import net.rhatec.amtmobile.types.Temps;
import net.rhatec.amtmobile.types.TransportServiceInfo;
import net.rhatec.amtmobile.view.HoraireAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class HoraireDlg extends ActivityWithMenu implements OnItemSelectedListener, OnItemClickListener
{
	HashMap<String, String>	m_ExtraInfoHoraire;
	Vector<Autobus>			m_CurrentDirectionVector;
	HashMap<String, String>	m_JourFerie;
	HashMap<String, String>	mapInfoDirection;
	HashMap<String, String>	mapInfoCircuit;
	Vector<Vector<Autobus>>	m_BusVector;
	int						m_OptionAjoutFavoris	= 0;
	int 					m_OptionAfficherCarte;
	String					m_NomTransportService;
	String					m_NoArret;
	String					m_NomIntersection;
	String					m_strNoAutobus;
	String					m_strDirectionBus;
	String					m_infoDirectionCode;
	String 					m_infoCircuitCode;
	long					m_nPositionInFile;
	boolean					init					= true;

	ListView				m_HoraireListView;

	Spinner					sListeHoraire;
	Spinner					sListeNom;
	Spinner					sListeDirection;
	ArrayList<Temps>		vTemps;
	Calendar unCalendrier = Calendar.getInstance();
	//boolean m_isAffichageRTL;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);

		
		// Recuperation des informations passees par l'activit� pr�c�dente.
		Bundle b = this.getIntent().getExtras();
		m_strNoAutobus = b.getString(TypeString.NOCIRCUIT);
		m_NomTransportService = b.getString(TypeString.SOCIETECODE);
		m_strDirectionBus = b.getString(TypeString.DIRECTION);
		m_NoArret = b.getString(TypeString.NOARRET);
		m_nPositionInFile = Long.parseLong(b.getString(TypeString.POSITIONOCTET));
		m_infoDirectionCode = b.getString(TypeString.INFODIRECTIONCODE);
		m_infoCircuitCode = b.getString(TypeString.INFOCIRCUITCODE);
		TransportServiceInfo service = TransportProvider.ObtenirTransportService(this, m_NomTransportService);

		//Set layout depending on screen orientation TODO: This should be an helper method
		WindowManager wm = getWindowManager();
		Display d = wm.getDefaultDisplay();
		if (d.getWidth() > d.getHeight())
			setContentView(R.layout.horaire_new_amtmetro_vertical); // ---landscape mode---
		else
			setContentView(R.layout.horaire_new_amtmetro);	
			
		this.setTitle(m_NomTransportService.toUpperCase() + "-"  +m_NoArret);

		HoraireArret hArret;
		if (m_nPositionInFile != 0)
			hArret = TransportProvider.ObtenirListeHoraireAutobus(this, m_NomTransportService, m_NoArret, m_nPositionInFile);
		else
			hArret = TransportProvider.ObtenirListeHoraireAutobus(this, m_NomTransportService, m_NoArret);

		int eError = hArret.ObtenirErreur();

		boolean IsAutobusPresent = false;
		if (eError == 0)
		{
			// Initialisation des maps
			mapInfoDirection = TransportProvider.ObtenirPhraseInfoDirection(this, m_NomTransportService);
			mapInfoCircuit = TransportProvider.ObtenirPhraseInfoCircuit(this, m_NomTransportService);
			m_ExtraInfoHoraire = TransportProvider.ObtenirPhraseInfoHoraire(this, m_NomTransportService);
			m_JourFerie = TransportProvider.ObtenirMapJourFerie(this, m_NomTransportService);
			HashMap<String, String> busNameMap = TransportProvider.ObtenirMapNomCircuit(this, m_NomTransportService);

			// Initialisation des variables
			m_nPositionInFile = hArret.ObtenirBytesPosition();
			TextView tNomIntersection = (TextView) findViewById(R.id.horaire_intersection);
			m_NomIntersection = hArret.ObtenirIntersection();
			tNomIntersection.setText(m_NomIntersection);
			Vector<Autobus> autobusVectorNotSorted = hArret.ObtenirAutobus(); // Vecteur d'autobus

			// Initialisation des spinners
			sListeNom = (Spinner) findViewById(R.id.SpinnerListeCircuit);
			sListeNom.setOnItemSelectedListener(this);
			sListeNom.setPrompt(getResources().getString(R.string.HoraireSpinnerDlg_Veuillez_choisir_un_circuit));
			
			sListeDirection = (Spinner) findViewById(R.id.SpinnerDirection);			
			sListeDirection.setOnItemSelectedListener(this);
			sListeDirection.setPrompt(getResources().getString(R.string.HoraireSpinnerDlg_Veuillez_choisir_un_circuit));

			sListeHoraire = (Spinner) findViewById(R.id.SpinnerHoraire);
			sListeHoraire.setOnItemSelectedListener(this);
			sListeHoraire.setPrompt(getResources().getString(R.string.HoraireSpinnerDlg_Veuillez_choisir_un_horaire));

			m_HoraireListView = (ListView) findViewById(R.id.ListeHoraire);
			m_HoraireListView.setOnItemClickListener(this);

			int numAutobusSelectionne = 0;
			/*
			 * L'utilisateur � entrer directement le num�ro de L'ARRET, on doit
			 * renseigner les attributs qui sont � "NONE" dans les arguments
			 * re�us pour l'activit� (Bundle) : le nom de l'autobus et la
			 * direction.
			 */
			
			if (m_strNoAutobus.equals(TypeString.INCONNU) || m_strDirectionBus.equals(TypeString.INCONNU) || m_infoDirectionCode.equals(TypeString.INCONNU) || m_infoCircuitCode.equals(TypeString.INCONNU))
			{
				m_strNoAutobus = autobusVectorNotSorted.get(0).ObtenirNumero();
				m_strDirectionBus = autobusVectorNotSorted.get(0).ObtenirDirectionCode();
				m_infoDirectionCode = autobusVectorNotSorted.get(0).ObtenirInfoDirectionCode();
				m_infoCircuitCode = autobusVectorNotSorted.get(0).ObtenirInfoCircuitCode();
			}

			// On cr�e un Hashmap contenant des vecteur d'autobus de direction
			// diff�rente, mais ayant le m�me num�ro
			HashMap<String, Vector<Autobus>> autobusMap = new HashMap<String, Vector<Autobus>>();
			for(Autobus a:autobusVectorNotSorted)
			{
				Vector<Autobus> vecteurCourant;
				String idAutobusCourant = a.ObtenirNumero()+a.ObtenirInfoCircuitCode();
				if ((vecteurCourant = autobusMap.get(idAutobusCourant)) == null)
				{
					vecteurCourant = new Vector<Autobus>();
					autobusMap.put(idAutobusCourant, vecteurCourant);
					vecteurCourant = autobusMap.get(idAutobusCourant);
				}
				if (a.ObtenirNumero().equals(m_strNoAutobus) && a.ObtenirDirectionCode().equals(m_strDirectionBus) && a.ObtenirInfoDirectionCode().equals(m_infoDirectionCode) && a.ObtenirInfoCircuitCode().equals(m_infoCircuitCode))
					IsAutobusPresent = true;

				vecteurCourant.add(a);
				autobusMap.put(idAutobusCourant, vecteurCourant);
			}

			// On remplis le spinner de la liste des autobus
			String[] ArrayNomLigneTrain = new String[autobusMap.size()];
			Collection<Vector<Autobus>> c = autobusMap.values();
			Iterator<Vector<Autobus>> itr = c.iterator();
			int compteur = 0;
			m_BusVector = new Vector<Vector<Autobus>>(autobusMap.size());

			while (itr.hasNext())
			{
				Vector<Autobus> vecAutobus = itr.next();
				Autobus a = vecAutobus.get(0);
				if (a.ObtenirNumero().equals(m_strNoAutobus) && a.ObtenirInfoCircuitCode().equals(m_infoCircuitCode)) // Le circuit s�lectionn� par l'utilisateur
					numAutobusSelectionne = compteur;

				m_BusVector.add(compteur, vecAutobus);
				
				
				if(service.isBus()) //On affiche le num�ro pour les bus et le nom pour les train/m�tro
				{
					String infoCircuit = mapInfoCircuit.get(a.ObtenirInfoCircuitCode());
					ArrayNomLigneTrain[compteur] = a.ObtenirNumero() + " " + StringHelpers.charDirectionToViewableString(a.ObtenirDirectionCode(), this.getBaseContext()) + " " + (infoCircuit == null? "" : infoCircuit);
				}
				else
					ArrayNomLigneTrain[compteur] = busNameMap.get(a.ObtenirNumero());
				
				++compteur;
			}

			// On assigne les nom de circuit au spinner de circuit
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ArrayNomLigneTrain);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sListeNom.setAdapter(adapter);
			sListeNom.setSelection(numAutobusSelectionne);
		} else
		{
			setResult(RESULT_FIRST_USER + eError);
			finish();
		}

		if (!IsAutobusPresent) // Si il s'agit d'un d�barcad�re, on retourne
		{
			setResult(RESULT_FIRST_USER + 5); // D�bard�re seulement, utiliser
												// des constantes
			finish();
		}

	}

	/**
	 * @function: onItemSelected
	 * @description: Fonction appel�e lorsque le spinner des num�ros d'autobus
	 *               ou bien le spinner des liste d'horaire change. La fonction
	 *               est aussi appel�e directement apr�s la construction de
	 *               l'objet pour aller r�cup�rer les heures de passages.
	 * @author: Hocine
	 * @params[in]:
	 * @params[out]:
	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView,
	 *      android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> spinnerParent, View view, int position, long id)
	{
		if (sListeNom == spinnerParent) // Le spinner de la liste d'autobus est modifi�.
		{
			int directionSelectionne = 0;
			// Il faut mettre � jour le spinner de direction
			m_CurrentDirectionVector = m_BusVector.get(position);
			int numDirection = m_CurrentDirectionVector.size();
			String[] directionArray = new String[numDirection];
			
			for (int i = 0; i < numDirection; ++i) 
			{
				Autobus aAutobusCourant = m_CurrentDirectionVector.get(i);
				directionArray[i] = mapInfoDirection.get(aAutobusCourant.ObtenirInfoDirectionCode());
	
				
				if (directionArray[i] == null)
					directionArray[i] = this.getString(R.string.HoraireDlg_Trajet_Regulier);
				
				if (init)
				{
					if (m_infoDirectionCode.equals(aAutobusCourant.ObtenirInfoDirectionCode()))
						directionSelectionne = i;
				}
			}
			//sListeDirection.setVisibility(Spinner.INVISIBLE);
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, directionArray);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			
			
			sListeDirection.setAdapter(adapter);
			if (init)
			{
				sListeDirection.setSelection(directionSelectionne);
				init = false;
			}


			// Il faut mettre � jour le spinner de direction
		} 
		else if (sListeDirection == spinnerParent)
		{
			// Il faut mettre � jour le spinner d'horaire
			Autobus aAutobus = m_CurrentDirectionVector.get(position);
			// On r�cup�re le vecteur d'horaire courant de l'autobus courant
			Vector<Horaire> vHoraire = aAutobus.ObtenirVecteurHoraire();
			/*
			HashMap<String, Integer> mapHoraire = new HashMap<String, Integer>();
			int j = 0;
			for (Horaire h : vHoraire)
			{
				mapHoraire.put(h.ObtenirNom(), j++);
			}*/
			
			Pair<Integer, Boolean> horaireAConsulter = DateHelpers.obtenirTypeHoraireActuelAConsulter(vHoraire, unCalendrier);

			int positionHoraireAConsulter = horaireAConsulter.first;
			if(positionHoraireAConsulter == -1) //On prends le premier horaire par d�faut
			{
				positionHoraireAConsulter = 0;
			}

			
			
			String[] ArrayHoraireName = new String[vHoraire.size()];
			int numHoraire = vHoraire.size();
			// On rempli le vecteur de nom d'horaire (Samedi, dimanche etc) pour
			// l'autobus qui est affich� (autobusCourant)
			for (int i = 0; i < numHoraire; ++i)
			{
				Horaire horaireCourant = vHoraire.get(i);
				String nomHoraire = horaireCourant.ObtenirNom();
				int horaireId = Integer.parseInt(nomHoraire);
				ArrayHoraireName[i] = DateHelpers.obtenirNomHoraire(horaireId,m_JourFerie, this);
				
			}
			// On assigne le tableau d'horaire (Samedi, dimanche etc) pour
			// l'autobus qui est affich� (autobusCourant)
			ArrayAdapter<String> horaireSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ArrayHoraireName);
			horaireSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sListeHoraire.setAdapter(horaireSpinnerAdapter); // Cette ligne appel automatiquement la fonction item selection avec sListeHoraire comme parent
			sListeHoraire.setSelection(positionHoraireAConsulter);
			// On va mettre � jour l'extra info et la direction
			m_strNoAutobus = aAutobus.ObtenirNumero();
			m_infoDirectionCode = aAutobus.ObtenirInfoDirectionCode();
			m_infoCircuitCode = aAutobus.ObtenirInfoCircuitCode();
			m_strDirectionBus = aAutobus.ObtenirDirectionCode();
		} 
		else if (sListeHoraire == spinnerParent)
		{
			// Il faut mettre � jour l'horaire
			Autobus aAutobus = m_CurrentDirectionVector.get(sListeDirection.getSelectedItemPosition());
			// On r�cup�re le vecteur d'horaire courant de l'autobus courant
			Vector<Horaire> vHoraire = aAutobus.ObtenirVecteurHoraire();
			int positionHoraireAConsulter = position;// sListeHoraire.getSelectedItemPosition();

			// En cas d'erreur on affiche le premier horaire dans la liste des
			// horaire du vecteur.
			if (positionHoraireAConsulter > (vHoraire.size() - 1))
				positionHoraireAConsulter = 0;

			// r�cup�rer les heures de passages pour l'autobus et le type
			// d'horaire
			Horaire hHoraire = vHoraire.get(positionHoraireAConsulter);
			vTemps = hHoraire.ObtenirVecteurHeure();

			// On affiche les heures de passages pour l'autobus et le type
			// d'horaire selectionnees.
			HoraireAdapter liste = new HoraireAdapter(this, vTemps, m_ExtraInfoHoraire, hHoraire.ObtenirExtraInfo());
			m_HoraireListView.setAdapter(liste);

			// Position automatique de la liste au prochaine passage
			int positionProchainPassage = hHoraire.ObtenirPositionProchainPassage();
			m_HoraireListView.setSelection(positionProchainPassage);
			setResult(RESULT_OK);
		}
	}

	/*
	 * @function: onItemClick
	 * 
	 * @description: M�thode appelle lorsqu'on clique sur une heure de passage
	 * d'un autobus. On passe l'ensemble des informations n�cessaires pour cr�er
	 * la notifications. L'usager pourra ajouter une notification par la suite.
	 * 
	 * @author: Hocine
	 * 
	 * @params[in]:
	 * 
	 * @params[out]:
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id)
	{
		// On r�cup�re l'objet temps � la position ou l'utilisateur � cliquer
		// pour le passer � la notification
		Temps heureSelectionnee = vTemps.get(position);
		// On ne peut pas notifier sur les passages au six minutes.
		if (heureSelectionnee.getM_Time().hour == 30 && heureSelectionnee.getM_Time().minute == 6)
		{

			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(R.string.notification_titre);
			alertDialog.setMessage(this.getString(R.string.HoraireSpinnerDlg_impossible_ajouter_notification_passage));
			alertDialog.setButton(this.getString(R.string.general_ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					return;
				}
			});
			alertDialog.setCanceledOnTouchOutside(true);
			alertDialog.show();
		} else
		{

			Intent iNotifier = new Intent(this, NotifierDlg.class);
			iNotifier.putExtra(TypeString.NOCIRCUIT, m_strNoAutobus);
			iNotifier.putExtra(TypeString.SOCIETECODE, m_NomTransportService);
			iNotifier.putExtra(TypeString.DIRECTION, m_strDirectionBus);
			iNotifier.putExtra(TypeString.NOARRET, m_NoArret);
			iNotifier.putExtra(TypeString.INTERSECTIONSCONCAT, m_NomIntersection);
			iNotifier.putExtra(TypeString.INFODIRECTIONCODE, m_infoDirectionCode);
			iNotifier.putExtra(TypeString.INFOCIRCUITCODE, m_infoCircuitCode);
			iNotifier.putExtra(TypeString.POSITIONOCTET, String.valueOf(m_nPositionInFile));
			iNotifier.putExtra(TypeString.MINUTE, heureSelectionnee.getM_Time().minute);
			iNotifier.putExtra(TypeString.HEURE, heureSelectionnee.getM_Time().hour);
			this.startActivity(iNotifier);
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{

	}

	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	// Créations des boutons du menu
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	/** hook into menu button for activity */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		m_OptionAjoutFavoris = MenuCreator.populateMenu(menu, this.getBaseContext()) + 1;
		MenuItem item = menu.add(Menu.NONE, m_OptionAjoutFavoris, Menu.NONE, this.getString(R.string.menu_option_ajouter_favoris));
		item.setAlphabeticShortcut('a');
		item.setIcon(R.drawable.ic_action_ajout_favoris);
		
		m_OptionAfficherCarte = m_OptionAjoutFavoris + 1;
		MenuItem optionAfficherCarte = menu.add(Menu.NONE, m_OptionAfficherCarte, Menu.NONE, this.getString(R.string.menu_option_afficher_arret_carte));
		optionAfficherCarte.setAlphabeticShortcut('m');
		optionAfficherCarte.setIcon(R.drawable.ic_menu_location);
		
		
		return true;//super.onCreateOptionsMenu(menu);
	}

	/** when menu button option selected */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int nItemId = item.getItemId();
		if (nItemId == m_OptionAjoutFavoris)
		{

			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(R.string.menu_option_ajouter_favoris);
			
			Favoris favoris = new Favoris(m_NomTransportService, m_strNoAutobus, m_strDirectionBus, m_NoArret, m_NomIntersection, m_nPositionInFile, m_infoDirectionCode, m_CurrentDirectionVector.get(sListeDirection.getSelectedItemPosition()).ObtenirVecteurHoraire(), m_infoCircuitCode);
			
			Message message = FavorisManager.ajouterFavoris(this, favoris);

			alertDialog.setMessage(this.getString(message.getMessage()));
			alertDialog.setButton(this.getString(R.string.general_ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					return;
				}
			});
			alertDialog.setCanceledOnTouchOutside(true);
			alertDialog.show();
		} 
		else if(nItemId == m_OptionAfficherCarte)
		{
			Intent mapActivity = new Intent(this, MapsDlg.class);
			startActivity(mapActivity);
		}
		else
		{
			MenuCreator.startActivityFromMenu(item, this);
		}

		return super.onOptionsItemSelected(item);
	}

}
