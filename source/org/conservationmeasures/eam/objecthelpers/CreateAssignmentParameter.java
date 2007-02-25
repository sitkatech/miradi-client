/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.util.HashMap;

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
	
	public HashMap getLogData()
	{
		HashMap dataPairs = new HashMap();
		dataPairs.put(TaskId.class.getSimpleName(), taskId);
		dataPairs.put(ProjectResourceId.class.getSimpleName(), resourceId);
		dataPairs.put(FundingSourceId.class.getSimpleName(), fundingId);
		dataPairs.put(AccountingCodeId.class.getSimpleName(), accountingId);
		return dataPairs;
	}
	
	TaskId taskId;
	ProjectResourceId resourceId;
	FundingSourceId fundingId;
	AccountingCodeId accountingId;

}
