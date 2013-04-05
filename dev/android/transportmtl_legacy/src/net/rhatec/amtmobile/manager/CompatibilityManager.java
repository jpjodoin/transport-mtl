package net.rhatec.amtmobile.manager;

public class CompatibilityManager 
{
	public static boolean isBB10()
	{
		return System.getProperty("os.name").equals("qnx");
	}

}
