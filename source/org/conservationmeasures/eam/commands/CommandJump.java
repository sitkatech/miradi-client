/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.project.Project;

public class CommandJump extends Command
{
	public CommandJump(int destinationStep)
	{
		step = destinationStep;
		previousStep = -1;
	}

	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
	}

	public void undo(Project target) throws CommandFailedException
	{
	}

	public static final String COMMAND_NAME = "Jump";
	
	int step;
	int previousStep;
}
