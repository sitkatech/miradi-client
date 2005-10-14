/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.project.Project;

public class CommandUndo extends Command
{
	public CommandUndo()
	{
	}
	
	public CommandUndo(DataInputStream dataIn)
	{
		
	}

	public boolean isUndo()
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
		target.undo();
	}

	public void undo(Project target) throws CommandFailedException
	{
	}

	public static final String COMMAND_NAME = "Undo";

}
