/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;

public class CommandCreateObject extends Command
{
	public CommandCreateObject(int typeToCreate)
	{
		this(typeToCreate, null);
	}
	
	public CommandCreateObject(int typeToCreate, String extraInfoToUse)
	{
		type = typeToCreate;
		extraInfo = extraInfoToUse;
		createdId = BaseId.INVALID;
	}
	
	public int getObjectType()
	{
		return type;
	}
	
	public String getExtraInfo()
	{
		return extraInfo;
	}
	
	public BaseId getCreatedId()
	{
		return createdId;
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			createdId = target.createObject(type, createdId, extraInfo);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	public void undo(Project target) throws CommandFailedException
	{
		try
		{
			target.deleteObject(type, createdId);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	public static final String COMMAND_NAME = "CreateObject";

	int type;
	String extraInfo;
	BaseId createdId;
}
