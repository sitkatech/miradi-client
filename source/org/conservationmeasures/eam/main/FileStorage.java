/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.exceptions.UnknownCommandException;

public class FileStorage extends Storage
{
	public FileStorage()
	{
	}
	
	public boolean hasFile()
	{
		return (file != null);
	}
	
	public String getName()
	{
		return file.getName();
	}
	
	public void setFile(File fileToUse)
	{
		clear();
		file = fileToUse;
	}
	
	public Vector load() throws IOException, UnknownCommandException
	{
		FileInputStream in = new FileInputStream(file);
		try
		{
			return load(in);
		}
		finally
		{
			in.close();
		}
	}

	public boolean exists()
	{
		return (file.exists());
	}
	
	public void createEmpty() throws FileNotFoundException, IOException
	{
		FileOutputStream out = new FileOutputStream(file);
		out.close();
	}

	public void appendCommand(Command command) throws IOException
	{
		if(!hasFile())
			throw new IOException("FileStorage: Can't append if no file open");
		
		FileOutputStream out = new FileOutputStream(file, true);
		try
		{
			appendCommand(out, command);
		}
		finally
		{
			out.close();
		}
	}
	
	File file;
}
