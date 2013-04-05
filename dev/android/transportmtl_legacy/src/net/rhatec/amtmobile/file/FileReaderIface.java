package net.rhatec.amtmobile.file;

import java.io.IOException;

public interface FileReaderIface
{
	public String readLine() throws IOException;

	public void close() throws IOException;

	public long getBytePosition();

}
