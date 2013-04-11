package net.rhatec.amtmobile.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.baseactivity.ActivityWithMenu;
import net.rhatec.amtmobile.constants.TypeString;
import net.rhatec.amtmobile.helpers.MenuCreator;
import net.rhatec.amtmobile.manager.FavorisManager;
import net.rhatec.amtmobile.types.Favoris;
import net.rhatec.amtmobile.types.FavorisGroupe;
import net.rhatec.amtmobile.types.FavorisNode;
import net.rhatec.amtmobile.types.FavorisType;
import net.rhatec.amtmobile.view.FavorisItemAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;


/*
 * Activit� qui affiche les favoris de l'application dans un dialog
 */
public class FavorisDlg extends ActivityWithMenu implements OnChildClickListener, OnGroupClickListener, OnItemLongClickListener
{



	// Membres
	private  ArrayList<FavorisNode>	m_List;
	ExpandableListView							m_ListeView;
	FavorisItemAdapter m_ItemAdapter = null;

	private int m_OptionCreerGroupe;
	private int m_OptionInformation;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favoris);
		this.setTitle(this.getResources().getString(R.string.FavorisDlg_Titre));



		m_ListeView = (ExpandableListView) findViewById(R.id.list);
		m_ListeView.setGroupIndicator(null); 
		m_ListeView.setOnGroupClickListener(this);
	
		m_ListeView.setOnChildClickListener(this);	
		m_ListeView.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);
		m_ListeView.setLongClickable(true);
		m_ListeView.setOnItemLongClickListener(this);
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
		reloadBookmarks();
	}
	

	@Override
	public boolean onGroupClick(ExpandableListView arg0, View view, int position, long id) 
	{
		boolean success = false;
		FavorisNode item = m_List.get(position);
		if(item.getType() == FavorisType.Favoris)
		{
			Favoris f = (Favoris)item;
			success = GoToSchedule(f);
		}

		return success;

	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
	{
		boolean success = false;
		FavorisNode item = m_List.get(groupPosition);
		if(item.getType() == FavorisType.FavorisGroupe)
		{
			FavorisGroupe fg = (FavorisGroupe) item;
			Favoris f = fg.obtenirFavoris().get(childPosition);
			success = GoToSchedule(f);
		}

		return success;
	}



	boolean GoToSchedule(FavorisNode item)
	{
		boolean used = false;
		if(item.getType() == FavorisType.Favoris)
		{
			Favoris f = (Favoris) item;

				Intent iHoraire = new Intent(this, HoraireDlg.class);

				Bundle bHoraire = new Bundle();
				bHoraire.putString(TypeString.SOCIETECODE, f.m_strTransportService);
				bHoraire.putString(TypeString.NOCIRCUIT, f.m_strNoBus);
				bHoraire.putString(TypeString.NOARRET, f.m_strNoArret);
				bHoraire.putString(TypeString.DIRECTION, f.m_strDirection);
				bHoraire.putString(TypeString.INFODIRECTIONCODE, f.m_codeInfoDirection);
				bHoraire.putString(TypeString.INFOCIRCUITCODE, f.m_codeInfoCircuit);
				bHoraire.putString(TypeString.POSITIONOCTET, String.valueOf(f.m_nLigneFavoris));
				iHoraire.putExtras(bHoraire);
				
				this.startActivity(iHoraire);

			

			used = true;
		}
		return used;
	}

	/** hook into menu button for activity */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		m_OptionCreerGroupe = MenuCreator.populateMenu(menu, this.getBaseContext()) + 1;
		
		MenuItem item = menu.add(Menu.NONE, m_OptionCreerGroupe, Menu.NONE, this.getString(R.string.menu_option_ajouter_groupe));
		item.setAlphabeticShortcut('g');
		item.setIcon(R.drawable.ic_action_group);
		m_OptionInformation = m_OptionCreerGroupe+1;
		MenuItem  optionInfoItem = menu.add(Menu.NONE, m_OptionInformation, Menu.NONE, this.getString(R.string.menu_option_info));
		optionInfoItem.setAlphabeticShortcut('i');
		optionInfoItem.setIcon(R.drawable.ic_action_info);
		
		return true;
	}

	/** when menu button option selected */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		boolean used = false;
		int nItemId = item.getItemId();
		if (nItemId == m_OptionCreerGroupe)
		{
			used = true;
			//TODO: Fonction pour éditer les groupes
			Bundle b = new Bundle();
			ArrayList<String> bookmarkList = new ArrayList<String>(10);
			for( FavorisNode fn : m_List)
			{
				if(fn.getType() == FavorisType.Favoris)
				{
					Favoris f = (Favoris) fn;
					bookmarkList.add(FavorisManager.SerializeBookmarkHeader(this, f));
					
				}
			}
			
			
			b.putStringArrayList(TypeString.BOOKMARKS, bookmarkList);
			Intent iCreateGroup = new Intent(this, CreateGroupDlg.class);
			iCreateGroup.putExtras(b);			
			this.startActivityForResult(iCreateGroup, 1);

		}
		else if(nItemId == m_OptionInformation)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setTitle(getResources().getString(R.string.CreateGroupDlg_ModifierFavorisGroupe));
		    builder.setMessage(getResources().getString(R.string.CreateGroupDlg_ModifierFavorisInfo));
		    builder.setPositiveButton(getResources().getString(R.string.CreateGroupDlg_Daccord), new DialogInterface.OnClickListener() {
		    	  @Override
		    	  public void onClick(DialogInterface dialog, int which) {
		    	    dialog.dismiss();
		    	  }
		    	});

		    AlertDialog dlg = builder.create();
		    dlg.show();
		}
		else
		{
			used = super.onOptionsItemSelected(item);
		}
		//MenuCreator.startActivityFromMenu(item, this);
		return used;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 1) //CREATE
		{
			String groupName = data.getStringExtra(TypeString.GROUPNAME);
			String groupColor = data.getStringExtra(TypeString.COLOR);
			ArrayList<Integer> selectedBookmark = data.getIntegerArrayListExtra(TypeString.SELECTEDBOOKMARKS);
			ArrayList<FavorisNode> toRemove = new ArrayList<FavorisNode>();
			Set<Integer> set = new HashSet<Integer>(selectedBookmark);
			FavorisGroupe newBookmarkGroup = new FavorisGroupe(groupName, groupColor);
			//TODO: We should not regenerated each time...
			int idx = 0;
			for(FavorisNode fn : m_List)
			{
				
				if(fn.getType() == FavorisType.Favoris)
				{
					Favoris f = (Favoris) fn;
					if(set.contains(idx))
					{
						newBookmarkGroup.ajouterFavoris(f);
						toRemove.add(f);
					}	
					++idx;
				}
			}
			m_List.removeAll(toRemove);			
			m_List.add(newBookmarkGroup);
			FavorisManager.updateFavoris(this, m_List);
			reafficherListe();
		}
		else if(resultCode == 2) //Edit
		{
			int currentPos = data.getIntExtra(TypeString.CURRENTPOSITION, 0);
			FavorisNode fn = m_List.get(currentPos);
			FavorisGroupe fg = (FavorisGroupe)fn;
	
			fg.definirNom(data.getStringExtra(TypeString.GROUPNAME));
			fg.definirCouleur(data.getStringExtra(TypeString.COLOR));
			ArrayList<Integer> selectedBookmark = data.getIntegerArrayListExtra(TypeString.SELECTEDBOOKMARKS);
			Set<Integer> set = new HashSet<Integer>(selectedBookmark);

			
			ArrayList<Favoris> favorisListe = fg.obtenirFavoris();
			ArrayList<FavorisNode> nodeList = new ArrayList<FavorisNode>(10);
			ArrayList<Favoris> groupeFavoris = new ArrayList<Favoris>(10);
			

			for(int i = 0; i < favorisListe.size(); ++i)
			{				
				if(set.contains(i))
					groupeFavoris.add(favorisListe.get(i));
				else
				{
					nodeList.add(favorisListe.get(i));
				}
			}
			int idx = favorisListe.size();
			for(FavorisNode n : m_List)
			{
				
				if(n.getType() == FavorisType.Favoris)
				{
					if(set.contains(idx))
					{
						Favoris f= (Favoris) n;					
						groupeFavoris.add(f);						
					}
					else
						nodeList.add(n);
					++idx;
				}
				else
					nodeList.add(n);				
			}
			
		
			fg.definirListeDeFavoris(groupeFavoris);	
			int newGIdx = nodeList.indexOf(fn);
			nodeList.set(newGIdx, fg);
			m_List = nodeList;		
			FavorisManager.updateFavoris(this, m_List);
			reafficherListe();
			
			
		}

	}
	
	public void reloadBookmarks()
	{
		m_List = FavorisManager.obtenirFavoris(getApplicationContext());
		reafficherListe();
		//m_ItemAdapter = new FavorisItemAdapter(this, m_List, true);
		//m_ListeView.setAdapter(m_ItemAdapter);
	}
	
	/**
	 * @function: reafficherListe
	 * @description: Demande a reafficher la liste des favoris
	 * @author: Hocine
	 * @params[in]:
	 * @params[out]:
	 */
	public void reafficherListe()
	{
		long[] ids = getExpandedIds();
		m_ItemAdapter = new FavorisItemAdapter(this, m_List, true);
		m_ListeView.setAdapter(m_ItemAdapter);
		this.restoreExpandedState(ids);
	}

	

	@Override
	public void removeItem(Menu m)
	{
		m.removeItem(MenuCreator.Favoris);
	}
	
	public void updateAndSaveList()
	{
		FavorisManager.updateFavoris(this, m_List);
		reafficherListe();
	}

	

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) 
	{
		final int groupPosition = ExpandableListView.getPackedPositionGroup(id);
		final FavorisNode fg = m_List.get(groupPosition);
		FavorisNode fn = fg;

		final int childPosition;
		boolean inGroup = false;
		if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) 
        {        	
            childPosition = ExpandableListView.getPackedPositionChild(id);
            FavorisGroupe g = (FavorisGroupe) fg;
			fn = g.obtenirFavoris().get(childPosition);
            inGroup = true;        	
        }
		else
		{
			childPosition = 0;
		}			


		if(fn.getType() == FavorisType.FavorisGroupe)
		{
			final FavorisGroupe group = (FavorisGroupe) fn;
			final CharSequence[] options = {getResources().getString(R.string.CreateGroupDlg_Editer), getResources().getString(R.string.CreateGroupDlg_SupprimerGroupeEtFavoris), getResources().getString(R.string.CreateGroupDlg_SupprimerGroupeSeulement), getResources().getString(R.string.CreateGroupDlg_Monter), getResources().getString(R.string.CreateGroupDlg_Descendre)};
			final Context context = this;
		    AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setTitle(getResources().getString(R.string.CreateGroupDlg_ModifierGroupe));
		    builder.setItems(options, new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which)
		        {
		        	switch(which)
		        	{
		        	case 0: //Edit group
		        		Bundle b = new Bundle();
		        		ArrayList<String> serializedBookmarkList = new ArrayList<String>(10);
		        		ArrayList<Favoris> bookmarkListGroup = group.obtenirFavoris();
		        		for( Favoris f : bookmarkListGroup)
		    			{
	    					serializedBookmarkList.add(FavorisManager.SerializeBookmarkHeader(context, f));    	
		    			}
		        		int nbUsed = serializedBookmarkList.size();
		    			for( FavorisNode fn : m_List)
		    			{
		    				if(fn.getType() == FavorisType.Favoris)
		    				{
		    					Favoris f = (Favoris) fn;
		    					serializedBookmarkList.add(FavorisManager.SerializeBookmarkHeader(context, f));	
		    				}
		    			}
		    			b.putString(TypeString.GROUPNAME, group.obtenirNom());
		    			b.putInt(TypeString.COLOR, group.obtenirCouleur());		    			
		    			b.putInt(TypeString.SELECTEDBOOKMARKS, nbUsed);
		    			b.putStringArrayList(TypeString.BOOKMARKS, serializedBookmarkList);
		    			b.putInt(TypeString.CURRENTPOSITION, groupPosition);
		    			b.putBoolean(TypeString.EDIT, true);
		    			Intent iCreateGroup = new Intent(context, CreateGroupDlg.class);
		    			iCreateGroup.putExtras(b);			
		    			startActivityForResult(iCreateGroup, 1);
		    			
		        		break;
		        	case 1:  // Delete group and bookmarks
			           {
			        	   m_List.remove(group);
			        	   updateAndSaveList();
			           }
		        		break;
		        	case 2: //Delete group only
		        	{
		        		 m_List.addAll(group.obtenirFavoris());
			        	   m_List.remove(group);	
			        	   updateAndSaveList();
		        	}
		        	break;		        	
		         
        		case 3: //Go Up
        			
        				if(groupPosition > 0)
        					Collections.swap(m_List, groupPosition, groupPosition-1);
        			
        			updateAndSaveList();
        			break;
        			
        		case 4: //Go Down        			
        				if(groupPosition < (m_List.size()-1))
        					Collections.swap(m_List, groupPosition, groupPosition+1);
        			
        			updateAndSaveList();
        			break;
		        	}
		        } 
			    });

			    AlertDialog dlg = builder.create();
			   dlg.show();
		}
		else if(fn.getType() == FavorisType.Favoris)
		{
			final boolean partOfGroup = inGroup;
			final Favoris f = (Favoris) fn;
			
			ArrayList<String> list = new ArrayList<String>(Arrays.asList(getResources().getString(R.string.CreateGroupDlg_AllerHoraire), getResources().getString(R.string.CreateGroupDlg_Supprimer) ,getResources().getString(R.string.CreateGroupDlg_Monter), getResources().getString(R.string.CreateGroupDlg_Descendre)));
			if(inGroup)
				list.add(getResources().getString(R.string.CreateGroupDlg_EnleverDuGroupe));
			
			final CharSequence[] options = list.toArray(new CharSequence[list.size()]);
			
				
		    AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setTitle(getResources().getString(R.string.CreateGroupDlg_ModifierFavoris));
		    builder.setItems(options, new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which)
		        {
		        	switch(which)
		        	{
		        		case 0: //Go to schedule
		        			GoToSchedule(f);		
		        			break;
		        		/*case 1: //Rename
		        			//TODO:
		        			updateAndSaveList();
		        			break;*/
		        			
		        		case 1: //Delete
		        			if(partOfGroup)
		        			{
		        				 FavorisGroupe g = (FavorisGroupe) fg;
		        				 ArrayList<Favoris> list =  g.obtenirFavoris();
		        				 list.remove(childPosition);
		        				 g.definirListeDeFavoris(list);
		        			}
		        			else
		        			{
		        				m_List.remove(groupPosition);
		        			}
		        			updateAndSaveList();
		        			
		        			break;
		        		case 2: //Go Up
		        			if(partOfGroup)
		        			{
		        				if(childPosition > 0)
		        				{
		        					 FavorisGroupe g = (FavorisGroupe) fg;
			        				 ArrayList<Favoris> list =  g.obtenirFavoris();
			        				 
			        				 Collections.swap(list, childPosition, childPosition-1);
			        				 g.definirListeDeFavoris(list);
		        				}

		        			}
		        			else
		        			{
		        				if(groupPosition > 0)
		        					Collections.swap(m_List, groupPosition, groupPosition-1);
		        			}
		        			updateAndSaveList();
		        			break;
		        			
		        		case 3: //Go Down
		        			if(partOfGroup)
		        			{
		        				FavorisGroupe g = (FavorisGroupe) fg;
		        				 ArrayList<Favoris> list =  g.obtenirFavoris();
		        				if(childPosition < (list.size()-1))
		        				{		        					 
		        					Collections.swap(list, childPosition, childPosition+1);
		        					g.definirListeDeFavoris(list);
		        				}    
		        			}
		        			else
		        			{
		        				if(groupPosition < (m_List.size()-1))
		        					Collections.swap(m_List, groupPosition, groupPosition+1);
		        			}
		        			updateAndSaveList();
		        			break;
		        		case 4: //Remove from group
		        			if(partOfGroup)
		        			{
		        				m_List.add(f);
		        				FavorisGroupe g = (FavorisGroupe) fg;
		        				 ArrayList<Favoris> list =  g.obtenirFavoris();		        				 
		        				 list.remove(childPosition);
		        				 updateAndSaveList();
		        			}
		        			break;
		        	
		        	}
		        	

		        } 
			    });

			    AlertDialog dlg = builder.create();
			   dlg.show();
		}
		// TODO Auto-generated method stub
		return false;
	}

	
	 private long[] expandedIds;

	    @Override
	    protected void onStart() {
	        super.onStart();
	        if (this.expandedIds != null) {
	            restoreExpandedState(expandedIds);
	        }
	    }

	    @Override
	    protected void onStop() {
	        super.onStop();
	        expandedIds = getExpandedIds();
	    }

	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        this.expandedIds = getExpandedIds();
	        outState.putLongArray("ExpandedIds", this.expandedIds);
	    }

	    @Override
	    protected void onRestoreInstanceState(Bundle state) {
	        super.onRestoreInstanceState(state);
	        long[] expandedIds = state.getLongArray("ExpandedIds");
	        if (expandedIds != null) {
	            restoreExpandedState(expandedIds);
	        }
	    }

	    private long[] getExpandedIds() 
	    {
	        if (m_ItemAdapter != null) 
	        {
	            int length = m_ItemAdapter.getGroupCount();
	            ArrayList<Long> expandedIds = new ArrayList<Long>();
	            for(int i=0; i < length; i++) 
	            {
	                if(m_ListeView.isGroupExpanded(i)) 
	                {
	                    expandedIds.add(m_ItemAdapter.getGroupId(i));
	                }
	            }
	            return toLongArray(expandedIds);
	        } 
	        else 
	        {
	            return null;
	        }
	    }

	    private void restoreExpandedState(long[] expandedIds) 
	    {
	        this.expandedIds = expandedIds;
	        if (expandedIds != null) 
	        {
	            if (m_ItemAdapter != null) {
	                for (int i=0; i<m_ItemAdapter.getGroupCount(); i++) 
	                {
	                    long id = m_ItemAdapter.getGroupId(i);
	                    if (inArray(expandedIds, id)) 
	                    	m_ListeView.expandGroup(i);
	                }
	            }
	        }
	    }

	    private static boolean inArray(long[] array, long element) {
	        for (long l : array) {
	            if (l == element) {
	                return true;
	            }
	        }
	        return false;
	    }

	    private static long[] toLongArray(List<Long> list)  {
	        long[] ret = new long[list.size()];
	        int i = 0;
	        for (Long e : list)  
	            ret[i++] = e.longValue();
	        return ret;
	    }







}