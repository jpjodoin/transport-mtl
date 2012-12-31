package net.rhatec.amtmobile.dialog;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.baseactivity.ActivityWithMenu;
import net.rhatec.amtmobile.constants.TypeString;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AvertissementValiditeDlg extends ActivityWithMenu implements OnClickListener
{
	CheckBox	cValidity;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.avertissementvalidite);
		Bundle b = this.getIntent().getExtras();
		TableLayout layoutTable = (TableLayout) findViewById(R.id.layoutListeAvertissement);
		layoutTable.setOnClickListener(this);

		String[] obsoleteDBA = b.getStringArray(TypeString.DBAOBSOLETEARRAY);
		int numberObsoleteDBA = obsoleteDBA.length;
		for (int i = 0; i < numberObsoleteDBA; i = i + 3)
		{
			TableRow row1 = new TableRow(this);
			TextView nom = new TextView(this);
			nom.setText(obsoleteDBA[i]);
			nom.setTextAppearance(this, R.style.TexteStandard);
			nom.setPadding(15, 0, 15, 0);

			TextView dateDebut = new TextView(this);
			dateDebut.setText(obsoleteDBA[i + 1]);
			dateDebut.setTextAppearance(this, R.style.TexteStandard);
			dateDebut.setPadding(15, 0, 15, 0);

			TextView dateFin = new TextView(this);
			dateFin.setText(obsoleteDBA[i + 2]);
			dateFin.setTextAppearance(this, R.style.TexteStandard);
			dateFin.setPadding(15, 0, 15, 0);

			row1.addView(nom);
			row1.addView(dateDebut);
			row1.addView(dateFin);
			layoutTable.addView(row1, new TableRow.LayoutParams());
		}

		cValidity = (CheckBox) findViewById(R.id.cbNePlusAvertir);
		cValidity.setOnClickListener(this);

		Button bMAJ = (Button) findViewById(R.id.bMAJ);
		bMAJ.setOnClickListener(this);

		Button bIgnore = (Button) findViewById(R.id.bIgnorer);
		bIgnore.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{

		case R.id.bMAJ:
			Intent iUpgrade = new Intent(this, MiseAJourDlg.class);
			this.startActivity(iUpgrade);
			break;

		case R.id.bIgnorer:
			finish();
			break;

		case R.id.cbNePlusAvertir:
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			preferences.edit().putBoolean("warningValidity", !cValidity.isChecked()).commit();
			break;

		default:

		}

	}
}
