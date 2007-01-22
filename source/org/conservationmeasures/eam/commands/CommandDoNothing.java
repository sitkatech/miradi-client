/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.project.Project;

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

	public void execute(Project target) throws CommandFailedException
	{
		++done;
	}

	public void undo(Project target) throws CommandFailedException
	{
		--done;
	}

	public static final String COMMAND_NAME = "DoNothing";

	public int done;
}