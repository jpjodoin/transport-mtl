package net.rhatec.amtmobile.ui;

import net.rhatec.amtmobile.baseactivity.GestureView;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

public class ZoomablePictureWidget extends GestureView
{
	private GestureDetector	m_gestureScanner;
	private Drawable		m_image;

	private int				m_zoomController;
	private int				m_offsetX	= 0;
	private int				m_offsetY	= 0;

	private int				m_zoomIncrement;
	private int				m_minimalZoom;
	private int				m_maximalZoom;
	private double			m_pictureRatio;

	private int				m_left;
	private int				m_top;
	private int				m_right;
	private int				m_bottom;
	private boolean			m_firstTime	= true;
	private Context			m_context;

	public ZoomablePictureWidget(Context _context, int _ressourceId)
	{
		super(_context);
		m_context = _context;
		m_gestureScanner = new GestureDetector(this);

			//System.gc(); //Big memory allocation...
			m_image = _context.getResources().getDrawable(_ressourceId);

			int pictureHeight = m_image.getIntrinsicHeight();
			int pictureWidth = m_image.getIntrinsicWidth();
			

			m_pictureRatio = (double) pictureWidth / (double) pictureHeight;
			setFocusable(true);

			m_minimalZoom = 0;
			m_zoomController = m_minimalZoom;

		
	}
	//http://stackoverflow.com/questions/477572/android-strange-out-of-memory-issue/823966#823966
	//http://forum.frandroid.com/forum/viewtopic.php?id=6568
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if (m_firstTime)
		{
			Display display = ((WindowManager) m_context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			int displayWidth = display.getWidth();
			int displayHeight = display.getHeight();
			int tinyDisplaySide = displayHeight > displayWidth ? displayWidth : displayHeight; // Apparence normale
			m_zoomIncrement = tinyDisplaySide / 20;
			m_maximalZoom = tinyDisplaySide * 5;

			int width = getWidth();
			int height = getHeight();
			boolean landscape = width > height ? true : false;
			int zoomFactor = landscape ? (int) (height * m_pictureRatio * 0.5) : (int) (width / m_pictureRatio * 0.5);
			if (landscape)
			{
				m_left = width / 2 - zoomFactor;
				m_right = width / 2 + zoomFactor;
				m_top = 0;
				m_bottom = height;

			} else
			{
				m_left = 0;
				m_right = width;
				m_top = height / 2 - zoomFactor;
				m_bottom = height / 2 + zoomFactor;

			}
			m_firstTime = false;
		}
		int ratio = (int) (m_zoomController * m_pictureRatio);
		
		m_image.setBounds(m_left -  ratio - m_offsetX, m_top - m_zoomController - m_offsetY, m_right	+ratio - m_offsetX, 
				m_bottom + m_zoomController - m_offsetY);
		m_image.draw(canvas);
		
		
	}

	public void zoomIn()
	{
		if (m_zoomController + m_zoomIncrement > m_maximalZoom)
			m_zoomController = m_maximalZoom;
		else
			m_zoomController += m_zoomIncrement;

		invalidate();
	}

	public void zoomOut()
	{
		if (m_offsetX != 0 || m_offsetY != 0) // On veut renvoyer la carte
												// tranquillement vers le centre
		{
			int step = (m_zoomController - m_minimalZoom) / m_zoomIncrement;
			if(step != 0) //Possible que cela arrive si la personne d�place la carte sans faire de zoom in et qu'elle zoom out ensuite...
			{
				m_offsetX -= m_offsetX / step;
				m_offsetY -= m_offsetY / step;				
			}

		}

		if (m_zoomController - m_zoomIncrement < m_minimalZoom) // Niveau
																// minimal de
																// zoom atteint
		{
			m_zoomController = m_minimalZoom;
			m_offsetX = 0;
			m_offsetY = 0;
		} else
			m_zoomController -= m_zoomIncrement;
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent me)
	{
		boolean success = false;
		if(m_gestureScanner != null) //A user had a null ptr exception here for some reason
			success = m_gestureScanner.onTouchEvent(me);
		
		return success;
	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		return true;
	}

	public void resetRatio()
	{
		m_firstTime = true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float _distanceX, float _distanceY)
	{
		m_offsetX += _distanceX;
		m_offsetY += _distanceY;
		invalidate();
		return true;
	}
	
	
	public void finish()
	{
		m_context = null;
		m_gestureScanner = null;
		m_image= null;
	
	}
	
}
