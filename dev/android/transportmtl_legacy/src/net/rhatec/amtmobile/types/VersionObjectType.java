package net.rhatec.amtmobile.types;

public enum VersionObjectType 
{
	DATABASE_VERSION ("DB_V", 1),
	BOOKMARKS_VERSION ("BK_V", 1);
	
	private final String m_keyName;
	private final int m_currentVersion;
	VersionObjectType(String strRepresentation, int currentVersion)
	{
		this.m_keyName = strRepresentation;
		this.m_currentVersion = currentVersion;
	}
	
	public String getKeyName()
	{
		return m_keyName;
	}
	
	public int getCurrentAppVersion()
	{
		return m_currentVersion;
	}
	
	
}
