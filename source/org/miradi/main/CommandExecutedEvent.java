/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.main;

import org.miradi.commands.Command;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;

public class CommandExecutedEvent
{
	public CommandExecutedEvent(Command wasExecuted)
	{
		command = wasExecuted;
	}
	
	public Command getCommand()
	{
		return command;
	}
	
	public String getCommandName()
	{
		return getCommand().getCommandName();
	}

	public boolean isSetDataCommand()
	{
		return isCommand(CommandSetObjectData.COMMAND_NAME);
	}

	public boolean isCreateObjectCommand()
	{
		return isCommand(CommandCreateObject.COMMAND_NAME);
	}

	public boolean isDeleteObjectCommand()
	{
		return isCommand(CommandDeleteObject.COMMAND_NAME);
	}

	private boolean isCommand(final String string)
	{
		Command rawCommand = getCommand();
		return (rawCommand.getCommandName().equals(string));
	}


	public boolean isSetDataCommandWithThisTypeAndTag(int objectType, String tag)
	{
		if(!isSetDataCommand())
			return false;

		CommandSetObjectData cmd = (CommandSetObjectData)getCommand();
		return (cmd.getObjectType() == objectType && cmd.getFieldTag().equals(tag));
	}
	
	public boolean isSetDataCommandWithThisType(int objectType)
	{
		if(!isSetDataCommand())
			return false;

		CommandSetObjectData cmd = (CommandSetObjectData)getCommand();
		return (cmd.getObjectType() == objectType);
	}
	
	private Command command;
}
