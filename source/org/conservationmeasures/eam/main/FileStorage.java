/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
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
		EAM.logDebug("Closing database");
		db.close();
	}
	
	public boolean hasFile()
	{
		return (directory != null);
	}
	
	public String getName()
	{
		return directory.getName();
	}
	
	public void setDirectory(File directoryToUse)
	{
		clear();
		directory = directoryToUse;
	}
	
	public Vector load() throws IOException, UnknownCommandException
	{
		db.openDiskDatabase(getDatabaseFileBase());
		Vector loaded = new Vector();
		EAM.logDebug("---Loading---");
		ResultSet allCommands = db.rawSelect("SELECT * FROM DoneCommands ORDER BY id");
		try
		{
			while(allCommands.next())
			{
				String name = allCommands.getString("name");
				byte[] data = allCommands.getBytes("data");
				DataInputStream dataIn = new DataInputStream(new ByteArrayInputStream(data));
				
				Command command = Command.createFrom(name, dataIn);
				loaded.add(command);
				EAM.logDebug(command.toString());
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return null;
		}
		EAM.logDebug("---Finished---");
		return loaded;
	}

	public boolean exists()
	{
		return (directory.exists());
	}
	
	public void createEmpty() throws IOException
	{
		DirectoryUtils.deleteEntireDirectoryTree(directory);
		db.openDiskDatabase(getDatabaseFileBase());
		createCommandsTable();
		db.close();
	}
	
	private File getDatabaseFileBase()
	{
		return new File(directory, directory.getName());
	}

	private void createCommandsTable() throws IOException
	{
		db.rawExecute("CREATE TABLE DoneCommands (id INTEGER IDENTITY PRIMARY KEY, name VARCHAR, data LONGVARBINARY);");
	}

	public void appendCommand(Command command) throws IOException
	{
		if(!hasFile())
			throw new IOException("FileStorage: Can't append if no file open");
		
		DoneCommand commandToSave = DoneCommand.buildFromCommand(command);
		db.insert(commandToSave);
		addCommandWithoutSaving(command);
	}
	
	File directory;
	Database db;
}
