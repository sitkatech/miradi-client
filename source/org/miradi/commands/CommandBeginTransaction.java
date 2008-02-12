/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.commands;

import java.util.HashMap;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.project.Project;

public class CommandBeginTransaction extends Command 
{
	public CommandBeginTransaction() 
	{
		super();
	}
	
	public void execute(Project target) throws CommandFailedException 
	{
		target.beginTransaction();
	}

	public Command getReverseCommand() throws CommandFailedException
	{
		return new CommandEndTransaction();
	}

	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public String toString()
	{
		return getCommandName();
	}

	public boolean isBeginTransaction()
	{
		return true;
	}
	

	public static final String COMMAND_NAME = "BeginTransaction";


	public HashMap getLogData()
	{
		HashMap dataPairs = new HashMap();
		return dataPairs;
	}

}
