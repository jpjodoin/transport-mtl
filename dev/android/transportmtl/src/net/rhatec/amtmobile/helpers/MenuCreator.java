package net.rhatec.amtmobile.helpers;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.dialog.FavorisDlg;
import net.rhatec.amtmobile.dialog.MiseAJourDlg;
import net.rhatec.amtmobile.dialog.PreferenceDlg;
import net.rhatec.amtmobile.dialog.TransportSelectDlg;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MenuCreator
{
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	// menu item constants
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	public static final int	MenuPrincipale	= Menu.FIRST + 1;
	public static final int	Favoris			= Menu.FIRST + 2;
	public static final int	Parametre		= Menu.FIRST + 3;
	public static final int	MiseAJour		= Menu.FIRST + 4;

	/** create the menu items */
	public static int populateMenu(Menu menu, Context _context)
	{

		// enable keyb shortcuts, qwerty mode = true means only show keyb
		// shortcuts (not numeric) and vice versa
		// these only show up in context menu, not options menu
		menu.setQwertyMode(true);
		// MenuCreator_Accueil

		MenuItem item1 = menu.add(Menu.NONE, MenuPrincipale, Menu.NONE, _context.getResources().getString(R.string.MenuCreator_Accueil));
		MenuItem item2 = menu.add(Menu.NONE, Favoris, Menu.NONE, _context.getResources().getString(R.string.MenuCreator_Favoris));
		MenuItem item3 = menu.add(Menu.NONE, Parametre, Menu.NONE, _context.getResources().getString(R.string.MenuCreator_Preferences));
		MenuItem item4 = menu.add(Menu.NONE, MiseAJour, Menu.NONE, _context.getResources().getString(R.string.MenuCreator_MAJ));
		item1.setAlphabeticShortcut('m');
		item1.setIcon(R.drawable.ic_menu_home);
		item2.setAlphabeticShortcut('f');
		item2.setIcon(R.drawable.ic_menu_star); // android.R.drawable.ic_menu_recent_history
		item3.setAlphabeticShortcut('p');
		item3.setIcon(R.drawable.ic_menu_preferences);
		item4.setAlphabeticShortcut('t');
		item4.setIcon(R.drawable.ic_menu_refresh);
		return MiseAJour; // On retourne la valeur du dernier item au cas o� on
							// voudrait rajouter d'autres options au menu

	}

	/** L'activit� � lanc� selon le bouton press� */
	public static void startActivityFromMenu(MenuItem item, Context toLaunchFrom)
	{
		switch (item.getItemId())
		{
		case MenuPrincipale:
			Intent iMenuPrincipale = new Intent(toLaunchFrom, TransportSelectDlg.class);
			toLaunchFrom.startActivity(iMenuPrincipale);
			break;

		case Favoris:
			Intent iFavoris = new Intent(toLaunchFrom, FavorisDlg.class);
			toLaunchFrom.startActivity(iFavoris);
			break;

		case Parametre:
			Intent iParametre = new Intent(toLaunchFrom, PreferenceDlg.class);
			toLaunchFrom.startActivity(iParametre);
			break;

		case MiseAJour:
			Intent iMiseAJour = new Intent(toLaunchFrom, MiseAJourDlg.class);
			toLaunchFrom.startActivity(iMiseAJour);
			break;

		}

	}

}
