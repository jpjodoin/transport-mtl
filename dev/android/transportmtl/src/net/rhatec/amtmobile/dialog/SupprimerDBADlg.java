package net.rhatec.amtmobile.dialog;

import java.util.Vector;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.baseactivity.ActivityWithMenu;
import net.rhatec.amtmobile.manager.FavorisManager;
import net.rhatec.amtmobile.manager.ListeSocieteManager;
import net.rhatec.amtmobile.providers.TransportProvider;
import net.rhatec.amtmobile.types.FileManager;
import net.rhatec.amtmobile.types.TransportServiceBase;

public class SupprimerDBADlg extends ActivityWithMenu implements OnClickListener
{

	Vector<CheckBox>				m_CheckBoxArray		= new Vector<CheckBox>(10);
	Vector<String>					m_transportToDelete	= new Vector<String>(10);
	Button							m_bSupprimer;
	ProgressDialog					m_dialog;
	int								m_StateProgress;

	Vector<TransportServiceBase>	m_liste;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		TableLayout choixMiseAJourLayout = new TableLayout(this);
		choixMiseAJourLayout.setPadding(10, 10, 10, 10);
		TableLayout tableLayout = new TableLayout(this);
		choixMiseAJourLayout.setOrientation(LinearLayout.VERTICAL);

		TableRow tableRowTitle = new TableRow(this);

		TextView titre1 = new TextView(this);
		titre1.setText(getResources().getString(R.string.MiseAJourDlg_Societe));
		titre1.setTextAppearance(this.getBaseContext(), R.style.TexteStandard);
		tableRowTitle.addView(titre1);
		tableLayout.addView(tableRowTitle);

		m_liste = ListeSocieteManager.obtenirListe();

		if (!m_liste.isEmpty())
		{
			
			for (TransportServiceBase t: m_liste)
			{
				TableRow tableRow = new TableRow(this);
				CheckBox cbTransport = new CheckBox(this);
				cbTransport.setChecked(true);
				cbTransport.setText(t.getLongName());
				cbTransport.setMinWidth(40);
				cbTransport.setTextAppearance(this.getBaseContext(), R.style.TexteStandard);
				m_CheckBoxArray.add(cbTransport);
				tableRow.addView(cbTransport);
				tableLayout.addView(tableRow);

			}
		}

		m_bSupprimer = new Button(this);
		m_bSupprimer.setText(getResources().getString(R.string.SupprimerDBADlg_Supprimer));
		m_bSupprimer.setOnClickListener(this);
		m_bSupprimer.setGravity(Gravity.CENTER_HORIZONTAL);
		//m_bSupprimer.setTypeface(Typeface.DEFAULT_BOLD);
		m_bSupprimer.setTextAppearance(this.getBaseContext(), R.style.TexteStandard);
		m_bSupprimer.setBackgroundResource(R.drawable.buttonbg);

		choixMiseAJourLayout.addView(tableLayout);
		choixMiseAJourLayout.addView(m_bSupprimer);
		ScrollView scrollView = new ScrollView(this);
		scrollView.addView(choixMiseAJourLayout);
		this.setContentView(scrollView);

	}

	@Override
	public void onClick(View v)
	{
		if (m_bSupprimer.getId() == v.getId())
		{

			int numCheckBox = m_CheckBoxArray.size();
			for (int i = 0; i < numCheckBox; ++i)
			{
				CheckBox cb = m_CheckBoxArray.get(i);
				if (cb.isChecked())
					m_transportToDelete.add(m_liste.get(i).getShortName());

			}
			
			m_dialog = ProgressDialog.show(this, "", getResources().getString(R.string.SupprimerDBADlg_Attendre), true);
			
			supprimerDBASelectionne();
		}

	}

	final Handler	mHandler		= new Handler();

	final Runnable mUpdateResults = new Runnable() 
	{
										@Override
										public void run()
										{
											updateResultsInUi();
										}
									};

	private void updateResultsInUi()
	{
		if (m_StateProgress == 0)
			m_dialog.show();
		else
			m_dialog.dismiss();
	}

	public void supprimerDBASelectionne()
	{
		Thread runningThread = new Thread() {
			@Override
			public void run()
			{
				m_StateProgress = 0;
				mHandler.post(mUpdateResults);
				for (String societe : m_transportToDelete)
				{
					FileManager.deleteDir(TransportProvider.getRootPath() + societe + "/");
					ListeSocieteManager.supprimerSociete(societe);
				}
				FavorisManager.supprimerFavorisSociete(m_transportToDelete);
				m_StateProgress = 1;
				mHandler.post(mUpdateResults);
				finish();
			}
		};
		runningThread.start();
	}

}
