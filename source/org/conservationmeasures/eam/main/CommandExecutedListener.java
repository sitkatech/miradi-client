/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.exceptions.CommandFailedException;

public interface CommandExecutedListener
{
	public void commandExecuted(CommandExecutedEvent event);
	public void commandUndone(CommandExecutedEvent event);
	public void commandFailed(Command command, CommandFailedException e);
}
