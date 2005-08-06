/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;

public class CommandDoNothing extends Command
{
	
	public String toString()
	{
		return getCommandName() + ":" + done;
	}
	
	public static String getCommandName()
	{
		return "DoNothing";
	}
	
	
	public void execute(BaseProject target) throws CommandFailedException
	{
		++done;
	}

	public void undo(BaseProject target) throws CommandFailedException
	{
		--done;
	}

	public void writeTo(DataOutputStream dataOut) throws IOException
	{
	}
	
	public int done;
}