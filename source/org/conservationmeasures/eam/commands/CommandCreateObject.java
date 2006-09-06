/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.project.Project;

public class CommandCreateObject extends Command
{
	public CommandCreateObject(int typeToCreate)
	{
		this(typeToCreate, null);
	}
	
	public CommandCreateObject(int typeToCreate, CreateObjectParameter parameterToUse)
	{
		type = typeToCreate;
		parameter = parameterToUse;
		createdId = BaseId.INVALID;
	}
	
	public int getObjectType()
	{
		return type;
	}
	
	public CreateObjectParameter getParameter()
	{
		return parameter;
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
			createdId = target.createObject(type, createdId, parameter);
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
	CreateObjectParameter parameter;
	BaseId createdId;
}
