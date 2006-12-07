/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ProjectResourceId;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.objectdata.BaseIdData;
import org.conservationmeasures.eam.objectdata.DateRangeEffortListData;
import org.conservationmeasures.eam.objecthelpers.CreateAssignmentParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
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
		
		
	}
	
	public Assignment(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new TaskId(idAsInt), json);
	}

	public int getType()
	{
		return ObjectType.ASSIGNMENT;
	}
	
	public CreateObjectParameter getCreationExtraInfo()
	{
		//TODO create ResourceIdData and TaskIdData classes
		ProjectResourceId resourceId = new ProjectResourceId(resourceIdData.getId().asInt());
		TaskId taskId = new TaskId(taskIdData.getId().asInt());
		return new CreateAssignmentParameter(taskId, resourceId);
	}
	
	public void clear()
	{
		super.clear();
		taskIdData = new BaseIdData();
		resourceIdData = new BaseIdData();
		detailListData = new DateRangeEffortListData();
		
		addNoClearField(TAG_ASSIGNMENT_TASK_ID, taskIdData);
		addNoClearField(TAG_ASSIGNMENT_RESOURCE_ID, resourceIdData);
		addNoClearField(TAG_DATERANGE_EFFORTS, detailListData);
	}
	
	public static final String TAG_ASSIGNMENT_TASK_ID = "TaskId";
	public static final String TAG_ASSIGNMENT_RESOURCE_ID = "ResourceId";
	public static final String TAG_DATERANGE_EFFORTS = "Details";
	
	BaseIdData taskIdData;
	BaseIdData resourceIdData;
	DateRangeEffortListData detailListData;
	
}
