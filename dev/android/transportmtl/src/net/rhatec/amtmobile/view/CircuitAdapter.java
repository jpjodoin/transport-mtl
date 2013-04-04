package net.rhatec.amtmobile.view;

import java.util.HashMap;
import java.util.List;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class CircuitAdapter extends SimpleAdapter {

	//0xFF prepend
	//first byte == alpha
	private int[] mColors; //= new int[] { 0xFF000000, 0xFFD95700 };
	//private boolean mBigFingerMode;
	//private int minHeigthSp;
	public CircuitAdapter(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to, int[] colors/*, boolean bigFinger*/) 
	{		
		super(context, items, resource, from, to);
		mColors = colors;
		//mBigFingerMode = bigFinger;
		//minHeigthSp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 48, context.getResources().getDisplayMetrics());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	  View view = super.getView(position, convertView, parent);
	  view.setBackgroundColor(mColors[position]+0xFF000000);
	 /* if(mBigFingerMode)
		  view.setMinimumHeight(minHeigthSp);*/

	  

	  return view;
	}

}
