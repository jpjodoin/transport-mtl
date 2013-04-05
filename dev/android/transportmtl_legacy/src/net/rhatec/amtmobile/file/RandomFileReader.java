package net.rhatec.amtmobile.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

public class RandomFileReader implements FileReaderIface
{
	RandomAccessFile	m_reader;

	public RandomFileReader(String _fileName) throws UnsupportedEncodingException, FileNotFoundException
	{
		m_reader = new RandomAccessFile(_fileName, "r");
	}

	@Override
	public String readLine() throws IOException
	{
		return m_reader.readLine();
	}

	public void seek(long _pos) throws IOException
	{
		m_reader.seek(_pos);
	}

	@Override
	public void close() throws IOException
	{
		m_reader.close();
	}

	@Override
	public long getBytePosition()
	{
		long nByte;
		try
		{
			nByte = m_reader.getFilePointer();
		} catch (IOException e)
		{
			nByte = 0;
			e.printStackTrace();
		}
		return nByte;
	}

}
