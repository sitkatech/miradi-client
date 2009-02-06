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

import java.text.ParseException;
import java.util.Arrays;
import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Assignment;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.views.ObjectsDoer;

public class RemoveAssignmentDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if (getObjects().length == 0 )
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
	
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			Assignment selectedObject = (Assignment)getObjects()[0];
			removeAssignment(getProject(), selectedObject);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	public static void removeAssignment(Project project, Assignment assignmentToRemove) throws Exception
	{
		Vector commands = new Vector();

		commands.addAll(Arrays.asList(assignmentToRemove.createCommandsToClear()));
		commands.add(getCommandsToRemoveAssignmenRefFromTask(project, assignmentToRemove));
		
		Command deleteCommand = new CommandDeleteObject(ObjectType.ASSIGNMENT, assignmentToRemove.getId());
		commands.add(deleteCommand);
		
		project.executeCommandsWithoutTransaction((Command[])commands.toArray(new Command[0]));
	}

	private static Command getCommandsToRemoveAssignmenRefFromTask(Project project, Assignment assignmentToRemove) throws ParseException
	{
		ORef ownerRef = assignmentToRemove.findObjectWhoOwnsUs(project.getObjectManager(), Task.getObjectType(), assignmentToRemove.getRef());
		Task task = (Task)project.findObject(ownerRef);	
		Command removeIdCommand = CommandSetObjectData.createRemoveIdCommand(task, Task.TAG_ASSIGNMENT_IDS, assignmentToRemove.getId());
		
		return removeIdCommand;
	}
}
