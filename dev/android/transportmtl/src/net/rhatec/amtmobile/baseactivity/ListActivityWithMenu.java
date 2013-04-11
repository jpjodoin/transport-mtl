package net.rhatec.amtmobile.baseactivity;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.SherlockListActivity;

import net.rhatec.amtmobile.helpers.MenuCreator;
import android.os.Bundle;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class ListActivityWithMenu extends SherlockListActivity
{
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	// Cr�ations des boutons et des menus par d�faut
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock);		
		getSupportActionBar().setDisplayShowTitleEnabled(true);	
		getSupportActionBar().setDisplayShowHomeEnabled(false);
	}
	
	
	/** hook into menu button for activity */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuCreator.populateMenu(menu, this.getBaseContext());
		removeItem(menu);
		return super.onCreateOptionsMenu(menu);
	}

	/** when menu button option selected */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		MenuCreator.startActivityFromMenu(item, this);
		return super.onOptionsItemSelected(item);
	}
	
	public void removeItem(Menu m)
	{

	}

}
