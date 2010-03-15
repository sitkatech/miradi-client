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

package org.miradi.views.planning.doers;

import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Task;
import org.miradi.views.diagram.CreateAnnotationDoer;


abstract public class AbstractCreateTaskNodeDoer extends AbstractTreeNodeDoer
{
	@Override
	public boolean isAvailable()
	{
		try
		{
			ORef parentRef = getParentRef();
			if (parentRef.isInvalid())
				return false;

			if(!childWouldBeVisible(Task.getChildTaskTypeCode(parentRef.getObjectType())))
				return false;

			return true;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
			return false;
		}
	}
	
	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			ORef newTaskRef = createTask();
			CreateAnnotationDoer.ensureObjectVisible(newTaskRef, getPicker());
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	private ORef createTask() throws Exception
	{
		ORef parentRef = getParentRef();
		BaseObject parentOfTask = BaseObject.find(getProject(), parentRef);
		
		getProject().executeBeginTransaction();
		try
		{
			CommandCreateObject create = new CommandCreateObject(ObjectType.TASK);
			getProject().executeCommand(create);
			
			ORef newTaskRef = create.getObjectRef();
			String containerTag = Task.getTaskIdsTag(parentOfTask);
			CommandSetObjectData appendCommand = CommandSetObjectData.createAppendIdCommand(parentOfTask, containerTag, newTaskRef.getObjectId());
			getProject().executeCommand(appendCommand);
			
			return newTaskRef;
		}
		finally
		{
			getProject().executeEndTransaction();
		}
	}
	
	abstract protected ORef getParentRef();
}
