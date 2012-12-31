package net.rhatec.amtmobile.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.types.Favoris;
import net.rhatec.amtmobile.types.FavorisGroupe;
import net.rhatec.amtmobile.types.FavorisNode;
import net.rhatec.amtmobile.types.FavorisType;
import net.rhatec.amtmobile.types.Horaire;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Hocine
 * @description: Adapter qui donne acc�s � une liste d'objet de Favoris
 */
public class FavorisItemAdapter extends BaseExpandableListAdapter
{
	private static final int[] EMPTY_STATE_SET = {};
    private static final int[] GROUP_EXPANDED_STATE_SET =
            {android.R.attr.state_expanded};
    private static final int[][] GROUP_STATE_SETS = {
         EMPTY_STATE_SET, // 0
         GROUP_EXPANDED_STATE_SET // 1
	};

	private Context								context;

	private boolean								m_visualisation;

	
	ArrayList<FavorisNode> mGroupes = new ArrayList<FavorisNode>();
    private LayoutInflater inflater;

	public FavorisItemAdapter(Context _context, ArrayList<FavorisNode> _list, boolean _visualisation)
	{
		this.context = _context;
		this.mGroupes = _list;
		this.m_visualisation = _visualisation;
		
		inflater = LayoutInflater.from(context);
	}
	



	@Override
	public Favoris getChild(int groupPosition, int childPosition) 
	{
		Favoris f = null;
		FavorisNode n = mGroupes.get(groupPosition);
		if(n.getType() == FavorisType.FavorisGroupe)
		{
			FavorisGroupe g = (FavorisGroupe) n;
			f = g.obtenirFavoris().get(childPosition);
		}
		
		return f;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,	boolean isLastChild, View convertView, ViewGroup parent) 
	{
		Favoris f = null;
		FavorisNode n = mGroupes.get(groupPosition);
		if(n.getType() == FavorisType.FavorisGroupe)
		{
			FavorisGroupe g = (FavorisGroupe) n;
			f = g.obtenirFavoris().get(childPosition);
		}
		return new FavorisItemView(this.context, f, m_visualisation); // TODO: Réutiliser convertView ?
	}

	@Override
	public int getChildrenCount(int groupPosition) 
	{
		int count = 0;
		FavorisNode n = mGroupes.get(groupPosition);
		if(n.getType() == FavorisType.FavorisGroupe)
		{
			FavorisGroupe g = (FavorisGroupe) n;
			count = g.obtenirFavoris().size();
		}
		return count;
	}

	@Override
	public FavorisNode getGroup(int groupPosition) 
	{
		return mGroupes.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mGroupes.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,	View convertView, ViewGroup parent)
	{
		
		
		FavorisNode n = getGroup(groupPosition);
		if(n.getType() == FavorisType.FavorisGroupe)
		{
			if(m_visualisation == false)
			{
				 ExpandableListView eLV = (ExpandableListView) parent;
				 eLV.expandGroup(groupPosition);
			}
		   
			
			FavorisGroupe group = (FavorisGroupe) n;
			FavorisGroupeItemView view;			
			
			//if (convertView == null) 
			//{
				
				view = new FavorisGroupeItemView();
				convertView = inflater.inflate(R.layout.groupefavoris, null);
				view.mName = (TextView) convertView.findViewById(R.id.group_name);			
				convertView.setTag(view);
				convertView.setBackgroundColor(group.obtenirCouleur());
			
				
			//} 
			/*else 
			{
				view = (FavorisGroupeItemView) convertView.getTag();
			}*/
			
			View ind = convertView.findViewById( R.id.explist_indicator);
			ImageView indicator = (ImageView)ind;
			if( getChildrenCount( groupPosition ) == 0 ) 
			{
				indicator.setVisibility( View.INVISIBLE );
			} else 
			{
				indicator.setVisibility( View.VISIBLE );
				int stateSetIndex = ( isExpanded ? 1 : 0) ;
				Drawable drawable = indicator.getDrawable();
				drawable.setState(GROUP_STATE_SETS[stateSetIndex]);
			}
			
			view.mName.setText(group.obtenirNom());
		}
		else if(n.getType() == FavorisType.Favoris)
		{
			Favoris f = (Favoris) n;
			convertView = new FavorisItemView(this.context, f, m_visualisation); 
		}
		else
		{
			
		}
		
		
		// TODO Auto-generated method stub
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
}
