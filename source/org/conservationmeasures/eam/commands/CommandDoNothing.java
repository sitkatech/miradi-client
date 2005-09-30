/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;

public class CommandDoNothing extends Command
{
	
	public String toString()
	{
		return getCommandName() + ":" + done;
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(BaseProject target) throws CommandFailedException
	{
		++done;
	}

	public void undo(BaseProject target) throws CommandFailedException
	{
		--done;
	}

	public static final String COMMAND_NAME = "DoNothing";

	public int done;
}