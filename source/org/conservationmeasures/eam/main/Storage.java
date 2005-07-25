/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;

public class Storage
{
	public Storage()
	{
		commands = new Vector();
	}

	public int getCommandCount()
	{
		return commands.size();
	}

	public Command getCommand(int i)
	{
		if(i < 0 || i >= getCommandCount())
			throw new RuntimeException("Command " + i + " not found");
		
		return (Command)commands.get(i);
	}

	protected void clear()
	{
		commands.clear();
	}
	
	protected void load(InputStream in) throws IOException
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

	protected void load(DataInputStream dataIn) throws IOException
	{
		EAM.logDebug("---Loading---");
		while(true)
		{
			if(dataIn.read() < 0)
				break;
			Command command = Command.readFrom(dataIn);
			addCommand(command);
			EAM.logDebug(command.toString());
		}
		EAM.logDebug("---Finished---");
	}

	protected void appendCommand(OutputStream out, Command command) throws IOException
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
		addCommand(command);
	}
	
	public void appendCommand(Command command) throws IOException
	{
		addCommand(command);
	}

	private void addCommand(Command command) throws IOException
	{
		commands.add(command);
	}

	protected Vector commands;

}
