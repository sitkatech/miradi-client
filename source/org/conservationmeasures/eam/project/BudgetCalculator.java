/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.ids.IdList;
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
import org.conservationmeasures.eam.utils.DateRange;

public class BudgetCalculator
{
	public BudgetCalculator(Project projectToUse)
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
		ProjectResource resource = ProjectResource.find(project, assignment.getResourceRef());
		if (resource == null)
			return 0.0;
	
		double totalUnits = getTotalUnits(assignment, dateRange);
		double costPerUnit = resource.getCostPerUnit();
	
		return totalUnits * costPerUnit;
	}
	
	private double getProportionalizedTotalOfChildTasks(BaseObject baseObject, String tasksTag, DateRange dateRange) throws Exception
	{
		IdList taskIds = new IdList(Task.getObjectType(), baseObject.getData(tasksTag));
		ORefList taskRefs = new ORefList(Task.getObjectType(), taskIds);
		double totalParentCost = 0.0;
		for (int i = 0; i < taskRefs.size(); ++i)
		{
			Task task = (Task) project.findObject(taskRefs.get(i));
			double taskTotalCost = task.getBudgetCost(dateRange);
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
	
	public double calculateBudgetCost(BaseObject baseObject, DateRange dateRange, double costAllocationPercentage) throws Exception
	{
		if (baseObject.getType() == ObjectType.INDICATOR)
			return getProportionalizedTotalOfChildTasks(baseObject, Indicator.TAG_TASK_IDS, dateRange);

		if (baseObject.getType() == ObjectType.STRATEGY)
			return getProportionalizedTotalOfChildTasks(baseObject, Strategy.TAG_ACTIVITY_IDS, dateRange);

		if (baseObject.getType() == ObjectType.TASK)
		{
			Task task = (Task) baseObject;
			return task.getBudgetCost(dateRange) * costAllocationPercentage;
		}
		
		return  0.0;		
	}
	
	public double calculateBudgetCost(ORef ref, DateRange dateRange) throws Exception
	{
		BaseObject foundObject = project.findObject(ref);
		return calculateBudgetCost(foundObject, dateRange, 1.0);
	}
		
	private Project project;
}
