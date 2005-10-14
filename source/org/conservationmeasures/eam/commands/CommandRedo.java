/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.project.Project;

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
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		target.redo();
	}

	public void undo(Project target) throws CommandFailedException
	{
	}
	
	public static final String COMMAND_NAME = "Redo";

}
