package net.rhatec.amtmobile.baseactivity;

import net.rhatec.amtmobile.helpers.MenuCreator;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ListActivityWithMenu extends ListActivity
{
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	// Cr�ations des boutons et des menus par d�faut
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

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
