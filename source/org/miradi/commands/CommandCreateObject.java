/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.commands;

import java.util.HashMap;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;

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
	
	public ORef getObjectRef()
	{
		return new ORef(getObjectType(), getCreatedId());
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
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	public Command getReverseCommand() throws CommandFailedException
	{
		return new CommandDeleteObject(type, createdId);
	}

	public HashMap getLogData()
	{
		HashMap dataPairs = new HashMap();
		dataPairs.put("OBJECT_TYPE", new Integer(type));
		if (parameter!=null)
			dataPairs.put(parameter.getClass().getSimpleName(), parameter.getFormatedDataString());
		return dataPairs;
	}
	
	public static final String COMMAND_NAME = "CreateObject";

	int type;
	CreateObjectParameter parameter;
	BaseId createdId;

}
