package net.rhatec.amtmobile.view;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.types.Arret;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ArretItemView extends LinearLayout
{

	public ArretItemView(Context _context, Arret _arret)
	{
		super(_context);
		this.setOrientation(HORIZONTAL);

		this.setLayoutParams(new ListView.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT));
		View vv = View.inflate(_context, R.layout.itemarretlist, null);
		this.addView(vv, this.getLayoutParams());

		TextView noArret = (TextView) findViewById(R.id.noarretliste);
		noArret.setText(_arret.ObtenirNumero());

		TextView nomIntersection1 = (TextView) findViewById(R.id.intersection1liste);
		nomIntersection1.setText(_arret.ObtenirNomRue1());

		/*String nomRue = _arret.ObtenirNomRue2();
		
			
		
		LinearLayout intersectionLayout = (LinearLayout) findViewById(R.id.IntersectionLayout);

		if (_arret.ObtenirMetro())
		{
			ImageView mMetro = new ImageView(_context);
			mMetro.setBackgroundResource(R.drawable.micon);
			intersectionLayout.addView(mMetro);
		}

		if (_arret.ObtenirTrain())
		{
			ImageView mTrain = new ImageView(_context);
			mTrain.setBackgroundResource(R.drawable.ticon);
			intersectionLayout.addView(mTrain);
		}*/

	}

}
