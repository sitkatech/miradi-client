/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.commands;

import java.util.HashMap;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.project.Project;

public class CommandEndTransaction extends Command 
{

	public CommandEndTransaction() 
	{
		super();
	}
	
	public void execute(Project target) throws CommandFailedException 
	{
		target.endTransaction();
	}
	
	public Command getReverseCommand() throws CommandFailedException
	{
		return new CommandBeginTransaction();
	}

	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public String toString()
	{
		return getCommandName();
	}
	
	public boolean isEndTransaction()
	{
		return true;
	}

	public HashMap getLogData()
	{
		HashMap dataPairs = new HashMap();
		return dataPairs;
	}
	
	public static final String COMMAND_NAME = "EndTransaction";

}
