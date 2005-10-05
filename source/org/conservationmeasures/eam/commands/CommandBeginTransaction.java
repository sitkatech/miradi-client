package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.Project;

public class CommandBeginTransaction extends Command 
{
	public CommandBeginTransaction() 
	{
		super();
	}
	
	public void execute(Project target) throws CommandFailedException 
	{
	}

	public void undo(Project target) throws CommandFailedException
	{
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

}
