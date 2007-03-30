/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateAssignmentParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
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
			createAssignment(getProject());
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private void createAssignment(Project project) throws Exception
	{
		Task selectedTask = (Task)project.findObject(ObjectType.TASK, getSelectedIds()[0]);

		project.executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject createAssignment = new CommandCreateObject(ObjectType.ASSIGNMENT, createExtraInfo(project, selectedTask));
			project.executeCommand(createAssignment);

			Command appendAssignment = CommandSetObjectData.createAppendIdCommand(selectedTask, Task.TAG_ASSIGNMENT_IDS, createAssignment.getCreatedId());
			project.executeCommand(appendAssignment);
		}
		finally 
		{
			project.executeCommand(new CommandEndTransaction());	
		}
	}
	
	private CreateAssignmentParameter createExtraInfo(Project project, Task selectedTask)
	{
		TaskId  taskId = new TaskId(selectedTask.getId().asInt());
		
		CreateAssignmentParameter extraInfo = new CreateAssignmentParameter(taskId);
		
		return extraInfo;
	}
}
