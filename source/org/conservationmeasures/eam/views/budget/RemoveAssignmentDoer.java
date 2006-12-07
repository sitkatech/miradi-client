/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ObjectsDoer;

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
	
		try
		{
			BaseId selectedId = getObjects()[0].getId();
			removeAssignment(getProject(), selectedId);
			
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	public static void removeAssignment(Project project, BaseId idToRemove) throws Exception
	{
		Task task = getTaskForAssignment(project, idToRemove);
		
		Command removeIdCommand = CommandSetObjectData.createRemoveIdCommand(task, Task.TAG_ASSIGNMENT_IDS, idToRemove);
		project.executeCommand(removeIdCommand);
		
		Command deleteCommand = new CommandDeleteObject(ObjectType.ASSIGNMENT, idToRemove);
		project.executeCommand(deleteCommand);
	}

	private static Task getTaskForAssignment(Project project, BaseId idToRemove)
	{
		return project.findTasksThatUseThisAssignment(idToRemove)[0];
	}
}
