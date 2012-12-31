package net.rhatec.amtmobile.dialog;


import java.util.ArrayList;
import java.util.HashMap;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.constants.TypeString;
import net.rhatec.amtmobile.manager.FavorisManager;
import net.rhatec.amtmobile.view.FavorisGroupEditAdapter;
import net.rhatec.amtmobile.view.FavorisGroupEditView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class CreateGroupDlg extends Activity implements OnClickListener, OnItemClickListener
{
	int mReturnCode = 1;
	int mCurrentPosition = 0;
	Button mColorBtn;
	ListView mBookmarkListView;
	String mColor = "#5794D5";
	EditText mEditTxt;
	ArrayList<HashMap<String, String>> mBookMarkList;
	FavorisGroupEditAdapter mListAdapter;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		//Todo: Réception par le bundle du titre, couleur et des favoris sélectionné
		super.onCreate(savedInstanceState);
		
		Bundle b = this.getIntent().getExtras();	
		
		setContentView(R.layout.creategroup);
		this.setTitle(this.getResources().getString(R.string.CreateGroupDlg_title));

		mColorBtn = (Button) findViewById(R.id.bColor);
		mColorBtn.setOnClickListener(this);
		
		Button okBtn = (Button) findViewById(R.id.bAccept);
		okBtn.setOnClickListener(this);
		
		Button cancelBtn = (Button) findViewById(R.id.bCancel);
		cancelBtn.setOnClickListener(this);
		
		mBookmarkListView = (ListView) findViewById(R.id.bookmarkList);
		mEditTxt = (EditText) findViewById(R.id.eName);
		TextView emptyText = (TextView)findViewById(android.R.id.empty);
		mBookmarkListView.setEmptyView(emptyText);
		
		//Default color
		
		int nbSelectedBookmarks = 0;

		boolean edit = b.getBoolean( TypeString.EDIT, false);
		mReturnCode = edit ? 2 : 1;
		if(edit)
		{
			mReturnCode = 2;
			mEditTxt.setText(b.getString(TypeString.GROUPNAME));
			mColor = String.format("#%X", b.getInt(TypeString.COLOR));
			nbSelectedBookmarks = b.getInt(TypeString.SELECTEDBOOKMARKS);
			mCurrentPosition = b.getInt(TypeString.CURRENTPOSITION);
		}
		
		int currentColor = Color.parseColor(mColor);
		mColorBtn.setBackgroundColor(currentColor);
		
		
		
		ArrayList<String> bookmarkListSerialized = b.getStringArrayList(TypeString.BOOKMARKS);
		mBookMarkList = new ArrayList<HashMap<String, String>>(bookmarkListSerialized.size());
		for(String bookmarkStr : bookmarkListSerialized)
		{
			HashMap<String, String> item  = FavorisManager.UnserializeBookmarkHeader(bookmarkStr);
			mBookMarkList.add(item);
		}
			
		FavorisGroupEditAdapter mListAdapter = new FavorisGroupEditAdapter(this, mBookMarkList);
		mBookmarkListView.setAdapter(mListAdapter);
		mBookmarkListView.setOnItemClickListener(this);
		mBookmarkListView.setItemsCanFocus(true);
		mBookmarkListView.setAnimationCacheEnabled(false);
		mBookmarkListView.setScrollingCacheEnabled(false);		
		
		for(int i = 0; i < nbSelectedBookmarks; ++i)
		{
			FavorisGroupEditView v1 = (FavorisGroupEditView) mBookmarkListView.getItemAtPosition(i);
			v1.setChecked(true);
		}
	}
	
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.bColor:
			int currentColor = Color.parseColor(mColor);
			AmbilWarnaDialog dialog = new AmbilWarnaDialog(CreateGroupDlg.this, currentColor, new OnAmbilWarnaListener()
			{
			@Override
			public void onCancel(AmbilWarnaDialog dialog) {
			}

			@Override
			public void onOk(AmbilWarnaDialog dialog, int color) 
			{
				mColorBtn.setBackgroundColor(color);
				mColor = String.format("#%X", color);
			}
			});
			dialog.show();
			break;
		
		case R.id.bCancel:
			setResult(0);
			finish();
			break;
			
		case R.id.bAccept:
			Intent intent = this.getIntent();
			String groupName = mEditTxt.getText().toString();
			intent.putExtra(TypeString.GROUPNAME, groupName);
			intent.putExtra(TypeString.COLOR, mColor);
			intent.putExtra(TypeString.CURRENTPOSITION, mCurrentPosition);
			ArrayList<Integer> selectedBookmarks = new ArrayList<Integer>();
			int itemCount = mBookmarkListView.getCount();
			
			for(int i = 0; i < itemCount; ++i)
			{
				FavorisGroupEditView v1 = (FavorisGroupEditView) mBookmarkListView.getItemAtPosition(i);
				if(v1.isChecked())
					selectedBookmarks.add(i);
			}
			intent.putIntegerArrayListExtra(TypeString.SELECTEDBOOKMARKS, selectedBookmarks);
			setResult(mReturnCode, intent); //Create
			finish();
			//Return information in bundle
			break;
		}

	}


	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
	{
		FavorisGroupEditView v1 = (FavorisGroupEditView) mBookmarkListView.getItemAtPosition(position);
		v1.setChecked(!v1.isChecked());
		//mListAdapter.notifyDataSetChanged();
		parent.invalidate();

		//mBookmarkListView.invalidateViews();
		//mListAdapter.notifyDataSetChanged();
		//
		// TODO Auto-generated method stub
		
	}

}
