/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
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
	
	abstract public String getCommandName();
	abstract public void execute(Project target) throws CommandFailedException;
	abstract public void undo(Project target) throws CommandFailedException;
}
