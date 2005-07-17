/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;

public class FileStorage
{
	public FileStorage()
	{
		commands = new Vector();
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
		commands.clear();
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
	
	public int getCommandCount()
	{
		return commands.size();
	}
	
	public Command getCommand(int i)
	{
		if(i < 0 || i >= getCommandCount())
			throw new RuntimeException("Command " + i + " not found in file: " + file.getAbsolutePath());
		
		return (Command)commands.get(i);
	}
	
	public void appendCommand(Command command) throws IOException
	{
		if(!hasFile())
			throw new IOException("FileStorage: Can't append if no file open");
		
		FileOutputStream out = new FileOutputStream(file, true);
		try
		{
			appendCommand(out, command);
			commands.add(command);
		}
		finally
		{
			out.close();
		}
		
	}
	
	
	

	private void appendCommand(FileOutputStream out, Command command) throws IOException
	{
		DataOutputStream dataOut = new DataOutputStream(out);
		try
		{
			appendCommand(dataOut, command);
		}
		finally
		{
			dataOut.close();
		}
	}

	private void appendCommand(DataOutputStream dataOut, Command command) throws IOException
	{
		dataOut.write(0);
		command.writeTo(dataOut);
		EAM.logDebug("wrote: " + command.toString());
	}
	

	
	private void load(FileInputStream in) throws IOException
	{
		DataInputStream dataIn = new DataInputStream(in);
		try
		{
			load(dataIn);
		}
		finally
		{
			dataIn.close();
		}
	}

	private void load(DataInputStream dataIn) throws IOException
	{
		EAM.logDebug("---Loading---");
		while(true)
		{
			if(dataIn.read() < 0)
				break;
			Command command = Command.readFrom(dataIn);
			commands.add(command);
			EAM.logDebug(command.toString());
		}
		EAM.logDebug("---Finished---");
	}
	
	File file;
	Vector commands;
}
