package net.rhatec.amtmobile.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class BufferedFileReader implements FileReaderIface
{
	BufferedReader	m_reader;
	int				m_BytePosition	= 0;

	public BufferedFileReader(String _fileName) throws UnsupportedEncodingException, FileNotFoundException
	{
		m_reader = new BufferedReader(new InputStreamReader(new FileInputStream(_fileName), "8859_1"), 2048); //  //TODO Adjust size for default
	}

	public BufferedFileReader(String _fileName, int _size) throws UnsupportedEncodingException, FileNotFoundException
	{
		m_reader = new BufferedReader(new InputStreamReader(new FileInputStream(_fileName), "8859_1"), _size);
	}

	@Override
	public String readLine() throws IOException
	{
		String line = m_reader.readLine();
		m_BytePosition += line != null ? line.length() + 2 : m_BytePosition;
		return line;

	}

	@Override
	public void close() throws IOException
	{
		m_reader.close();
	}

	@Override
	public long getBytePosition()
	{
		return m_BytePosition;
	}

}
