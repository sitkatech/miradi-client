/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objects;

import java.util.Arrays;
import java.util.Set;
import java.util.Vector;

import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.ids.TaskId;
import org.miradi.main.EAM;
import org.miradi.objectdata.IdListData;
import org.miradi.objectdata.ORefListData;
import org.miradi.objecthelpers.DateRangeEffortList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.DateRange;
import org.miradi.utils.EnhancedJsonObject;

public class Task extends BaseObject
{
	public Task(ObjectManager objectManager, BaseId idToUse) throws Exception
	{
		super(objectManager, new TaskId(idToUse.asInt()));
		clear();
	}
	
	public Task(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new TaskId(idAsInt), json);
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
	
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_SUBTASK_IDS))
			return Task.getObjectType();
		
		if (tag.equals(TAG_ASSIGNMENT_IDS))
			return Assignment.getObjectType();
		
		return super.getAnnotationType(tag);
	}

	public boolean isIdListTag(String tag)
	{
		if (tag.equals(TAG_SUBTASK_IDS))
			return true;
		
		if (tag.equals(TAG_ASSIGNMENT_IDS))
			return true;
		
		return false;
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
		if(type == Task.getObjectType())
			return true;
		
		return false;
	}
	
	public Set<String> getReferencedObjectTags()
	{
		Set<String> set = super.getReferencedObjectTags();
		set.add(TAG_SUBTASK_IDS);
		set.add(TAG_ASSIGNMENT_IDS);
		return set;
	}
	

	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = super.getOwnedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.TASK: 
				list.addAll(new ORefList(objectType, getSubtaskIdList()));
				break;
			case ObjectType.ASSIGNMENT: 
				list.addAll(new ORefList(objectType, getAssignmentIdList()));
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
	
	public IdList getAssignmentIdList()
	{
		return assignmentIds.getIdList().createClone();
	}
	
	public ORefList getAssignmentRefs()
	{
		return new ORefList(Assignment.getObjectType(), getAssignmentIdList());
	}
	
	public String toString()
	{
		return getLabel();
	}
	
	
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_STRATEGY_LABEL))
			return getLabelOfTaskParent();
		
		if(fieldTag.equals(PSEUDO_TAG_INDICATOR_LABEL))
			return getLabelOfTaskParent();
		
		if (fieldTag.equals(PSEUDO_TAG_ASSIGNED_RESOURCES_HTML))
			return getAppendedResourceNames();
		
		return super.getPseudoData(fieldTag);
	}

	public DateRange getCombinedEffortDates() throws Exception
	{
		if (getSubtaskCount() > 0)
			return combineSubtaskEffortListDateRanges(getSubtaskRefs());
		
		return combineAssignmentEffortListDateRanges(this);
	}

	public String getAppendedResourceNames()
	{
		String appendedResources = "";
		for (int i = 0; i < assignmentIds.size(); ++i)
		{
			Assignment assignment = (Assignment) objectManager.findObject(Assignment.getObjectType(),assignmentIds.get(i));
			ORef resourceRef = assignment.getResourceRef();
			ProjectResource resource = (ProjectResource) objectManager.findObject(resourceRef);
			if (resource == null)
				continue;
			if (i > 0)
				appendedResources += ", "; 
					
			appendedResources += resource.getWho();
		}
		
		return appendedResources;
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
	protected ProgressReport getLatestProgressReport()
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
	
	public double getBudgetCostAllocation() throws Exception
	{
		int type = getTypeOfParent();
		ORefList parentRefs = findObjectsThatReferToUs(type); 
		if(isTask())
		{
			ORef parentRef = parentRefs.get(0);
			Task parentTask = Task.find(getObjectManager(), parentRef);
			return parentTask.getBudgetCostAllocation();
		}
		
		return 1.0 / parentRefs.size();
	}
	
	private int getTypeOfParent()
	{
		if(isTask())
			return Task.getObjectType();
		
		if(isMethod())
			return Indicator.getObjectType();
		
		if(isActivity())
			return Strategy.getObjectType();
		
		throw new RuntimeException("Unknown task type: " + getRef());
	}

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
		double cost = 0;
		IdList idList = getAssignmentIdList();
		for (int i = 0; i < idList.size(); i++)
		{
			Assignment assignment = (Assignment)getProject().findObject(ObjectType.ASSIGNMENT, idList.get(i));
			ProjectResource resource = getProjectResource(idList.get(i));
			if (resource != null)
			{
				String effortListAsString = assignment.getData(Assignment.TAG_DATERANGE_EFFORTS);
				DateRangeEffortList effortList = new DateRangeEffortList(effortListAsString);
				double totalCostPerAssignment = getTotaUnitQuantity(dateRangeToUse, resource.getCostPerUnit(), effortList);
				cost += totalCostPerAssignment;
			}
		}
		
		return cost;
	}
	
	private ProjectResource getProjectResource(BaseId assignmentId)
	{
		String stringId = getProject().getObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ASSIGNMENT_RESOURCE_ID);
		BaseId resourceId = new BaseId(stringId);
		ProjectResource resource = (ProjectResource)getProject().findObject(ObjectType.PROJECT_RESOURCE, resourceId);
		
		return resource;
	}

	private double getTotaUnitQuantity(DateRange dateRangeToUse, double costPerUnit, DateRangeEffortList effortList)
	{
		if (dateRangeToUse != null)
			return (effortList.getTotalUnitQuantity(dateRangeToUse) * costPerUnit);
		
		return (effortList.getTotalUnitQuantity() * costPerUnit);
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
				return Indicator.TAG_TASK_IDS;
		}
		
		throw new Exception("getTaskIdsTag called for non-task container type " + type);
	}

	public static Task find(ObjectManager objectManager, ORef taskRef)
	{
		return (Task) objectManager.findObject(taskRef);
	}
	
	public static Task find(Project project, ORef taskRef)
	{
		return find(project.getObjectManager(), taskRef);
	}
	
	public void clear()
	{
		super.clear();
		subtaskIds = new IdListData(TAG_SUBTASK_IDS, Task.getObjectType());
		assignmentIds = new IdListData(TAG_ASSIGNMENT_IDS, Assignment.getObjectType());
		progressReportRefs = new ORefListData(TAG_PROGRESS_REPORT_REFS);
		
		strategyLabel = new PseudoStringData(PSEUDO_TAG_STRATEGY_LABEL);
		indicatorLabel = new PseudoStringData(PSEUDO_TAG_INDICATOR_LABEL);
		taskTotal = new PseudoStringData(PSEUDO_TAG_BUDGET_TOTAL);
		who = new PseudoStringData(PSEUDO_TAG_ASSIGNED_RESOURCES_HTML);
		
		addField(TAG_SUBTASK_IDS, subtaskIds);
		addField(TAG_ASSIGNMENT_IDS, assignmentIds);
		addField(TAG_PROGRESS_REPORT_REFS, progressReportRefs);
		
		addField(PSEUDO_TAG_STRATEGY_LABEL, strategyLabel);
		addField(PSEUDO_TAG_INDICATOR_LABEL, indicatorLabel);
		addField(PSEUDO_TAG_BUDGET_TOTAL, taskTotal);
		addField(PSEUDO_TAG_ASSIGNED_RESOURCES_HTML, who);		
	}

	
	public final static String TAG_SUBTASK_IDS = "SubtaskIds";
	public final static String TAG_ASSIGNMENT_IDS = "AssignmentIds";
	public final static String TAG_PROGRESS_REPORT_REFS = "ProgressReportRefs";
	public final static String PSEUDO_TAG_STRATEGY_LABEL = "StrategyLabel";
	public final static String PSEUDO_TAG_INDICATOR_LABEL = "IndicatorLabel";
	public final static String PSEUDO_TAG_TASK_BUDGET_DETAIL = "PseudoTaskBudgetDetail";
	public final static String PSEUDO_TAG_ASSIGNED_RESOURCES_HTML = "Who";

	
	public static final String OBJECT_NAME = "Task";
	public static final String METHOD_NAME = "Method";
	public static final String ACTIVITY_NAME = "Activity";
	
	private String cachedObjectTypeName;
	
	private IdListData subtaskIds;
	private IdListData assignmentIds;
	private ORefListData progressReportRefs;
	private PseudoStringData strategyLabel;
	private PseudoStringData indicatorLabel;
	private PseudoStringData taskTotal;
	private PseudoStringData who;
}
