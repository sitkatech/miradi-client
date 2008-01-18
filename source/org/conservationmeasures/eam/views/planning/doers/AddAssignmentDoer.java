/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning.doers;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.views.ObjectsDoer;

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
