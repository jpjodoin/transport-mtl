package net.rhatec.amtmobile.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FavorisGroupEditAdapter extends BaseAdapter 
{

	ArrayList<FavorisGroupEditView>  mList = new ArrayList<FavorisGroupEditView>();
	Context mContext;
	public FavorisGroupEditAdapter(Context _context, ArrayList<HashMap<String, String>> _list) 
	{
		mContext = _context;
		for(HashMap<String,String> element: _list)
		{
			mList.add(new FavorisGroupEditView(mContext, element));
		}
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public FavorisGroupEditView getItem(int _position) 
	{
		return mList.get(_position);
	}

	@Override
	public long getItemId(int _position) {
		return _position;
	}

	@Override
	public View getView(int _position, View _convertView, ViewGroup _parent) 
	{
		
		/*FavorisGroupEditView v = (FavorisGroupEditView)mList.get(_position);
		v.mCheckBox.setChecked(v.isChecked());
		return v;*/
		return mList.get(_position);
	}

}
