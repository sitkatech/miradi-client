/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.commands;

import java.util.HashMap;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

public class CommandDeleteObject extends Command
{
	public CommandDeleteObject(ORef ref)
	{
		this(ref.getObjectType(), ref.getObjectId());
	}
	
	public CommandDeleteObject(int objectType, BaseId objectId)
	{
		type = objectType;
		id = objectId;
		reverseExtraInfo = null;
	}
	
	public int getObjectType()
	{
		return type;
	}
	
	public BaseId getObjectId()
	{
		return id;
	}

	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			BaseObject object = target.findObject(type, id);
			reverseExtraInfo = object.getCreationExtraInfo();
			target.deleteObject(object);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	public Command getReverseCommand() throws CommandFailedException
	{
		CommandCreateObject command = new CommandCreateObject(type, reverseExtraInfo);
		command.setCreatedId(id);
		return command;
	}
	
	public HashMap getLogData()
	{
		HashMap dataPairs = new HashMap();
		dataPairs.put("OBJECT_TYPE", new Integer(type));
		dataPairs.put(BaseId.class.getSimpleName(), id);
		return dataPairs;
	}
	
	public final static String COMMAND_NAME = "DeleteObject";

	int type;
	BaseId id;
	CreateObjectParameter reverseExtraInfo;
}
