/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.planning.doers;

import org.miradi.commands.Command;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Task;
import org.miradi.views.ObjectsDoer;

public class AddAssignmentDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if (getObjects().length != 1)
			return false;
		
		if (getSelectedObjectType() != ObjectType.TASK)
			return false;
		
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;

		try 
		{
			Task selectedTask = (Task)getProject().findObject(ObjectType.TASK, getSelectedIds()[0]);
			if (selectedTask.getSubtaskCount() > 0)
			{
				EAM.errorDialog(EAM.text("Resources cannot be added to this task because it already has subtasks."));
				return;
			}
			
			createAssignment(selectedTask);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private void createAssignment(Task selectedTask) throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject createAssignment = new CommandCreateObject(ObjectType.ASSIGNMENT);
			getProject().executeCommand(createAssignment);

			Command appendAssignment = CommandSetObjectData.createAppendIdCommand(selectedTask, Task.TAG_ASSIGNMENT_IDS, createAssignment.getCreatedId());
			getProject().executeCommand(appendAssignment);
		}
		finally 
		{
			getProject().executeCommand(new CommandEndTransaction());	
		}
	}
	
}
