/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.ORefData;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.CreateTaskParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class Task extends BaseObject
{
	public Task(BaseId idToUse, CreateTaskParameter extraInfo) throws Exception
	{
		super(new TaskId(idToUse.asInt()));
		clear();
		parentRef.set(extraInfo.getParentRef().toString());
	}
	
	public Task(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new TaskId(idAsInt), json);
		parentRef.set(json.optString(TAG_PARENT_REF));
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject jsonObject = super.toJson();
		jsonObject.put(TAG_PARENT_REF, parentRef.toString());
		
		return jsonObject;
	}
	
	public Vector getDeleteSelfAndSubtasksCommands(Project project) throws Exception
	{
		Vector deleteIds = new Vector();
		deleteIds.add(new CommandSetObjectData(getType(), getId(), Task.TAG_SUBTASK_IDS, ""));
		int subTaskCount = getSubtaskCount();
		for (int index = 0; index < subTaskCount; index++)
		{
			BaseId subTaskId = getSubtaskId(index);
			Task  subTask = (Task)project.findObject(ObjectType.TASK, subTaskId);
			Vector returnedDeleteCommands = subTask.getDeleteSelfAndSubtasksCommands(project);
			deleteIds.addAll(returnedDeleteCommands);
			
		}
		
		deleteIds.addAll(Arrays.asList(createCommandsToClear()));
		deleteIds.add(new CommandDeleteObject(getType(), getId()));
		
		return deleteIds;
	}

	public int getType()
	{
		return getObjectType();
	}

	public static int getObjectType()
	{
		return ObjectType.TASK;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		switch(type)
		{
			case ObjectType.TASK: 
				return true;
			case ObjectType.ASSIGNMENT: 
				return true;
			default:
				return false;
		}
	}
	
	
	public static boolean canReferToThisType(int type)
	{
		switch(type)
		{
			case ObjectType.TASK: 
				return true;
			case ObjectType.FACTOR: 
				return true;
			case ObjectType.INDICATOR: 
				return true;
			default:
				return false;
		}
	}
	
	public boolean isTask()
	{
		return getParentRef().getObjectType() == ObjectType.TASK;
	}

	public boolean isActivity()
	{
		return getParentRef().getObjectType() == ObjectType.FACTOR;
	}

	public boolean isMethod()
	{
		return getParentRef().getObjectType() == ObjectType.INDICATOR;
	}

	public void addSubtaskId(BaseId subtaskId)
	{
		subtaskIds.add(subtaskId);
	}
	
	public int getSubtaskCount()
	{
		return subtaskIds.size();
	}
	
	public BaseId getSubtaskId(int index)
	{
		return subtaskIds.get(index);
	}
	
	public IdList getSubtaskIdList()
	{
		return subtaskIds.getIdList().createClone();
	}
	
	public IdList getAssignmentIdList()
	{
		return assignmentIds.getIdList().createClone();
	}
	
	public CreateObjectParameter getCreationExtraInfo()
	{
		return new CreateTaskParameter(parentRef.getRawRef());
	}
	
	public void setParentRef(ORef parentRefToUse) throws Exception
	{
		parentRef.set(parentRefToUse);
	}
	
	public ORef getParentRef()
	{
		return parentRef.getRawRef();
	}

	public String toString()
	{
		return getLabel();
	}
	
	
	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if (fieldTag.equals(TAG_PARENT_REF))
			parentRef.set(dataValue);
		else
			super.setData(fieldTag, dataValue);
	}
	
	public String getData(String fieldTag)
	{
		if (fieldTag.equals(TAG_PARENT_REF))
			return parentRef.get();
		
		return super.getData(fieldTag);
	}


	public void clear()
	{
		super.clear();
		parentRef = new ORefData();
		subtaskIds = new IdListData();
		assignmentIds = new IdListData();
		
		addField(TAG_SUBTASK_IDS, subtaskIds);
		addField(TAG_ASSIGNMENT_IDS, assignmentIds);
	}

	public final static String TAG_PARENT_REF = "ParentRef";
	public final static String TAG_SUBTASK_IDS = "SubtaskIds";
	public final static String TAG_ASSIGNMENT_IDS = "AssignmentIds";
	public final static String PSEUDO_TAG_STRATEGY_LABEL = "PseudoTagStrategyLabel";
	public final static String PSEUDO_TAG_INDICATOR_LABEL = "PseudoTagIndicatorLabel";
	public final static String PSEUDO_TAG_SUBTASK_TOTAL = "PseudoTagSubtaskTotal";
	public final static String PSEUDO_TAG_TASK_TOTAL = "PseudoTagTaskTotal";
	public final static String PSEUDO_TAG_TASK_COST = "PseudoTagTaskCost";
	
	IdListData subtaskIds;
	IdListData assignmentIds;
	ORefData parentRef;
}
