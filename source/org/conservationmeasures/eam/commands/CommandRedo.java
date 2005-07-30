/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;

public class CommandRedo extends Command
{
	public CommandRedo()
	{
	}

	public CommandRedo(DataInputStream dataIn)
	{
		
	}

	public boolean isRedo()
	{
		return true;
	}

	public String toString()
	{
		return getCommandName();
	}
	
	public static String getCommandName()
	{
		return "Redo";
	}
	
	public void execute(BaseProject target) throws CommandFailedException
	{
		target.redo();
	}

	public void writeTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getCommandName());
	}

}
