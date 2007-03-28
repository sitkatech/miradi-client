/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.AccountingCodeId;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FundingSourceId;
import org.conservationmeasures.eam.ids.ProjectResourceId;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.objectdata.BaseIdData;
import org.conservationmeasures.eam.objectdata.DateRangeEffortListData;
import org.conservationmeasures.eam.objecthelpers.CreateAssignmentParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class Assignment extends EAMBaseObject
{
	public Assignment(BaseId idToUse, CreateAssignmentParameter extraInfo)
	{
		super(idToUse);
		clear();
		taskIdData.setId(extraInfo.getTaskId());
		resourceIdData.setId(extraInfo.getResourceId());
		accountingIdData.setId(extraInfo.getAccountingCodeId());
		fundingIdData.setId(extraInfo.getFundingSourceId());
	}
	
	public Assignment(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new TaskId(idAsInt), json);
	}

	public int getType()
	{
		return ObjectType.ASSIGNMENT;
	}
	
	public DateRangeEffortList getDetails()
	{
		return detailListData.getDateRangeEffortList();
	}
	
	public void setResourceId(BaseId resourceIdToUse)
	{
		resourceIdData.setId(resourceIdToUse);
	}
	
	public BaseId getResourceId()
	{
		return resourceIdData.getId();
	}
	
	public CreateObjectParameter getCreationExtraInfo()
	{
		//TODO create ResourceIdData and TaskIdData classes
		ProjectResourceId resourceId = new ProjectResourceId(resourceIdData.getId().asInt());
		TaskId taskId = new TaskId(taskIdData.getId().asInt());
		AccountingCodeId accountingId = new AccountingCodeId(accountingIdData.getId().asInt());
		FundingSourceId fundingId = new FundingSourceId(fundingIdData.getId().asInt());
		
		return new CreateAssignmentParameter(taskId, resourceId, accountingId, fundingId);
	}
	
	public void clear()
	{
		super.clear();
		taskIdData = new BaseIdData();
		resourceIdData = new BaseIdData();
		detailListData = new DateRangeEffortListData();
		accountingIdData = new BaseIdData();
		fundingIdData = new BaseIdData();
		
		addNoClearField(TAG_ASSIGNMENT_TASK_ID, taskIdData);
		addNoClearField(TAG_ASSIGNMENT_RESOURCE_ID, resourceIdData);
		addNoClearField(TAG_DATERANGE_EFFORTS, detailListData);
		addNoClearField(TAG_ACCOUNTING_CODE, accountingIdData);
		addNoClearField(TAG_FUNDING_SOURCE, fundingIdData);
	}
	
	public static final String TAG_ASSIGNMENT_TASK_ID = "TaskId";
	public static final String TAG_ASSIGNMENT_RESOURCE_ID = "ResourceId";
	public static final String TAG_DATERANGE_EFFORTS = "Details";
	public static final String TAG_ACCOUNTING_CODE = "AccountingCode";
	public static final String TAG_FUNDING_SOURCE = "FundingSource";
	
	public static final String OBJECT_NAME = "Assignment";
	
	BaseIdData taskIdData;
	BaseIdData resourceIdData;
	DateRangeEffortListData detailListData;
	BaseIdData accountingIdData;
	BaseIdData fundingIdData;
	
}
