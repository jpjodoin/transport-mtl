package net.rhatec.amtmobile.view;

import java.util.HashMap;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.constants.TypeString;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class FavorisGroupEditView extends LinearLayout
{

	CheckBox mCheckBox;
	// Ligne1
	public FavorisGroupEditView(Context context, HashMap<String,String> info)
	{
		super(context);
		this.setLayoutParams(new ListView.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		View w = View.inflate(context, R.layout.favorisitem, null);
		this.addView(w, this.getLayoutParams());
		
		
		TextView reseauTransport = (TextView) findViewById(R.id.reseauTransport);
		reseauTransport.setText(info.get(TypeString.SOCIETECODE));

		String from = getResources().getString(R.string.General_From);
		String to =   getResources().getString(R.string.General_To);
		
		TextView directionInfoPhrase = (TextView) findViewById(R.id.ExtraInfo);
		directionInfoPhrase.setText(to +" : "+info.get(TypeString.INFODIRECTIONPHRASE));

		TextView NoArretListTxt = (TextView) findViewById(R.id.NoArretListTxt);
		NoArretListTxt.setText(info.get(TypeString.NOARRET));

		TextView IntersectionTxt = (TextView) findViewById(R.id.IntersectionTxt);
		IntersectionTxt.setText(from +" : " +info.get(TypeString.INTERSECTIONSCONCAT));


		TextView AutobusTxt = (TextView) findViewById(R.id.AutobusTxt);
		AutobusTxt.setText(info.get(TypeString.NOCIRCUIT));
		

		
		//LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout1);
		

		//TableRow Ligne = (TableRow) findViewById(R.id.TableRow04);
		mCheckBox = (CheckBox) findViewById(R.id.checkBox);
		mCheckBox.setVisibility(VISIBLE);
		mCheckBox.setFocusable(false); // We don't want to steal from the list view the event

	}
	
	public void setChecked(boolean state)
	{
		mCheckBox.setChecked(state);
		mCheckBox.invalidate();
	}
	
	public boolean isChecked()
	{
		return mCheckBox.isChecked();
	}
	
}