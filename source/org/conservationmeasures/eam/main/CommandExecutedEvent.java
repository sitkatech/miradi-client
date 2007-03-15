/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.IdList;

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
	
	public IdList extractNewlyAddedIds()
	{
			CommandSetObjectData cmd = (CommandSetObjectData)getCommand();
			final String value1 = cmd.getPreviousDataValue();
			final String value2 = cmd.getDataValue();
			return substract(value1, value2);
	}

	public IdList extractNewlyRemovedIds()
	{
		CommandSetObjectData cmd = (CommandSetObjectData) getCommand();
		final String value1 = cmd.getPreviousDataValue();
		final String value2 = cmd.getDataValue();
		return substract(value2, value1);
	}

	private IdList substract(String value1, String value2)
	{
		try
		{
			IdList oldIdList = new IdList(value1);
			IdList newIdList = new IdList(value2);
			newIdList.subtract(oldIdList);
			return newIdList;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return new IdList();
		}
	}


	private Command command;
}
