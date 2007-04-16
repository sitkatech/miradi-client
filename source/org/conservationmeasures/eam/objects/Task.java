/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.ORefData;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.CreateTaskParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.conservationmeasures.eam.views.budget.BudgetTotalsCalculator;

public class Task extends BaseObject
{
	public Task(ObjectManager objectManager, BaseId idToUse, CreateTaskParameter extraInfo) throws Exception
	{
		super(objectManager, new TaskId(idToUse.asInt()));
		clear();
		parentRef.set(extraInfo.getParentRef().toString());
	}
	
	public Task(BaseId idToUse, CreateTaskParameter extraInfo) throws Exception
	{
		super(new TaskId(idToUse.asInt()));
		clear();
		parentRef.set(extraInfo.getParentRef().toString());
	}
	
	public Task(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new TaskId(idAsInt), json);
		parentRef.set(json.optString(TAG_PARENT_REF));
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
			default:
				return false;
		}
	}
	

	public ORefList getReferencedObjects(int objectType)
	{
		ORefList list = super.getReferencedObjects(objectType);
		
		if ((getParentRef()==null) || (getParentRef().getObjectId().equals(BaseId.INVALID)))
			return list;
		
		if (getParentRef().getObjectType()==objectType) 
			list.addAll(new ORefList(new ORef[] {getParentRef()}));

		return list;
	}
	
	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = super.getOwnedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.TASK: 
				list.addAll(new ORefList(objectType, getSubtaskIdList()));
			case ObjectType.ASSIGNMENT: 
				list.addAll(new ORefList(objectType, getAssignmentIdList()));
		}
		return list;
	}
	
	public boolean isTask()
	{
		return getParentRef().getObjectType() == ObjectType.TASK;
	}

	public boolean isActivity()
	{
		return Factor.isFactor(getParentRef().getObjectType());
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
	
	//TODO: look to see if we can get rid of this now we have a get owner:
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
	
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_STRATEGY_LABEL))
			return getLabelOfTaskParent();
		
		if(fieldTag.equals(PSEUDO_TAG_INDICATOR_LABEL))
			return getLabelOfTaskParent();
		
		if (fieldTag.equals(PSEUDO_TAG_SUBTASK_TOTAL))
			return getSubtaskTotalCost();
		
		if (fieldTag.equals(PSEUDO_TAG_TASK_TOTAL))
			return getTaskTotalCost();
		
		if (fieldTag.equals(PSEUDO_TAG_TASK_COST))
			return getTaskCost();
		
		return super.getPseudoData(fieldTag);
	}



	private String getTaskCost()
	{
		try
		{
			BudgetTotalsCalculator calculator = new BudgetTotalsCalculator(objectManager.getProject());
			double cost = calculator.getTaskCost((TaskId)getId());
			return formateResults(cost);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "0";
		}
	}

	private String getSubtaskTotalCost()
	{
		try
		{
			BudgetTotalsCalculator calculator = new BudgetTotalsCalculator(objectManager.getProject());
			double subtaskTotalCost = calculator.getTotalTasksCost(getSubtaskIdList());
			return formateResults(subtaskTotalCost);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "0";
		}
	}

	private String getTaskTotalCost()
	{		
		try
		{
			BudgetTotalsCalculator calculator = new BudgetTotalsCalculator(objectManager.getProject());
			double totalTaskCost = calculator.getTotalTaskCost((TaskId)getId());
			return formateResults(totalTaskCost);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "0";
		}
	}

	private String formateResults(double cost)
	{
		DecimalFormat formater = objectManager.getProject().getCurrencyFormatter();
		return formater.format(cost);
	}


	private String getLabelOfTaskParent()
	{
		if(parentRef == null || getParentRef().getObjectType() == ObjectType.FAKE)
		{
			EAM.logDebug("Task without parent: " + getId());
			return "(none)";
		}
		BaseObject parent = objectManager.findObject(getParentRef());
		if(parent == null)
		{
			EAM.logDebug("Parent of task " + getId() + " not found: " + parentRef);
			return "(none)";
		}
		return parent.getData(BaseObject.TAG_LABEL);
	}

	
	public void clear()
	{
		super.clear();
		parentRef = new ORefData();
		subtaskIds = new IdListData();
		assignmentIds = new IdListData();
		strategyLabel = new PseudoStringData(PSEUDO_TAG_STRATEGY_LABEL);
		indicatorLabel = new PseudoStringData(PSEUDO_TAG_INDICATOR_LABEL);
		subtaskTotal = new PseudoStringData(PSEUDO_TAG_SUBTASK_TOTAL);
		taskTotal = new PseudoStringData(PSEUDO_TAG_TASK_TOTAL);
		taskCost = new PseudoStringData(PSEUDO_TAG_TASK_COST);
		
		addField(TAG_SUBTASK_IDS, subtaskIds);
		addField(TAG_ASSIGNMENT_IDS, assignmentIds);
		addField(PSEUDO_TAG_STRATEGY_LABEL, strategyLabel);
		addField(PSEUDO_TAG_INDICATOR_LABEL, indicatorLabel);
		addField(PSEUDO_TAG_SUBTASK_TOTAL, subtaskTotal);
		addField(PSEUDO_TAG_TASK_TOTAL, taskTotal);
		addField(PSEUDO_TAG_TASK_COST, taskCost);
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
	PseudoStringData strategyLabel;
	PseudoStringData indicatorLabel;
	PseudoStringData subtaskTotal;
	PseudoStringData taskTotal;
	PseudoStringData taskCost;
}
