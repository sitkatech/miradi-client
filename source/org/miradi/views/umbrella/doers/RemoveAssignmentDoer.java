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
package org.miradi.views.umbrella.doers;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
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
		commands.addAll(getCommandsToRemoveAssignmenRefFromBaseObject(project, assignmentToRemove));
		
		Command deleteCommand = new CommandDeleteObject(ObjectType.ASSIGNMENT, assignmentToRemove.getId());
		commands.add(deleteCommand);
		
		project.executeCommandsWithoutTransaction((Command[])commands.toArray(new Command[0]));
	}

	private static Vector<Command> getCommandsToRemoveAssignmenRefFromBaseObject(Project project, Assignment assignmentToRemove) throws ParseException
	{
		Vector<Command> commandsToRemoveFromReferrers = new Vector<Command>();
		ORefList referrerRefs = assignmentToRemove.findObjectsThatReferToUs();
		for (int index = 0; index < referrerRefs.size(); ++index)
		{
			BaseObject baseObject = project.findObject(referrerRefs.get(index));
			IdList assignmentIds = new IdList(Assignment.getObjectType(), baseObject.getData(BaseObject.TAG_ASSIGNMENT_IDS));
			if (assignmentIds.contains(assignmentToRemove.getId()))
			{
				commandsToRemoveFromReferrers.add(CommandSetObjectData.createRemoveIdCommand(baseObject, BaseObject.TAG_ASSIGNMENT_IDS, assignmentToRemove.getId()));
			}
		}

		return commandsToRemoveFromReferrers; 
	}
}
