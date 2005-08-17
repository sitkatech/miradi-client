package org.conservationmeasures.eam.commands;

import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;

public class CommandBeginTransaction extends Command 
{

	public CommandBeginTransaction() 
	{
		super();
	}
	
	public void execute(BaseProject target) throws CommandFailedException 
	{
	}

	public void writeTo(DataOutputStream dataOut) throws IOException 
	{
		dataOut.writeUTF(getCommandName());
	}

	public void undo(BaseProject target) throws CommandFailedException
	{
	}
	
	public static String getCommandName()
	{
		return "BeginTransaction";
	}

	public String toString()
	{
		return getCommandName();
	}

}
