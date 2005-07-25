/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.commands.Command;

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
	
	public void load(File fileToUse) throws IOException
	{
		clear();
		file = fileToUse;
		FileInputStream in = new FileInputStream(file);
		try
		{
			load(in);
		}
		finally
		{
			in.close();
		}
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
