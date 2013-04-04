package net.rhatec.amtmobile.dialog;

import java.util.Vector;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.baseactivity.ActivityWithMenu;
import net.rhatec.amtmobile.helpers.MenuCreator;
import net.rhatec.amtmobile.manager.UpdateManager;
import net.rhatec.amtmobile.manager.UpdateManager.UpdateState;
import net.rhatec.amtmobile.types.UpdateStruct;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MiseAJourDlg extends ActivityWithMenu implements OnClickListener
{

	// Constante
	public static final int	BUTTON_MISE_A_JOUR	= 10;
	public static final int	BUTTON_TERMINER		= BUTTON_MISE_A_JOUR + 1;
	 
	// Layouts
	LinearLayout			m_ChoixTypeMiseAJourLayout;
	LinearLayout			m_RechercheMiseAJourLayout;
	ScrollView				m_ChoixMiseAJourScrollLayout;
	LinearLayout			m_ChoixMiseAJourLayout;
	LinearLayout			m_TelechargementLayout;
	LinearLayout			m_MiseAJourTermineLayout;

	// Widgets
	Vector<CheckBox>		m_Checkbox;
	ProgressBar				m_UpdateProgressBar;
	TextView				m_ViewUpdateProgress;

	// Autres
	Context					m_Context;
	UpdateState				m_State;
	UpdateState				m_PreviousState		= UpdateState.DEBUT;
	Vector<UpdateStruct>	m_Update;
	UpdateManager			m_UpdateManager;
	String					m_ErrorString;

	public UpdateState GetState()
	{
		return m_State;
	}

	/**
	 * Cette m�thode change le dialog en fonction du nouvel �tat
	 * 
	 * @param state
	 *            Nouvelle �tat
	 */
	public void ChangeState(UpdateState state)
	{
		m_PreviousState = m_State;
		m_State = state;
		switch (m_State)
		{
		case RECHERCHE_EN_COURS:
			m_ChoixTypeMiseAJourLayout.setVisibility(View.GONE);
			m_RechercheMiseAJourLayout.setVisibility(View.VISIBLE);
			m_RechercheMiseAJourLayout.setPadding(10, 10, 10, 20);
			break;

		case RESULTAT_RECUS:
			UpdateReceiveDlg();
			break;

		case VERIFIER_VALIDITE:
			m_UpdateManager.VerifierValidite(m_PreviousState);
			break;

		case TELECHARGEMENT_EN_COURS:
			DownloadInProgressDlg();
			break;
		case ERREUR:
			DownloadCompletedDlg(m_ErrorString);
			break;
		case MISEAJOUR_TERMINE:
			DownloadCompletedDlg(getResources().getString(R.string.MiseAJourDlg_Mise_a_jour_terminee_succes) + "\n");
			break;

			
		case UNKNOWN:
			
			break;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		m_Context = this.getApplicationContext();
		this.setTitle(getResources().getString(R.string.update_manager_title));

		m_ChoixMiseAJourScrollLayout = new ScrollView(m_Context);
		m_ChoixMiseAJourLayout = new LinearLayout(m_Context);

		m_TelechargementLayout = new LinearLayout(m_Context);
		m_MiseAJourTermineLayout = new LinearLayout(m_Context);

		m_TelechargementLayout.setVisibility(View.GONE);
		m_ChoixMiseAJourLayout.setVisibility(View.GONE);
		m_ChoixMiseAJourScrollLayout.setVisibility(View.GONE);
		m_MiseAJourTermineLayout.setVisibility(View.GONE);
		UpdateModeDlg();

	}

	@Override
	public void onClick(View v)
	{
		switch (m_State)
		{
		case DEBUT:
			if (v.getId() == R.id.bContinuer)
			{
				ChangeState(UpdateState.RECHERCHE_EN_COURS);
				m_UpdateManager = new UpdateManager(this,  this.getApplicationContext());
				m_UpdateManager.VerifierVersionAppEtDb();
			} else if (v.getId() == R.id.bAnnuler)
			{
				m_ErrorString = getResources().getString(R.string.MiseAJourDlg_miseAjour_annule_par_utilisateur);
				ChangeState(UpdateState.ERREUR);
			}
			break;

		case RECHERCHE_EN_COURS:
			if (v.getId() == R.id.AnnulerMAJBtn)
			{
				m_ErrorString = getResources().getString(R.string.MiseAJourDlg_miseAjour_annule_par_utilisateur);
				ChangeState(UpdateState.ERREUR);
			}
			break;

		case RESULTAT_RECUS:
			if (BUTTON_MISE_A_JOUR == v.getId())
			{
				ChangeState(UpdateState.TELECHARGEMENT_EN_COURS);
				Vector<String> strUpdateArray = new Vector<String>();
				for(CheckBox c: m_Checkbox)
				{
					if(c.isChecked())
						strUpdateArray.add(c.getText().toString());
				}
				
				m_UpdateManager.GetDatabase(this, strUpdateArray);
			}
			break;

		case TELECHARGEMENT_EN_COURS:
			// TODO: Ajouter Bouton annuler t�l�chargement
			break;

		case ERREUR:
		case MISEAJOUR_TERMINE:
			if (BUTTON_TERMINER == v.getId())
			{
				Intent iMain = new Intent(this, TransportSelectDlg.class);
				this.startActivity(iMain);
			}

			break;

		default:

		}

	}

	// This prevent android from closing the activity when we rotate the lid.
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		// ChangeState(m_State);
	}

	void UpdateModeDlg()
	{
		this.setContentView(R.layout.typemiseajour);
		m_State = UpdateState.DEBUT;
		m_Checkbox = new Vector<CheckBox>();
		m_ChoixTypeMiseAJourLayout = (LinearLayout) findViewById(R.id.TypeMiseAJourLayout);
		m_ChoixTypeMiseAJourLayout.setPadding(10, 10, 10, 10);
		m_RechercheMiseAJourLayout = (LinearLayout) findViewById(R.id.RechercheMiseAJourLayout);

		m_ChoixTypeMiseAJourLayout.setVisibility(View.VISIBLE);
		m_RechercheMiseAJourLayout.setVisibility(View.GONE);

		// �cran Avertissement
		Button bContinuerMAJ = (Button) findViewById(R.id.bContinuer);
		bContinuerMAJ.setOnClickListener(this);

		Button bAnnulerMAJ = (Button) findViewById(R.id.bAnnuler);
		bAnnulerMAJ.setOnClickListener(this);

		TextView lConnectionType = (TextView) findViewById(R.id.lblConnectionType);
		TextView lblSDCard = (TextView) findViewById(R.id.lblSDCardDetection);
		TextView lblError = (TextView) findViewById(R.id.lblError);


		if (PreferenceManager.getDefaultSharedPreferences(this).getString("storage", "external").equals("internal"))
		{
			lblSDCard.setText("");
		}		
		else if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			lblSDCard.setText(getResources().getString(R.string.MiseAJourDlg_Carte_SD_absente));
			lblError.setText(getResources().getString(R.string.Erreur) + ": " + getResources().getString(R.string.MiseAJourDlg_Carte_SD_absente) + ". "
					+ getResources().getString(R.string.MiseAJourDlg_CarteSD_obligatoire_chargement_bases_de_donnees));
			bContinuerMAJ.setEnabled(false);
		} else
		{
			lblSDCard.setText(getResources().getString(R.string.MiseAJourDlg_Carte_SD_detectee));
		}

		// Network connection status
		//
		ConnectivityManager cm = (ConnectivityManager) m_Context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null)
		{
			if (info.isConnected())
			{
				lConnectionType.setText(getResources().getString(R.string.MiseAJourDlg_connecte_a) + ": " + info.getTypeName());
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(m_Context);
				String conType = preferences.getString("conType", "all-connect");
				if (!conType.equals("all-connect"))
				{
					if (conType.equals("only-wifi") && !(info.getTypeName().equals("WIFI")))
					{
						lConnectionType.setText(getResources().getString(R.string.MiseAJourDlg_connecte_a) + ": " + info.getTypeName());
						lblError.setText(getResources().getString(R.string.MiseAJourDlg_miseAjour_accessible_wifi));
						bContinuerMAJ.setEnabled(false);
					} else if (conType.equals("only-3g") && !(info.getTypeName().equals("MOBILE")))
					{
						lConnectionType.setText(getResources().getString(R.string.MiseAJourDlg_connecte_a) + ": " + info.getTypeName());
						lblError.setText(getResources().getString(R.string.MiseAJourDlg_miseAjour_accessible_3G));
						bContinuerMAJ.setEnabled(false);
					}
				}
			}

			else
			{
				lConnectionType.setText(getResources().getString(R.string.MiseAJourDlg_aucune_connexion_disponible));
				bContinuerMAJ.setEnabled(false);
			}
		} else
		{
			lConnectionType.setText(getResources().getString(R.string.MiseAJourDlg_aucune_connexion_disponible));
			bContinuerMAJ.setEnabled(false);
		}

		// �cran recherche de mise � jour
		Button bAnnulerMiseAJour = (Button) findViewById(R.id.AnnulerMAJBtn);
		bAnnulerMiseAJour.setOnClickListener(this);
	}

	public void ChangeDbToGet(Vector<UpdateStruct> vUpdate)
	{
		m_Update = vUpdate;
	}

	public void SetErrorString(String _error)
	{
		m_ErrorString = _error;
	}

	private void UpdateReceiveDlg()
	{
		m_ChoixMiseAJourLayout.setPadding(10, 10, 10, 10);
		TableLayout TableLayout = new TableLayout(m_Context);
		m_ChoixMiseAJourLayout.setOrientation(LinearLayout.VERTICAL);

		TableRow TableRowTitle = new TableRow(m_Context);

		TextView titre1 = new TextView(m_Context);
		titre1.setText(getResources().getString(R.string.MiseAJourDlg_Societe));
		titre1.setTextColor(Color.WHITE);
		TableRowTitle.addView(titre1);

		TextView tSizeTitle = new TextView(m_Context);
		tSizeTitle.setText("     " + getResources().getString(R.string.MiseAJourDlg_Grosseur));
		tSizeTitle.setTextColor(Color.WHITE);
		TableRowTitle.addView(tSizeTitle);


		try
		{
			// Ajouter importance
			TableLayout.addView(TableRowTitle);

			if (!m_Update.isEmpty())
			{
				for(UpdateStruct update : m_Update)
				{
					TableRow TableRow = new TableRow(m_Context);
					CheckBox cbTransport = new CheckBox(m_Context);
					cbTransport.setChecked(update.m_IsChecked);
					cbTransport.setText(update.m_strShortName);
					cbTransport.setMinWidth(40);
					cbTransport.setTextColor(Color.WHITE);
					m_Checkbox.add(cbTransport);

					TableRow.addView(cbTransport);

					TextView tSize = new TextView(m_Context);
					tSize.setTextColor(Color.WHITE);
					int nSize = Integer.valueOf(update.m_nSize) / 1000;

					tSize.setText("     " + nSize + " kb");
					TableRow.addView(tSize);

					TextView tImportance = new TextView(m_Context);
					tImportance.setTextColor(Color.WHITE);

					TableRow.addView(tImportance);
					TableLayout.addView(TableRow);
				}
				Button bMettreAJour = new Button(m_Context);
				// bMettreAJour.setLayoutParams(new
				// LayoutParams(LayoutParams.WRAP_CONTENT,
				// LayoutParams.WRAP_CONTENT));
				bMettreAJour.setText(getResources().getString(R.string.arret_bouton_miseajour));
				bMettreAJour.setId(BUTTON_MISE_A_JOUR);
				bMettreAJour.setOnClickListener(this);
				bMettreAJour.setGravity(Gravity.CENTER_HORIZONTAL);
				bMettreAJour.setTypeface(Typeface.DEFAULT_BOLD);
				bMettreAJour.setTextColor(Color.WHITE);
				bMettreAJour.setBackgroundResource(R.drawable.buttonbg);

				m_ChoixMiseAJourLayout.addView(TableLayout);
				m_ChoixMiseAJourLayout.addView(bMettreAJour);
				// ChoixMiseAJourLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
			}
			m_ChoixTypeMiseAJourLayout.setVisibility(View.GONE);
			m_RechercheMiseAJourLayout.setVisibility(View.GONE);
			m_ChoixMiseAJourScrollLayout.addView(m_ChoixMiseAJourLayout);
			this.addContentView(m_ChoixMiseAJourScrollLayout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			m_ChoixMiseAJourLayout.setVisibility(View.VISIBLE);
			m_ChoixMiseAJourScrollLayout.setVisibility(View.VISIBLE);
		} catch (NumberFormatException e)
		{
			m_ErrorString = getResources().getString(R.string.MiseAJourDlg_erreur_communication_serveur);
			ChangeState(UpdateState.ERREUR);
		}

	}

	private void DownloadInProgressDlg()
	{
		/*
		 * ProgressBar progressBar = new ProgressBar(m_Context); //TODO:
		 * Indeterminate progress bar for extraction ?
		 * progressBar.setIndeterminate(true);
		 */
		m_TelechargementLayout.setPadding(10, 10, 10, 10);
		m_TelechargementLayout.setOrientation(LinearLayout.VERTICAL);
		m_ViewUpdateProgress = new TextView(m_Context);
		m_ViewUpdateProgress.setTextColor(Color.WHITE);
		m_ViewUpdateProgress.setText(getResources().getString(R.string.MiseAJourDlg_progres_en_cours) + "\n");
		m_ViewUpdateProgress.setGravity(Gravity.CENTER_HORIZONTAL);
		m_UpdateProgressBar = new ProgressBar(m_Context, null, android.R.attr.progressBarStyleHorizontal);
		m_UpdateProgressBar.setMax(100);
		m_UpdateProgressBar.setIndeterminate(false);
		m_UpdateProgressBar.setMinimumWidth(180);
		m_TelechargementLayout.addView(m_ViewUpdateProgress);
		m_TelechargementLayout.addView(m_UpdateProgressBar);
		// m_TelechargementLayout.addView(progressBar);

		this.addContentView(m_TelechargementLayout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		m_ChoixTypeMiseAJourLayout.setVisibility(View.GONE);
		m_RechercheMiseAJourLayout.setVisibility(View.GONE);
		m_ChoixMiseAJourLayout.setVisibility(View.GONE);
		m_ChoixMiseAJourScrollLayout.setVisibility(View.GONE);
		m_TelechargementLayout.setVisibility(View.VISIBLE);
	}

	public void ChangeProgress(int nUpdate, String m_strUpdateProgress)
	{
		m_UpdateProgressBar.setProgress(nUpdate);
		m_ViewUpdateProgress.setText(m_strUpdateProgress);
	}

	private void DownloadCompletedDlg(String textBox)
	{

		m_MiseAJourTermineLayout.setPadding(10, 10, 10, 10);
		m_MiseAJourTermineLayout.setOrientation(LinearLayout.VERTICAL);
		// FinalLayout.setMinimumHeight(300);
		m_MiseAJourTermineLayout.setMinimumWidth(200);
		TextView tCompleter = new TextView(m_Context);
		tCompleter.setTextAppearance(m_Context, R.style.TexteStandard);
		tCompleter.setText(textBox);
		tCompleter.setGravity(Gravity.CENTER_HORIZONTAL);

		m_MiseAJourTermineLayout.addView(tCompleter);
		LinearLayout buttonLayout = new LinearLayout(m_Context);
		buttonLayout.setPadding(5, 15, 5, 15);
		Button bTerminer = new Button(m_Context);
		// bTerminer.setBackgroundResource(R.drawable.buttonbg);
		bTerminer.setTextAppearance(m_Context, R.style.BoutonStandard);
		bTerminer.setGravity(Gravity.CENTER_HORIZONTAL);
		bTerminer.setText(getResources().getString(R.string.MiseAJourDlg_Terminer));
		//bTerminer.setTypeface(Typeface.DEFAULT_BOLD);
	//	bTerminer.setTextColor(Color.WHITE);
		bTerminer.setBackgroundResource(R.drawable.buttonbg);
		bTerminer.setId(BUTTON_TERMINER);

		bTerminer.setOnClickListener(this);
		buttonLayout.addView(bTerminer, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		m_MiseAJourTermineLayout.addView(buttonLayout);
		m_ChoixTypeMiseAJourLayout.setVisibility(View.GONE);
		m_RechercheMiseAJourLayout.setVisibility(View.GONE);
		m_ChoixMiseAJourLayout.setVisibility(View.GONE);
		m_ChoixMiseAJourScrollLayout.setVisibility(View.GONE);
		m_TelechargementLayout.setVisibility(View.GONE);
		this.addContentView(m_MiseAJourTermineLayout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		m_MiseAJourTermineLayout.setVisibility(View.VISIBLE);
		//m_Context = null; //We deference the context to avoid Memory leak of the context
		//m_UpdateManager.Finish();
		//m_UpdateManager = null;
	}
	

	public void removeItem(Menu m)
	{
		m.removeItem(MenuCreator.MiseAJour);
	}


}
