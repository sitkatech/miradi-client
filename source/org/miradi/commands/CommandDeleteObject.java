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
	public CommandDeleteObject(BaseObject baseObjectToDelete)
	{
		this(baseObjectToDelete.getRef());
	}
	
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
	
	public ORef getObjectRef()
	{
		return new ORef(getObjectType(), getObjectId());
	}

	@Override
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	@Override
	public void execute(Project project) throws CommandFailedException
	{
		try
		{
			BaseObject object = project.findObject(type, id);
			reverseExtraInfo = object.getCreationExtraInfo();
			project.deleteObject(object);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	@Override
	public Command getReverseCommand() throws CommandFailedException
	{
		CommandCreateObject command = new CommandCreateObject(type, reverseExtraInfo);
		command.setCreatedId(id);
		return command;
	}
	
	@Override
	public HashMap<String, Comparable> getLogData()
	{
		HashMap<String, Comparable> dataPairs = new HashMap<String, Comparable>();
		dataPairs.put("OBJECT_TYPE", new Integer(type));
		dataPairs.put(BaseId.class.getSimpleName(), id);
		return dataPairs;
	}
	
	@Override
	public String toString()
	{
		return COMMAND_NAME + ": " + getLogData().toString();
	}
	
	public final static String COMMAND_NAME = "DeleteObject";

	int type;
	BaseId id;
	CreateObjectParameter reverseExtraInfo;
}
