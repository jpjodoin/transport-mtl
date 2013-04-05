package net.rhatec.amtmobile.view;

import java.util.HashMap;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.types.Temps;
import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HoraireItemView extends TableLayout
{

	public HoraireItemView(Context context, Temps time, HashMap<String, String> ExtraInfoMap, String extraInfo)
	{
		super(context);

		this.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 5, 5, 5);
		TableRow.LayoutParams rowparams = new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		TableRow row = new TableRow(context);

		// Première colonne
		LinearLayout layout = new LinearLayout(context);
		TextView heureDePassage = new TextView(context);
		heureDePassage.setTextColor(Color.WHITE);
		heureDePassage.setTextSize(14f);
		if (time.m_PassageAIntervalle)
		{
			heureDePassage.setText("   ...   ");
		} else
		{
			if (DateFormat.is24HourFormat(context))
			{
				heureDePassage.setText(time.m_Time.format("%H:%M"));
			} else
			{
				// Formatage am/pm
				heureDePassage.setText(time.m_Time.format("%I:%M%p"));
			}
		}
		layout.addView(heureDePassage, params);
		layout.setGravity(Gravity.CENTER_VERTICAL);
		ImageView vHandicape = new ImageView(context);

		if (time.m_PourHandicape)
			vHandicape.setBackgroundResource(R.drawable.handicap_on);
		else
			vHandicape.setBackgroundResource(R.drawable.handicap_off);

		layout.addView(vHandicape, params);

		if (time.m_Lundi | time.m_Mardi | time.m_Mercredi | time.m_Jeudi | time.m_Vendredi | time.m_Samedi | time.m_Dimanche)
		{

			String infoJournee = getResources().getString(R.string.HoraireItemView_Passage_le);
			if (time.m_Lundi)
				infoJournee += getResources().getText(R.string.jour_lundi) + " ";
			if (time.m_Mardi)
				infoJournee += getResources().getText(R.string.jour_mardi) + " ";
			if (time.m_Mercredi)
				infoJournee += getResources().getText(R.string.jour_mercredi) + " ";
			if (time.m_Jeudi)
				infoJournee += getResources().getText(R.string.jour_jeudi) + " ";
			if (time.m_Vendredi)
				infoJournee += getResources().getText(R.string.jour_vendredi) + " ";
			if (time.m_Samedi)
				infoJournee += getResources().getText(R.string.jour_samedi) + " ";
			if (time.m_Dimanche)
				infoJournee += getResources().getText(R.string.jour_dimanche) + " ";

			infoJournee += getResources().getString(R.string.HoraireItemView_seulement) + ".";

			TextView journee = new TextView(context);
			journee.setText(infoJournee);
			journee.setTextColor(Color.WHITE);
			journee.setTextSize(14f);
			layout.addView(journee, params);
		}

		if (time.m_PassageAIntervalle)
		{
			TextView horaireSpeciale = new TextView(context);
			horaireSpeciale.setText(getResources().getString(R.string.HoraireItemView_Passage_au_6_minutes_ou_moins));
			horaireSpeciale.setTextColor(Color.WHITE);
			horaireSpeciale.setTextSize(14f);
			layout.addView(horaireSpeciale, params);
		}

		if (time.m_HoraireSpecial)
		{
			TextView horaireSpeciale = new TextView(context);
			horaireSpeciale.setText(ExtraInfoMap.get(extraInfo));
			horaireSpeciale.setTextColor(Color.WHITE);
			horaireSpeciale.setTextSize(14f);
			horaireSpeciale.setMaxLines(4);
			layout.addView(horaireSpeciale, params);
		}

		layout.setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		row.addView(layout, rowparams);

		// Colonne de filler pour pouvoir cliquer à partir de n'importe où
		TextView filler = new TextView(context);
		filler.setText("");
		row.addView(filler, rowparams);
		this.setColumnStretchable(row.getChildCount() - 1, true);

		this.addView(row, new ListView.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

	}
}
