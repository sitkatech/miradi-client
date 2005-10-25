/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.exceptions.CommandFailedException;

public interface CommandExecutedListener
{
	public void commandExecuted(CommandExecutedEvent event);
	public void commandFailed(Command command, CommandFailedException e);
}
