package net.rhatec.amtmobile.view;


import java.util.Vector;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.helpers.StringHelpers;
import net.rhatec.amtmobile.manager.FavorisManager;
import net.rhatec.amtmobile.providers.TransportProvider;
import net.rhatec.amtmobile.types.DateHelpers;
import net.rhatec.amtmobile.types.Favoris;
import net.rhatec.amtmobile.types.Pair;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Hocine
 * @description: Classe qui s<occupe de rempli les champs défini dans la vue
 *               R.layout.favorisitem et qui ajoute ou pas une croix si on passe
 *               au mode Edition
 * 
 */
public class FavorisItemView extends RelativeLayout
{


	public FavorisItemView(Context context, Favoris f, boolean _visualisation)
	{
		super(context);
		//Calendar unCalendrier = Calendar.getInstance();
		
		Pair<Integer, Boolean> pair = DateHelpers.obtenirTypeHoraireActuelAConsulter(f.m_vHoraire);
		
		
		//TODO : Affichage info circuit phrase
		this.setLayoutParams(new ListView.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT));
		View w = View.inflate(context, R.layout.favorisitem, null);
		this.addView(w, this.getLayoutParams());

		String from = getResources().getString(R.string.General_From);
		String to =   getResources().getString(R.string.General_To);
		TextView reseauTransport = (TextView) findViewById(R.id.reseauTransport);
		reseauTransport.setText(f.m_strTransportService);

		TextView directionInfoPhrase = (TextView) findViewById(R.id.ExtraInfo);
		directionInfoPhrase.setText(f.m_codeInfoDirection.equals("null") ? " " : to+" : "+ TransportProvider.ObtenirPhraseInfoDirectionSpecifique(context, f.m_strTransportService, f.m_codeInfoDirection));

		TextView NoArretListTxt = (TextView) findViewById(R.id.NoArretListTxt);
		NoArretListTxt.setText(f.m_strNoArret);

		TextView IntersectionTxt = (TextView) findViewById(R.id.IntersectionTxt);
		IntersectionTxt.setText(from+" : "+f.m_strIntersection);
		TextView ProchainPassageTxt = (TextView) findViewById(R.id.ProchainPassageTxt); //On n'affiche pas les prochains passage pendant l'�dition
		if(pair.first >= 0 && pair.first < f.m_vHoraire.size())
		{
			Vector<String> passages = f.m_vHoraire.get(pair.first).ObtenirProchainPassage(6, pair.second, context);//);
			
			if(passages.size() > 0)
			{
				
				StringBuilder sb = new StringBuilder();
				sb.append("<font color=\'#ADFF2F\'>");
				sb.append(passages.get(0));
				sb.append("</font>");
				passages.set(0, sb.toString());
	
			}
			
			ProchainPassageTxt.setText(Html.fromHtml(StringHelpers.putSpaceOrNewLineBetweenStrings(passages, FavorisManager.nombreHoraireParLigne, "<br>")), TextView.BufferType.SPANNABLE);
		}
		else
			ProchainPassageTxt.setText(R.string.FavorisDlg_Aucun_Passage);
			
			
			

		TextView AutobusTxt = (TextView) findViewById(R.id.AutobusTxt);
		AutobusTxt.setText(f.m_strNoBus);
	}
}
