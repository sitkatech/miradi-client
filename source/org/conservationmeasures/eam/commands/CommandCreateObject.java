/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
	
	void setCreatedId(BaseId id)
	{
		createdId = id;
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
	
	public Command getReverseCommand() throws CommandFailedException
	{
		return new CommandDeleteObject(type, createdId);
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
