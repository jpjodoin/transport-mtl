package net.rhatec.amtmobile.view;

import java.util.HashMap;
import java.util.List;

import net.rhatec.amtmobile.types.Temps;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class HoraireAdapter extends BaseAdapter
{

	private Context					context;
	private List<Temps>				tempsListe;
	private HashMap<String, String>	m_HoraireExtraInfoMap;
	private String					m_ExtraInfo;

	public HoraireAdapter(Context _context, List<Temps> _tempsListe, HashMap<String, String> _horaireExtraInfoMap, String _extraInfo)
	{
		this.context = _context;

		this.tempsListe = _tempsListe;
		this.m_HoraireExtraInfoMap = _horaireExtraInfoMap;
		this.m_ExtraInfo = _extraInfo;
	}

	@Override
	public int getCount()
	{
		return tempsListe.size();
	}

	@Override
	public Object getItem(int position)
	{
		return tempsListe.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Temps temps = tempsListe.get(position);
		return new HoraireItemView(this.context, temps, m_HoraireExtraInfoMap, m_ExtraInfo);
	}

}
