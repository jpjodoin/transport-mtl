package net.rhatec.amtmobile.dialog;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.baseactivity.ActivityWithMenu;
import net.rhatec.amtmobile.constants.TypeString;
import net.rhatec.amtmobile.ui.ZoomablePictureWidget;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ZoomControls;

public class CarteViewerDlg extends ActivityWithMenu {
	// http://vinnysoft.blogspot.com/2009/08/zooming-imageview.html
	// http://groups.google.com/group/android-developers/browse_thread/thread/13fafedb71ccc296/0c0bc0830a20ee82

	ZoomControls m_zoomControls;
	ZoomablePictureWidget m_zoomablePictureWidget = null;
	String m_TransportName;
	int m_resid;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle b = this.getIntent().getExtras();
		m_TransportName = b.getString(TypeString.SOCIETECODE);
		m_resid = getResources().getIdentifier(m_TransportName + "carte",
				"drawable", "net.rhatec.amtmobile");
		if (m_resid != 0) {

			m_zoomablePictureWidget = new ZoomablePictureWidget(this, m_resid);
			setContentView(R.layout.zoomviewdlg);
			m_zoomControls = (ZoomControls) findViewById(R.id.ZoomControls01);
			m_zoomControls.setZoomSpeed(5);
			m_zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					m_zoomablePictureWidget.zoomIn();
				}
			});

			m_zoomControls
					.setOnZoomOutClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							m_zoomablePictureWidget.zoomOut();
						}
					});
			FrameLayout fLayout = (FrameLayout) findViewById(R.id.FramePicture);
			fLayout.addView(m_zoomablePictureWidget);

		} else
			finish();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		m_zoomablePictureWidget.resetRatio();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (m_zoomablePictureWidget != null) {
			m_zoomablePictureWidget.finish();
			m_zoomablePictureWidget = null;
		}
	}

}
