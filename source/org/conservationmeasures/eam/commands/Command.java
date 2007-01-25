/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.project.Project;

public abstract class Command
{
	public boolean equals(Object other)
	{
		return toString().equals(other.toString());
	}
	
	public boolean isBeginTransaction()
	{
		return false;
	}
	
	public boolean isEndTransaction()
	{
		return false;
	}
	
	public Command getReverseCommand() throws CommandFailedException
	{
		throw new CommandFailedException("Unimplemented getReverseCommand: " + getClass());
	}
	
	abstract public String getCommandName();
	abstract public void execute(Project target) throws CommandFailedException;
}
