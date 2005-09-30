package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;

public class CommandEndTransaction extends Command 
{

	public CommandEndTransaction() 
	{
		super();
	}
	
	public void execute(BaseProject target) throws CommandFailedException 
	{
	}

	public void undo(BaseProject target) throws CommandFailedException
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
	
	public boolean isEndTransaction()
	{
		return true;
	}

	public static final String COMMAND_NAME = "EndTransaction";

}
