package net.rhatec.amtmobile.dialog;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.baseactivity.ActivityWithMenu;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DatabaseNotFoundDlg extends ActivityWithMenu implements OnClickListener
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nodatabase);

		Button bUpdate = (Button) findViewById(R.id.bUpdate);
		bUpdate.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		Intent iUpgrade = new Intent(this, MiseAJourDlg.class);
		this.startActivity(iUpgrade);
	}
}
