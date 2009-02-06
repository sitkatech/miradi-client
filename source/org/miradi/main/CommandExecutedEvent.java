/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.main;

import org.miradi.commands.Command;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.objects.Factor;

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

	public boolean isCreateCommandForThisType(int objectType)
	{
		if (!isCreateObjectCommand())
			return false;
		
		CommandCreateObject createCommand = (CommandCreateObject) getCommand();
		return createCommand.getObjectType() == objectType;		
	}

	public boolean isSetDataCommandWithThisTypeAndTag(int objectType, String tag)
	{
		return isSetDataCommandWithThisType(objectType) && isSetDataCommandWithThisTag(tag);
	}
	
	public boolean isSetDataCommandWithThisTag(String tag)
	{
		if(!isSetDataCommand())
			return false;

		CommandSetObjectData cmd = (CommandSetObjectData)getCommand();
		return (cmd.getFieldTag().equals(tag));
	}
	
	public boolean isSetDataCommandWithThisType(int objectType)
	{
		if(!isSetDataCommand())
			return false;

		CommandSetObjectData cmd = (CommandSetObjectData)getCommand();
		return (cmd.getObjectType() == objectType);
	}
	
	public boolean isFactorSetDataCommandWithThisTypeAndTag(String tag)
	{
		if (!isSetDataCommand())
			return false;
		
		CommandSetObjectData cmd = (CommandSetObjectData) getCommand();
		return (Factor.isFactor(cmd.getObjectType()) && cmd.getFieldTag().equals(tag));
	}
	
	private Command command;

}
