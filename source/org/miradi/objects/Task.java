/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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

import java.util.Arrays;
import java.util.Vector;

import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objectdata.IdListData;
import org.miradi.objectdata.ORefListData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.DateRange;
import org.miradi.utils.EnhancedJsonObject;

public class Task extends Factor
{
	public Task(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, Factor.TYPE_ACTIVITY);
		clear();
	}
	
	public Task(ObjectManager objectManager, FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, Factor.TYPE_ACTIVITY, json);
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
	
	@Override
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_SUBTASK_IDS))
			return Task.getObjectType();
		
			if (tag.equals(TAG_PROGRESS_REPORT_REFS))
			return ProgressReport.getObjectType();
		
		return super.getAnnotationType(tag);
	}

	@Override
	public boolean isIdListTag(String tag)
	{
		if (tag.equals(TAG_SUBTASK_IDS))
			return true;
		
		return false;
	}

	@Override
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_PROGRESS_REPORT_REFS))
			return true;
		
		return super.isRefList(tag);
	}
	
	public int getType()
	{
		return getObjectType();
	}

	public String getTypeName()
	{
		ensureCachedTypeStringIsValid();
		return cachedObjectTypeName;
	}
	
	@Override
	public String getDetails()
	{
		return details.get();
	}
	
	public static int getObjectType()
	{
		return ObjectType.TASK;
	}
	
	@Override
	public boolean canHaveIndicators()
	{
		return false;
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
	
	
	@Override
	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = super.getOwnedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.TASK: 
				list.addAll(new ORefList(objectType, getSubtaskIdList()));
				break;
		}
		return list;
	}
	
	//NOTE: this is not testing if this is a Task object...
	//but if it is a user level task as opposed to a method or an activity
	public boolean isTask()
	{
		ensureCachedTypeStringIsValid();
		return (OBJECT_NAME.equals(cachedObjectTypeName));
	}

	@Override
	public boolean isActivity()
	{
		ensureCachedTypeStringIsValid();
		return (ACTIVITY_NAME.equals(cachedObjectTypeName));
	}

	public boolean isMethod()
	{
		ensureCachedTypeStringIsValid();
		return (METHOD_NAME.equals(cachedObjectTypeName));
	}
	
	@Override
	public boolean mustBeDeletedBecauseParentIsGone()
	{
		boolean isSuperShared = super.mustBeDeletedBecauseParentIsGone();
		if (isSuperShared)
			return true;
		
		ORefList referrers = findObjectsThatReferToUs(Strategy.getObjectType());
		
		return referrers.size() > 0;
	}
	
	private void ensureCachedTypeStringIsValid()
	{
		ORefList strategyReferrers = findObjectsThatReferToUs(Strategy.getObjectType());
		if(strategyReferrers.size() > 0)
		{
			cachedObjectTypeName = ACTIVITY_NAME;
			return;
		}
		
		ORefList indicatorReferrers = findObjectsThatReferToUs(Indicator.getObjectType());
		if(indicatorReferrers.size() > 0)
		{
			cachedObjectTypeName = METHOD_NAME;
			return;
		}

		// FIXME: We should be able to do this test first, but in Marine Example 1.0.7
		// there are Activities that somehow have owners
		ORef ownerRef = getOwnerRef();
		if (ownerRef != null && !ownerRef.isInvalid())
		{
			cachedObjectTypeName = OBJECT_NAME;
			return;
		}
		
		EAM.logVerbose("Task with no owner: " + getRef());
	}
	
	public boolean isOrphandTask()
	{
		return !hasReferrers();
	}
	
	public boolean isShared()
	{
		return findObjectsThatReferToUs().size() > 1;
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
		
		return super.getPseudoData(fieldTag);
	}

	@Override
	public DateRange getWhenRollup() throws Exception
	{
		if (hasSubTasks())
			return combineSubtaskEffortListDateRanges(getSubtaskRefs());
		
		return combineAssignmentEffortListDateRanges();
	}

	@Override
	public ORefSet getWhoRollup() throws Exception
	{
		if (hasSubTasks())
			return getAllResources(getSubtaskRefs());
		
		return getTaskResources();
	}
	
	public boolean hasSubTasks()
	{
		return getSubtaskCount() > 0;
	}
		
	public ORefSet getTaskResources()
	{
		ORefSet resourceRefs = new ORefSet();
		ORefList assignmentRefs = getAssignmentRefs();
		for (int i = 0; i < assignmentRefs.size(); ++i)
		{
			Assignment assignment = (Assignment.find(getProject(), assignmentRefs.get(i)));
			resourceRefs.add(assignment.getResourceRef());	
		}
		
		return resourceRefs;
	}
	
	public DateRange combineAssignmentEffortListDateRanges() throws Exception
	{
		DateRange combinedDateRange = null;
		ORefList assignmentRefs = getAssignmentRefs();
		for (int i = 0; i < assignmentRefs.size(); ++i)
		{
 			Assignment assignment = (Assignment) objectManager.findObject(assignmentRefs.get(i));
			DateRange dateRange = assignment.getCombinedEffortListDateRange();
			combinedDateRange = DateRange.combine(combinedDateRange, dateRange);
		}
		
		return combinedDateRange;
	}

	public ORefList getSubtaskRefs()
	{
		return new ORefList(Task.getObjectType(), getSubtaskIdList());
	}
	
	public ORefList getProgressReportRefs()
	{
		return progressReportRefs.getORefList();
	}
	
	@Override
	public ProgressReport getLatestProgressReport()
	{
		ProgressReport progressReport = (ProgressReport) getLatestObject(getObjectManager(), getProgressReportRefs(), ProgressReport.TAG_PROGRESS_DATE);
		return progressReport;
	}
	
	public String getParentTypeCode()
	{
		if(isActivity())
			return Strategy.OBJECT_NAME;
		
		if(isMethod())
			return Indicator.OBJECT_NAME;
		
		Task owner = (Task)getOwner();
		if(owner.isActivity())
			return ACTIVITY_NAME;
		if(owner.isMethod())
			return METHOD_NAME;
		
		return OBJECT_NAME;
	}
	
	@Override
	public int getWorkUnits(DateRange dateRangeToUse) throws Exception
	{
		if (hasSubTasks())
			return getWorkUnits(getSubtaskRefs(), dateRangeToUse);
		
		return super.getWorkUnits(dateRangeToUse);
	}

	@Override
	public int getTotalShareCount()
	{
		int type = getTypeOfParent();
		ORefList parentRefs = findObjectsThatReferToUs(type); 
		if(isTask())
		{
			ORef parentRef = parentRefs.get(0);
			Task parentTask = Task.find(getObjectManager(), parentRef);
			return parentTask.getTotalShareCount();
		}
		
		return parentRefs.size();
	}
	
	public int getTypeOfParent()
	{
		if(isTask())
			return Task.getObjectType();
		
		if(isMethod())
			return Indicator.getObjectType();
		
		if(isActivity())
			return Strategy.getObjectType();
		
		throw new RuntimeException("Unknown task type: " + getRef());
	}

	@Override
	public double getBudgetCostRollup(DateRange dateRangeToUse) throws Exception
	{
		if (getSubtaskCount() == 0)
			return getTotalAssignmentCost(dateRangeToUse);

		return getTotalSubtasksCost(dateRangeToUse);
	}

	private double getTotalSubtasksCost(DateRange dateRangeToUse) throws Exception
	{
		double total = getTotalAssignmentCost(dateRangeToUse);
		int subTaskCount = getSubtaskCount();
		for (int index = 0; index < subTaskCount; index++)
		{
			BaseId subTaskId = getSubtaskId(index);
			Task  subTask = (Task)getProject().findObject(ObjectType.TASK, subTaskId);
			total += subTask.getBudgetCost(dateRangeToUse);
		}
		
		return total;
	}
	
	public double getTotalAssignmentCost(DateRange dateRangeToUse) throws Exception
	{
		double totalAssignmentsCost = 0;
		ORefList assignmentRefs = getAssignmentRefs();
		for (int index = 0; index < assignmentRefs.size(); index++)
		{
			Assignment assignment = Assignment.find(getProject(), assignmentRefs.get(index));
			totalAssignmentsCost += assignment.getTotalAssignmentCost(dateRangeToUse);
		}
		
		return totalAssignmentsCost;
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
		if(parentType == Strategy.getObjectType())
			return ACTIVITY_NAME;
		
		if(parentType == Indicator.getObjectType())
			return METHOD_NAME;
		
		return OBJECT_NAME;
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
				
			case ObjectType.INDICATOR:
				return Indicator.TAG_METHOD_IDS;
		}
		
		throw new Exception("getTaskIdsTag called for non-task container type " + type);
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
		return objectType == getObjectType();
	}
	
	public static Task find(ObjectManager objectManager, ORef taskRef)
	{
		return (Task) objectManager.findObject(taskRef);
	}
	
	public static Task find(Project project, ORef taskRef)
	{
		return find(project.getObjectManager(), taskRef);
	}
	
	@Override
	public void clear()
	{
		super.clear();
		subtaskIds = new IdListData(TAG_SUBTASK_IDS, Task.getObjectType());
		progressReportRefs = new ORefListData(TAG_PROGRESS_REPORT_REFS);
		details = new StringData(TAG_DETAILS);
		
		strategyLabel = new PseudoStringData(PSEUDO_TAG_STRATEGY_LABEL);
		indicatorLabel = new PseudoStringData(PSEUDO_TAG_INDICATOR_LABEL);
		taskTotal = new PseudoStringData(PSEUDO_TAG_BUDGET_TOTAL);
		
		addField(TAG_SUBTASK_IDS, subtaskIds);
		addField(TAG_PROGRESS_REPORT_REFS, progressReportRefs);
		addField(TAG_DETAILS, details);
		
		addField(PSEUDO_TAG_STRATEGY_LABEL, strategyLabel);
		addField(PSEUDO_TAG_INDICATOR_LABEL, indicatorLabel);
		addField(PSEUDO_TAG_BUDGET_TOTAL, taskTotal);		
	}

	
	public final static String TAG_SUBTASK_IDS = "SubtaskIds";
	public final static String TAG_PROGRESS_REPORT_REFS = "ProgressReportRefs";
	public final static String TAG_DETAILS = "Details";
	
	public final static String PSEUDO_TAG_STRATEGY_LABEL = "StrategyLabel";
	public final static String PSEUDO_TAG_INDICATOR_LABEL = "IndicatorLabel";
	public final static String PSEUDO_TAG_TASK_BUDGET_DETAIL = "PseudoTaskBudgetDetail";
	
	public static final String OBJECT_NAME = "Task";
	public static final String METHOD_NAME = "Method";
	public static final String ACTIVITY_NAME = "Activity";
	
	private String cachedObjectTypeName;
	
	private IdListData subtaskIds;
	private ORefListData progressReportRefs;
	private StringData details;
	
	private PseudoStringData strategyLabel;
	private PseudoStringData indicatorLabel;
	private PseudoStringData taskTotal;
	
}
