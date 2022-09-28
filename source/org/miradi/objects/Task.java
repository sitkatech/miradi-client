/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.objects;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.UnknownTaskParentTypeException;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.*;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.GoalSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.CommandVector;

public class Task extends Factor
{
	public Task(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, createSchema(objectManager));
	}

	public static TaskSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static TaskSchema createSchema(ObjectManager objectManager)
	{
		return (TaskSchema) objectManager.getSchemas().get(ObjectType.TASK);
	}

	public CommandVector getDeleteSelfAndSubtasksCommands(Project project) throws Exception
	{
		CommandVector deleteIds = new CommandVector();
		deleteIds.add(new CommandSetObjectData(getType(), getId(), Task.TAG_SUBTASK_IDS, ""));
		int subTaskCount = getSubtaskCount();
		for (int index = 0; index < subTaskCount; index++)
		{
			BaseId subTaskId = getSubtaskId(index);
			Task  subTask = (Task)project.findObject(ObjectType.TASK, subTaskId);
			deleteIds.addAll(subTask.createCommandsToDeleteChildrenAndObject());
		}
		
		return deleteIds;
	}
	
	@Override
	public CommandVector createCommandsToDeleteChildren() throws Exception
	{
		CommandVector commandsToDeleteChildren  = super.createCommandsToDeleteChildren();
		commandsToDeleteChildren.addAll(createCommandsToDeleteBudgetChildren());
		commandsToDeleteChildren.addAll(createCommandsToDeleteRefs(TAG_OUTPUT_REFS));
		commandsToDeleteChildren.addAll(createCommandsToDeleteRefs(TAG_PROGRESS_REPORT_REFS));
		commandsToDeleteChildren.addAll(getDeleteSelfAndSubtasksCommands(getProject()));
		
		return commandsToDeleteChildren;
	}
	
	@Override
	protected CommandVector createCommandsToDereferenceObject() throws Exception
	{
		CommandVector commandsToDereferences = super.createCommandsToDereferenceObject();
		commandsToDereferences.addAll(buildRemoveFromRelevancyListCommands(getRef()));
		
		return commandsToDereferences;
	}
	
	@Override
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_SUBTASK_IDS))
			return TaskSchema.getObjectType();
		
		return super.getAnnotationType(tag);
	}

	@Override
	public String getTypeName()
	{
		ensureCachedTypeStringIsValid();
		return cachedObjectTypeName;
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return new int[] {
			StrategySchema.getObjectType(),
			TaskSchema.getObjectType(),
		};
	}
	
	@Override
	public String getDetails()
	{
		return getData(TAG_DETAILS);
	}
	
	@Override
	public boolean canDirectlyOwnIndicators()
	{
		return false;
	}
	
	//NOTE: this is not testing if this is a Task object...
	//but if it is a user level task as opposed to an activity
	public boolean isTask()
	{
		return is(TaskSchema.OBJECT_NAME);
	}

	@Override
	public boolean isActivity()
	{
		return is(TaskSchema.ACTIVITY_NAME);
	}

	@Override
	public boolean isMonitoringActivity()
	{
		return is(TaskSchema.ACTIVITY_NAME) && getBooleanData(TAG_IS_MONITORING_ACTIVITY);
	}

	private boolean is(final String taskObjectTypeName)
	{
		ensureCachedTypeStringIsValid();
		return (taskObjectTypeName.equals(cachedObjectTypeName));
	}
	
	@Override
	public boolean hasReferrers()
	{
		boolean isSuperShared = super.hasReferrers();
		if (isSuperShared)
			return true;
		
		ORefList referrers = findObjectsThatReferToUs(StrategySchema.getObjectType());
		
		return referrers.size() > 0;
	}
	
	private void ensureCachedTypeStringIsValid()
	{
		ORefList strategyReferrers = findObjectsThatReferToUs(StrategySchema.getObjectType());
		if(strategyReferrers.size() > 0)
		{
			cachedObjectTypeName = TaskSchema.ACTIVITY_NAME;
			return;
		}
		
		// NOTE: We should be able to do this test first, but in Marine Example 1.0.7
		// there are Activities that somehow have owners
		ORef ownerRef = getOwnerRef();
		if (ownerRef != null && !ownerRef.isInvalid())
		{
			cachedObjectTypeName = TaskSchema.OBJECT_NAME;
			return;
		}
		
		EAM.logVerbose("Task with no owner: " + getRef());
	}
	
	public boolean isOrphanedTask()
	{
		return !hasReferrers();
	}
	
	public int getSubtaskCount()
	{
		return getSubtaskIdList().size();
	}
	
	public BaseId getSubtaskId(int index)
	{
		return getSubtaskIdList().get(index);
	}
	
	public IdList getSubtaskIdList()
	{
		return getSafeIdListData(TAG_SUBTASK_IDS);
	}
	
	@Override
	public String toString()
	{
		return getLabel();
	}
	
	@Override
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_STRATEGY_LABEL))
			return getLabelOfTaskParent();
		
		if(fieldTag.equals(PSEUDO_TAG_INDICATOR_LABEL))
			return getLabelOfTaskParent();
		
		if (fieldTag.equals(PSEUDO_TAG_RELEVANT_OBJECTIVE_REFS))
			return getRelevantDesireRefsAsString(ObjectiveSchema.getObjectType());
		
		if (fieldTag.equals(PSEUDO_TAG_RELEVANT_GOAL_REFS))
			return getRelevantDesireRefsAsString(GoalSchema.getObjectType());

		if (fieldTag.equals(PSEUDO_TAG_RELEVANT_INDICATOR_REFS))
			return getRelevantIndicatorRefsAsString();

		if(fieldTag.equals(PSEUDO_TAG_ACTIVITY_TYPE_LABEL))
			return isMonitoringActivity() ? EAM.fieldLabel(ObjectType.FAKE, PSEUDO_TAG_ACTIVITY_TYPE_LABEL) : "";

		return super.getPseudoData(fieldTag);
	}
	
	private String getRelevantDesireRefsAsString(int desireType)
	{
		try
		{
			return getRelevantDesireRefs(desireType).toString();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}
	
	public ORefList getRelevantDesireRefs(int desireType) throws Exception
	{
		ORefSet relevantObjectives = new ORefSet(Desire.findAllRelevantDesires(getProject(), getRef(), desireType));
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		return calculateRelevantRefList(relevantObjectives, relevantOverrides);
	}

	@Override
	public ORefList getRelevantIndicatorRefList() throws Exception
	{
		return getRelevantIndicatorRefs();
	}

	protected String getRelevantIndicatorRefsAsString()
	{
		try
		{
			return getRelevantIndicatorRefs().toString();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}

	public ORefList getRelevantIndicatorRefs() throws Exception
	{
		ORefSet relevantIndicators = new ORefSet(Indicator.findAllRelevantIndicators(getProject(), getRef()));
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		return calculateRelevantRefList(relevantIndicators, relevantOverrides);
	}

	@Override
	public ORefList getChildTaskRefs()
	{
		return new ORefList(TaskSchema.getObjectType(), getSubtaskIdList());
	}
	
	public ORefList getParentRefs()
	{
		try
		{
			int parentType = getTypeOfParent();
			ORefList parentRefs = findObjectsThatReferToUs(parentType);
		
			return parentRefs;
		}
		catch (UnknownTaskParentTypeException e)
		{
			EAM.logVerbose(getRef() + " " + EAM.text("Task does not have a parent"));
			return new ORefList();
		}
	}
	
	private int getTypeOfParent() throws UnknownTaskParentTypeException
	{
		if(isTask())
			return TaskSchema.getObjectType();
		
		if(isActivity())
			return StrategySchema.getObjectType();
		
		throw new UnknownTaskParentTypeException();
	}

	private String getLabelOfTaskParent()
	{

		BaseObject parent = getOwner();
		if(parent == null)
		{
			EAM.logWarning("Parent of task " + getId() + " not found: " + getOwnerRef());
			return "(none)";
		}
		return parent.getData(BaseObject.TAG_LABEL);
	}
	
	public static String getChildTaskTypeCode(int parentType)
	{
		if(parentType == StrategySchema.getObjectType())
			return TaskSchema.ACTIVITY_NAME;
		
		return TaskSchema.OBJECT_NAME;
	}
	
	public static String getTaskIdsTag(BaseObject container) throws Exception
	{
		int type = container.getType();
		switch(type)
		{
			case ObjectType.TASK:
				return Task.TAG_SUBTASK_IDS;
				
			case ObjectType.STRATEGY:
				return Strategy.TAG_ACTIVITY_IDS;
		}
		
		throw new Exception("getTaskIdsTag called for non-task container type " + type);
	}

	public static boolean isActivity(Project projectToUse, ORef ref)
	{
		return is(projectToUse, ref, TaskSchema.ACTIVITY_NAME);
	}
	
	private static boolean is(Project projectToUse, ORef ref, String objectTypeName)
	{
		if (!is(ref))
			return false;
		
		Task task = Task.find(projectToUse, ref);
		return task.is(objectTypeName);
	}

	public static boolean isActivity(BaseObject baseObject)
	{
		if (Task.is(baseObject))
			return ((Task) baseObject).isActivity();
			
		return false;
	}
	
	public static boolean is(BaseObject object)
	{
		if(object == null)
			return false;
		return is(object.getRef());
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == TaskSchema.getObjectType();
	}
	
	public static Task find(ObjectManager objectManager, ORef taskRef)
	{
		return (Task) objectManager.findObject(taskRef);
	}
	
	public static Task find(Project project, ORef taskRef)
	{
		return find(project.getObjectManager(), taskRef);
	}
	
	public final static String TAG_SUBTASK_IDS = "SubtaskIds";
	public final static String TAG_DETAILS = "Details";
	public final static String TAG_IS_MONITORING_ACTIVITY = "IsMonitoringActivity";

	public final static String PSEUDO_TAG_STRATEGY_LABEL = "StrategyLabel";
	public final static String PSEUDO_TAG_INDICATOR_LABEL = "IndicatorLabel";
	public static final String PSEUDO_TAG_RELEVANT_OBJECTIVE_REFS = "PseudoTaskRelevantObjectiveRefs";
	public static final String PSEUDO_TAG_RELEVANT_GOAL_REFS = "PseudoTaskRelevantGoalRefs";
	public static final String PSEUDO_TAG_RELEVANT_INDICATOR_REFS = "PseudoRelevantIndicatorRefs";
	public final static String PSEUDO_TAG_ACTIVITY_TYPE_LABEL = "ActivityTypeLabel";

	private String cachedObjectTypeName;
}
