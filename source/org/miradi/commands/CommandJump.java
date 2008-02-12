/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.commands;

import java.util.HashMap;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.project.Project;

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
	
	public Command getReverseCommand() throws CommandFailedException
	{
		return new CommandJump(previousStep);
	}

	public HashMap getLogData()
	{
		HashMap dataPairs = new HashMap();
		dataPairs.put("STEP", new Integer(step));
		return dataPairs;
	}
	
	public static final String COMMAND_NAME = "Jump";
	
	int step;
	int previousStep;
}
