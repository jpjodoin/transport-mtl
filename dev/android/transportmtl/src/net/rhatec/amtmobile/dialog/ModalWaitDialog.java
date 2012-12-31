package net.rhatec.amtmobile.dialog;

import net.rhatec.amtmobile.tasks.Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class ModalWaitDialog 
{
	ProgressDialog						m_Dialog;
	int									m_StateProgress;
	final Handler	mHandler		= new Handler();
	Task m_task;
	final Runnable	mUpdateResults	= new Runnable() 
	{
		@Override
		public void run()
		{
			updateResultsInUi();
		}
	};
	
	public ModalWaitDialog()
	{

	}

	private void updateResultsInUi()
	{
		if (m_StateProgress == 0)
			m_Dialog.show();
		else
			m_Dialog.dismiss();
	}

	
	public void runTask(Context c, String message, Task t)
	{
		m_Dialog = ProgressDialog.show(c, "", message, true);
		m_task = t;
		executeTask();
	}
	
	private void executeTask()
	{
		Thread runningThread = new Thread() {
			@Override
			public void run()
			{
				m_StateProgress = 0;
				mHandler.post(mUpdateResults);
				m_task.execute();
				m_task = null;
				m_StateProgress = 1;
				mHandler.post(mUpdateResults);
			}

		};
		runningThread.start();
	}

}
