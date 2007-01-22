/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.exceptions.AlreadyInThatViewException;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.project.Project;

public class CommandSwitchView extends Command
{
	public CommandSwitchView(String destinationView)
	{
		toView = destinationView;
	}

	public String getDestinationView()
	{
		return toView;
	}
	
	public String getPreviousView()
	{
		return fromView;
	}

	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		fromView = target.getCurrentView();
		if(fromView.equals(toView))
			throw new AlreadyInThatViewException("Already in view " + toView);
		
		target.switchToView(toView);
	}
	
	public Command getReverseCommand() throws CommandFailedException
	{
		return new CommandSwitchView(getPreviousView());
	}

	public String toString()
	{
		StringBuffer string = new StringBuffer();
		string.append(getCommandName());
		string.append(" from ");
		string.append(getPreviousView());
		string.append( " to ");
		string.append(getDestinationView());
		return string.toString();
	}

	public void undo(Project target) throws CommandFailedException
	{
		target.switchToView(fromView);
	}
	

	public static final String COMMAND_NAME = "SwitchView";

	String toView;
	String fromView;
}
