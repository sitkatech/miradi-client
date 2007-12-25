/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DateRange;

public class PlanniningViewBudgetTotalsCalculator
{
	public PlanniningViewBudgetTotalsCalculator(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public double getTotalUnits(Assignment assignment, DateRange dateRange) throws Exception
	{
		DateRangeEffortList effortList = getDateRangeEffortList(assignment);
		return effortList.getTotalUnitQuantity(dateRange);
	}
	
	private DateRangeEffortList getDateRangeEffortList(Assignment assignment) throws Exception
	{
		String effortListAsString = assignment.getData(Assignment.TAG_DATERANGE_EFFORTS);
		return new DateRangeEffortList(effortListAsString);
	}
	
	public double getTotalCost(Assignment assignment, DateRange dateRange) throws Exception
	{
		ProjectResource resource = getProjectResource(assignment.getId());
		if (resource == null)
			return 0.0;
	
		double totalUnits = getTotalUnits(assignment, dateRange);
		double costPerUnit = resource.getCostPerUnit();
	
		return totalUnits * costPerUnit;
	}
	
	public ProjectResource getProjectResource(BaseId assignmentId)
	{
		String stringId = project.getObjectData(ObjectType.ASSIGNMENT, assignmentId, Assignment.TAG_ASSIGNMENT_RESOURCE_ID);
		BaseId resourceId = new BaseId(stringId);
		ProjectResource resource = (ProjectResource)project.findObject(ObjectType.PROJECT_RESOURCE, resourceId);
		
		return resource;
	}
	
	public double getTaskCost(TaskId taskId) throws Exception
	{
		Task task = (Task)project.findObject(ObjectType.TASK, taskId);
		sumTotals(task, null);
		return totalCost;
	}
	
	public double getTotalTasksCost(IdList taskIds) throws Exception
	{
		double totalTaskCost = 0.0;
		for (int i = 0; i < taskIds.size(); i++)
		{
			Task task = (Task)project.findObject(ObjectType.TASK, taskIds.get(i));
			totalTaskCost += getTotalCost(task);
		}
		return totalTaskCost;
	}

	private double getProportionalizedTotalTaskCost(BaseObject baseObject, DateRange dateRange, double costAllocationPercentage) throws Exception
	{
		totalCost = 0.0;
		calculateTotalAssignment((Task) baseObject, dateRange);
		totalCost = totalCost * costAllocationPercentage;
		
		return totalCost;
	}
	
	private double getTotalCost(Task task, DateRange dateRange) throws Exception
	{
		totalCost = 0.0;
		calculateTotalAssignment(task, dateRange);
		return totalCost;
	}
	
	private double getTotalCost(Task task) throws Exception
	{
		totalCost = 0.0;
		calculateTotalAssignment(task, null);
		return totalCost;
	}
	
	private double computeTotalOfChildTasks(BaseObject baseObject, String tasksTag, DateRange dateRange) throws Exception
	{
		IdList taskIds = new IdList(Task.getObjectType(), baseObject.getData(tasksTag));
		ORefList taskRefs = new ORefList(Task.getObjectType(), taskIds);
		double totalParentCost = 0.0;
		for (int i = 0; i < taskRefs.size(); ++i)
		{
			Task task = (Task) project.findObject(taskRefs.get(i));
			double taskTotalCost = getTotalCost(task, dateRange);
			double allocationFraction = getAllocationFraction(baseObject.getRef(), task);
			totalParentCost += (taskTotalCost * allocationFraction);	
		}
		
		return totalParentCost;
	}
	
	private double getAllocationFraction(ORef parentRef, Task task)
	{
		ORefList allReferrers = task.findObjectsThatReferToUs(parentRef.getObjectType());
		return (1.0 / allReferrers.size());
	}
	
	public double calculateTotalCost(BaseObject baseObject, DateRange dateRange, double costAllocationPercentage) throws Exception
	{
		if (baseObject.getType() == ObjectType.INDICATOR)
			return computeTotalOfChildTasks(baseObject, Indicator.TAG_TASK_IDS, dateRange);

		if (baseObject.getType() == ObjectType.STRATEGY)
			return computeTotalOfChildTasks(baseObject, Strategy.TAG_ACTIVITY_IDS, dateRange);

		if (baseObject.getType() == ObjectType.TASK)
			return getProportionalizedTotalTaskCost(baseObject, dateRange, costAllocationPercentage);
		
		return  0.0;		
	}
	
	public double calculateTotalCost(ORef ref, DateRange dateRange) throws Exception
	{
		BaseObject foundObject = project.findObject(ref);
		return calculateTotalCost(foundObject, dateRange, 1.0);
	}
		
	private void calculateTotalAssignment(Task task, DateRange dateRangeToUse) throws Exception
	{
		sumTotals(task, dateRangeToUse);
		int subTaskCount = task.getSubtaskCount();
		for (int index = 0; index < subTaskCount; index++)
		{
			BaseId subTaskId = task.getSubtaskId(index);
			Task  subTask = (Task)project.findObject(ObjectType.TASK, subTaskId);
			calculateTotalAssignment(subTask, dateRangeToUse);
		}
	}
	
	private void sumTotals(Task task, DateRange dateRangeToUse) throws Exception
	{
		IdList idList = task.getAssignmentIdList();
		for (int i = 0; i < idList.size(); i++)
		{
			Assignment assignment = (Assignment)project.findObject(ObjectType.ASSIGNMENT, idList.get(i));
			ProjectResource resource = getProjectResource(idList.get(i));
			if (resource != null)
			{
				String effortListAsString = assignment.getData(Assignment.TAG_DATERANGE_EFFORTS);
				DateRangeEffortList effortList = new DateRangeEffortList(effortListAsString);
				double totalCostPerAssignment = getTotaUnitQuantity(dateRangeToUse, resource.getCostPerUnit(), effortList);
				totalCost += totalCostPerAssignment;
			}
		}
	}

	private double getTotaUnitQuantity(DateRange dateRangeToUse, double costPerUnit, DateRangeEffortList effortList)
	{
		if (dateRangeToUse != null)
			return (effortList.getTotalUnitQuantity(dateRangeToUse) * costPerUnit);
		
		return (effortList.getTotalUnitQuantity() * costPerUnit);
	}
	
	private double totalCost;
	private Project project;
}
