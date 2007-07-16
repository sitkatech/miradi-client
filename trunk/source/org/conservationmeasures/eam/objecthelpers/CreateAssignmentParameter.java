/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.util.HashMap;

import org.conservationmeasures.eam.ids.TaskId;

public class CreateAssignmentParameter extends CreateObjectParameter
{
	public CreateAssignmentParameter(TaskId parentTaskIdToUse)
	{
		taskId = parentTaskIdToUse;
	}
	
	public TaskId getTaskId()
	{
		return taskId;
	}
	
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof CreateAssignmentParameter))
			return false;
		
		CreateAssignmentParameter other = (CreateAssignmentParameter)rawOther;
		if (! (other.getTaskId().equals(taskId)))
			return false;
		
		return true;
	}
	
	public String getFormatedDataString()
	{
		HashMap dataPairs = new HashMap();
		dataPairs.put(TaskId.class.getSimpleName(), taskId);
		return formatDataString(dataPairs);
	}
	
	TaskId taskId;
}
