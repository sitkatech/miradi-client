/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.ids.ProjectResourceId;
import org.conservationmeasures.eam.ids.TaskId;

public class CreateAssignmentParameter extends CreateObjectParameter
{
	public CreateAssignmentParameter(TaskId taskIdToUse, ProjectResourceId resourceIdToUse)
	{
		taskId = taskIdToUse;
		resourceId = resourceIdToUse;
	}
	
	public TaskId getTaskId()
	{
		return taskId;
	}
	
	public ProjectResourceId getResourceId()
	{
		return resourceId;
	}
	
	TaskId taskId;
	ProjectResourceId resourceId;
}
