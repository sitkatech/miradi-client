/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;

public class CommandUndo extends Command
{
	public CommandUndo()
	{
	}
	
	public CommandUndo(DataInputStream dataIn)
	{
		
	}

	public String toString()
	{
		return getCommandName();
	}
	
	public static String getCommandName()
	{
		return "Undo";
	}
	
	public void execute(BaseProject target) throws CommandFailedException
	{
		try
		{
			target.undo();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException();
		}
	}

	public void writeTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getCommandName());
	}
}
