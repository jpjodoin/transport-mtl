package net.rhatec.amtmobile.baseactivity;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;

public class GestureView extends View implements OnGestureListener
{

	public GestureView(Context context)
	{
		super(context);
	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	{
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		return false;
	}

}
