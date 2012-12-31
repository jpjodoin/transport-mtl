package net.rhatec.amtmobile.tasks;

import net.rhatec.amtmobile.types.FileManager;

public class DeleteFolderTask implements Task 
{

	String m_name;
	
	public DeleteFolderTask(String folderName)
	{
		m_name = folderName;
	}
	
	
	@Override
	public void execute() 
	{
		FileManager.deleteDir(m_name);
	}

}
