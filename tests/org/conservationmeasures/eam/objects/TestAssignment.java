/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ProjectResourceId;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.objecthelpers.CreateAssignmentParameter;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestAssignment extends ObjectTestCase
{
	public TestAssignment(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		verifyFields(ObjectType.ASSIGNMENT, getExtraInfo());
	}
	
	private CreateAssignmentParameter getExtraInfo()
	{
		TaskId taskId = new TaskId(BaseId.INVALID.asInt());
		ProjectResourceId resourceId = new ProjectResourceId(BaseId.INVALID.asInt());
		DateRangeEffortList detailList = new DateRangeEffortList();
		return new CreateAssignmentParameter(taskId, resourceId, detailList);
	}
}
