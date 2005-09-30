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
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.database.Database;
import org.conservationmeasures.eam.exceptions.UnknownCommandException;
import org.martus.util.DirectoryUtils;

public class FileStorage extends Storage
{
	public FileStorage() throws IOException
	{
		db = new Database();
	}
	
	public void close() throws IOException
	{
		db.close();
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
		db.openDiskDatabase(getDatabaseFileBase());
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
	
	public void createEmpty() throws IOException
	{
		FileOutputStream out = new FileOutputStream(file);
		out.close();
		DirectoryUtils.deleteEntireDirectoryTree(getDatabaseDirectory());
		db.openDiskDatabase(getDatabaseFileBase());
		createCommandsTable();
		db.close();
	}

	private File getDatabaseFileBase()
	{
		File directory = getDatabaseDirectory();
		File dbBase = new File(directory, file.getName());
		return dbBase;
	}

	private File getDatabaseDirectory()
	{
		File directory = new File(file.getAbsolutePath() + ".db");
		directory.mkdirs();
		return directory;
	}
	
	private void createCommandsTable() throws IOException
	{
		db.rawExecute("CREATE TABLE commands (id INTEGER IDENTITY PRIMARY KEY, name VARCHAR, data LONGVARBINARY);");
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
	Database db;
}
