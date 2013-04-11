package net.rhatec.amtmobile.dialog;
import android.os.Bundle;
import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.baseactivity.ActivityWithMenu;


public class MapsDlg extends ActivityWithMenu 
{
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.mapdlg);
	    }
}
