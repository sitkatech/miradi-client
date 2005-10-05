/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.exceptions.UnknownCommandException;
import org.conservationmeasures.eam.main.DoneCommand;
import org.conservationmeasures.eam.main.EAM;
import org.martus.util.DirectoryUtils;

public class EAMDatabase
{
	public EAMDatabase() throws IOException
	{
		commands = new Vector();
		db = new DatabaseWrapper();
	}

	public int getCommandCount()
	{
		return commands.size();
	}

	public Command getCommandAt(int i)
	{
		if(i < 0 || i >= getCommandCount())
			throw new RuntimeException("Command " + i + " not found");
		
		return (Command)commands.get(i);
	}

	protected void clear()
	{
		commands.clear();
	}
	
	public void addCommandWithoutSaving(Command command) throws IOException
	{
		addCommand(command);
	}
	
	public void appendCommand(Command command) throws IOException
	{
		if(!isOpen())
			throw new IOException("FileStorage: Can't append if no file open");
		
		DoneCommand commandToSave = DoneCommand.buildFromCommand(command);
		db.insert(commandToSave);
		addCommandWithoutSaving(command);
	}

	public void close() throws IOException
	{
		EAM.logDebug("Closing database");
		clear();
		db.close();
		name = null;
	}

	public static boolean doesProjectExist(File directoryToCheck)
	{
		return isExistingProject(directoryToCheck);
	}
	
	public boolean isOpen()
	{
		return db.isOpen();
	}

	public String getName()
	{
		return name;
	}

	public void open(File directory) throws IOException
	{
		clear();
		if(!doesProjectExist(directory))
			createEmpty(directory);
		db.openDiskDatabase(getDatabaseFileBase(directory));
		name = directory.getName();
	}
	

	public void openMemoryDatabase(String nameToUse) throws IOException
	{
		clear();
		db.openMemoryDatabase(nameToUse);
		dropAllTables();
		createCommandsTable();
		name = nameToUse;
	}

	public Vector load() throws IOException, UnknownCommandException
	{
		Vector loaded = new Vector();
		EAM.logDebug("---Loading---");
		ResultSet allCommands = db.rawSelect("SELECT * FROM DoneCommands ORDER BY id");
		try
		{
			while(allCommands.next())
			{
				String commandName = allCommands.getString("name");
				byte[] data = allCommands.getBytes("data");
				DataInputStream dataIn = new DataInputStream(new ByteArrayInputStream(data));
				
				Command command = Command.createFrom(commandName, dataIn);
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
	
	private void createEmpty(File directory) throws IOException
	{
		DirectoryUtils.deleteEntireDirectoryTree(directory);
		db.openDiskDatabase(getDatabaseFileBase(directory));
		createCommandsTable();
		db.close();
	}

	public static boolean isExistingProject(File projectDirectory)
	{
		if(projectDirectory == null)
			return false;
		
		if(!projectDirectory.isDirectory())
			return false;
		
		String projectName = projectDirectory.getName();
		File script = new File(projectDirectory, projectName + ".script");
		return script.exists();
	}

	private static File getDatabaseFileBase(File directory)
	{
		return new File(directory, directory.getName());
	}

	private void createCommandsTable() throws IOException
	{
		db.rawExecute("CREATE TABLE DoneCommands (id INTEGER IDENTITY PRIMARY KEY, name VARCHAR, data LONGVARBINARY);");
	}

	public void dropAllTables() throws IOException
	{
		db.rawExecute("DROP TABLE DoneCommands IF EXISTS;");
	}

	private void addCommand(Command command)
	{
		commands.add(command);
	}

	protected Vector commands;
	String name;
	protected DatabaseWrapper db;

}
