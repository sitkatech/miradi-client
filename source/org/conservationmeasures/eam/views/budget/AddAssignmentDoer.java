/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ProjectResourceId;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateAssignmentParameter;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class AddAssignmentDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
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
		DateRangeEffortList effortList = new DateRangeEffortList();
		Assignment newAssignment =  new Assignment(BaseId.INVALID, createExtraInfo(project), effortList);
		
		Command addAssignmentCommand = CommandSetObjectData.createAppendIdCommand(newAssignment, Task.TAG_ASSIGNMENT_IDS, newAssignment.getId());
		getProject().executeCommand(addAssignmentCommand);
	}
	
	private CreateAssignmentParameter createExtraInfo(Project project)
	{
		Task selectedTask = (Task)project.findObject(ObjectType.TASK, getSelectedIds()[0]);
		TaskId  taskId = new TaskId(selectedTask.getId().asInt());
		ProjectResourceId resourceId = ProjectResourceId.INVALID;
		
		CreateAssignmentParameter extraInfo = new CreateAssignmentParameter(taskId, resourceId);
		
		return extraInfo;
	}
}
