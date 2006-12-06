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
	public CreateAssignmentParameter(TaskId taskIdToUse, ProjectResourceId resourceIdToUse, DateRangeEffortList dateRangeEffortListToUse)
	{
		taskId = taskIdToUse;
		resourceId = resourceIdToUse;
		dateRangeEffortList = dateRangeEffortListToUse;
	}
	
	public TaskId getTaskId()
	{
		return taskId;
	}
	
	public ProjectResourceId getResourceId()
	{
		return resourceId;
	}
	
	public DateRangeEffortList getDateRangeEffortList()
	{
		return dateRangeEffortList;
	}
	
	TaskId taskId;
	ProjectResourceId resourceId;
	DateRangeEffortList dateRangeEffortList;
}
