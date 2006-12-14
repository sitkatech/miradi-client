/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.AccountingCodeId;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FundingSourceId;
import org.conservationmeasures.eam.ids.ProjectResourceId;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.objecthelpers.CreateAssignmentParameter;
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
		
		Assignment assignment = new Assignment(TaskId.INVALID, getExtraInfo());
		assertEquals("same task id?", getExtraInfo().getTaskId().toString(), assignment.getData(Assignment.TAG_ASSIGNMENT_TASK_ID));
		assertEquals("same resource id?", getExtraInfo().getResourceId().toString(), assignment.getData(Assignment.TAG_ASSIGNMENT_RESOURCE_ID));
		assertEquals("same extraInfo?", getExtraInfo(), assignment.getCreationExtraInfo());
		
	}
	
	private CreateAssignmentParameter getExtraInfo()
	{
		TaskId taskId = new TaskId(BaseId.INVALID.asInt());
		ProjectResourceId resourceId = new ProjectResourceId(BaseId.INVALID.asInt());
		AccountingCodeId accountingId = AccountingCodeId.INVALID;
		FundingSourceId fundingId = FundingSourceId.INVALID;
		
		return new CreateAssignmentParameter(taskId, resourceId, accountingId, fundingId);
	}
}
