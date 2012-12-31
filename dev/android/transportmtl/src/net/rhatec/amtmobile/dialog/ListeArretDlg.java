package net.rhatec.amtmobile.dialog;

import java.util.List;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.baseactivity.ListActivityWithMenu;
import net.rhatec.amtmobile.constants.TypeString;
import net.rhatec.amtmobile.providers.TransportProvider;
import net.rhatec.amtmobile.types.Arret;
import net.rhatec.amtmobile.view.ArretAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListeArretDlg extends ListActivityWithMenu implements OnItemClickListener
{
	// ArrayList<HashMap<String,String>> m_Liste = new
	// ArrayList<HashMap<String,String>>();
	List<Arret>	vListeArret;
	String		m_TransportName;
	String		m_NoAutobus;
	String		m_Direction;
	String		m_InfoDirectionCode;
	String		m_InfoCircuitCode;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle b = this.getIntent().getExtras();
		m_TransportName = b.getString(TypeString.SOCIETECODE);
		this.setTitle(getResources().getString(R.string.ListeArretDlg_Liste_d_arret_pour) + " " + m_TransportName.toUpperCase());
		m_NoAutobus = b.getString(TypeString.NOCIRCUIT);
		m_Direction = b.getString(TypeString.DIRECTION);
		m_InfoDirectionCode = b.getString(TypeString.INFODIRECTIONCODE);
		m_InfoCircuitCode = b.getString(TypeString.INFOCIRCUITCODE);

		ListView lView = getListView();
		lView.setLongClickable(false);
		lView.setClickable(true);
		lView.setOnItemClickListener(this);

		vListeArret = TransportProvider.ObtenirListeArret(m_TransportName, m_NoAutobus, m_InfoCircuitCode, m_Direction, m_InfoDirectionCode);
		ArretAdapter liste = new ArretAdapter(this, vListeArret);
		setListAdapter(liste);

	}

	// TODO: Hardcode à enlever
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id)
	{
		Arret item = vListeArret.get(position);
		Bundle b = new Bundle();
		Intent iHoraire = null;
		iHoraire = new Intent(ListeArretDlg.this, HoraireDlg.class);


		b.putString(TypeString.SOCIETECODE, m_TransportName);
		b.putString(TypeString.NOCIRCUIT, m_NoAutobus);
		b.putString(TypeString.NOARRET, item.ObtenirNumero());
		b.putString(TypeString.DIRECTION, m_Direction);
		b.putString(TypeString.INFODIRECTIONCODE, m_InfoDirectionCode);
		b.putString(TypeString.INFOCIRCUITCODE, m_InfoCircuitCode);
		b.putString(TypeString.POSITIONOCTET, item.ObtenirPositionDansFichier());
		iHoraire.putExtras(b);
		this.startActivityForResult(iHoraire, 1);
	}

	// Quand l'activité retourne
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_FIRST_USER)
		{
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(getResources().getString(R.string.ListeArretDlg_Aucun_horaire_disponible));
			alertDialog.setMessage(getResources().getString(R.string.listearret_notification_aucunarret_msg));
			alertDialog.setButton(getResources().getString(R.string.general_ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					return;
				}
			});
			alertDialog.show();
		} else if (resultCode == 6)
		{
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(getResources().getString(R.string.ListeArretDlg_Aucun_horaire_disponible));
			alertDialog.setMessage(getResources().getString(R.string.ListeArretDlg_Debarcadere_seulement) + ".");
			alertDialog.setButton(getResources().getString(R.string.general_ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					return;
				}
			});
			alertDialog.show();
		}

	}

}