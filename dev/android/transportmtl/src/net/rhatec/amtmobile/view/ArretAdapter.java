package net.rhatec.amtmobile.view;

import java.util.List;

import net.rhatec.amtmobile.types.Arret;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ArretAdapter extends BaseAdapter
{
	Context		m_Context;
	List<Arret>	m_ListeArret;

	public ArretAdapter(Context _context, List<Arret> _tempsListe)
	{
		this.m_Context = _context;
		this.m_ListeArret = _tempsListe;
		
	}

	@Override
	public int getCount()
	{
		return m_ListeArret.size();
	}

	@Override
	public Arret getItem(int _position)
	{
		return m_ListeArret.get(_position);
	}

	@Override
	public long getItemId(int _position)
	{
		return _position;
	}

	@Override
	public View getView(int _position, View _convertView, ViewGroup _parent)
	{
		Arret arret = m_ListeArret.get(_position);
		return new ArretItemView(this.m_Context, arret);
	}

}
