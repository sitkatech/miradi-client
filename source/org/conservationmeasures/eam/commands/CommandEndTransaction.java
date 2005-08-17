package org.conservationmeasures.eam.commands;

import java.io.DataOutputStream;
import java.io.IOException;

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

	public void writeTo(DataOutputStream dataOut) throws IOException 
	{
		dataOut.writeUTF(getCommandName());
	}
	
	public void undo(BaseProject target) throws CommandFailedException
	{
	}

	public static String getCommandName()
	{
		return "EndTransaction";
	}
	
	public String toString()
	{
		return getCommandName();
	}
	
}
