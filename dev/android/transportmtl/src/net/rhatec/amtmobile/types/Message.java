package net.rhatec.amtmobile.types;

public final class Message
{

	public final boolean	status;
	public final int		message;

	public Message(boolean status, int message)
	{
		this.status = status;
		this.message = message;
	}

	public boolean getStatus()
	{
		return status;
	}

	public int getMessage()
	{
		return message;
	}
}
