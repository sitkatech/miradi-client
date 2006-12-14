/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.ids.AccountingCodeId;
import org.conservationmeasures.eam.ids.FundingSourceId;
import org.conservationmeasures.eam.ids.ProjectResourceId;
import org.conservationmeasures.eam.ids.TaskId;

public class CreateAssignmentParameter extends CreateObjectParameter
{
	public CreateAssignmentParameter(TaskId taskIdToUse, ProjectResourceId resourceIdToUse,
									AccountingCodeId accountingIdToUse, FundingSourceId fundingIdToUse)
	{
		taskId = taskIdToUse;
		resourceId = resourceIdToUse;
		accountingId = accountingIdToUse;
		fundingId = fundingIdToUse;
	}
	
	public TaskId getTaskId()
	{
		return taskId;
	}
	
	public ProjectResourceId getResourceId()
	{
		return resourceId;
	}
	
	public AccountingCodeId getAccountingCodeId()
	{
		return accountingId;
	}
	
	public FundingSourceId getFundingSourceId()
	{
		return fundingId;
	}
	
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof CreateAssignmentParameter))
			return false;
		
		CreateAssignmentParameter other = (CreateAssignmentParameter)rawOther;
		if (! (other.getResourceId().equals(resourceId)))
			return false;
		
		if (! (other.getTaskId().equals(taskId)))
			return false;
		
		if (! (other.getFundingSourceId().equals(fundingId)))
			return false;
		
		if (! (other.getAccountingCodeId().equals(accountingId)))
			return false;
		
		return true;
	}
	
	TaskId taskId;
	ProjectResourceId resourceId;
	FundingSourceId fundingId;
	AccountingCodeId accountingId;
}
