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
import org.conservationmeasures.eam.objecthelpers.CreateAssignmentParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class Assignment extends EAMBaseObject
{
	public Assignment(BaseId idToUse, CreateAssignmentParameter extraInfoToUse)
	{
		super(idToUse);
		taskId  = extraInfoToUse.getTaskId();
		resourceId = extraInfoToUse.getResourceId();
		clear();
	}
	
	public Assignment(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new BaseId(idAsInt), json);
	}

	public int getType()
	{
		return ObjectType.ASSIGNMENT;
	}
	
	public CreateObjectParameter getCreationExtraInfo()
	{
		return new CreateAssignmentParameter((TaskId)taskIdData.getId(), (ProjectResourceId)resourceIdData.getId());
	}
	
	public void clear()
	{
		super.clear();
		taskIdData = new BaseIdData();
		taskIdData.setId(taskId);
		
		resourceIdData = new BaseIdData();
		resourceIdData.setId(resourceId);
		
		addNoClearField(TAG_ASSIGNMENT_TASK_ID, taskIdData);
		addNoClearField(TAG_ASSIGNMENT_RESOURCE_ID, resourceIdData);
	}
	
	public static final String TAG_ASSIGNMENT_TASK_ID = "TaskId";
	public static final String TAG_ASSIGNMENT_RESOURCE_ID = "ResourceId";
	
	BaseIdData taskIdData;
	BaseIdData resourceIdData;
	
	TaskId taskId;
	ProjectResourceId resourceId; 
	
	
}
