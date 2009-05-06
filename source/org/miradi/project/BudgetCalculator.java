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
package org.miradi.project;

import org.miradi.objecthelpers.DateRangeEffortList;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectResource;
import org.miradi.utils.DateRange;

public class BudgetCalculator
{
	public BudgetCalculator(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public double getTotalUnits(ResourceAssignment assignment, DateRange dateRange) throws Exception
	{
		DateRangeEffortList effortList = getDateRangeEffortList(assignment);
		return effortList.getTotalUnitQuantity(dateRange);
	}
	
	private DateRangeEffortList getDateRangeEffortList(ResourceAssignment assignment) throws Exception
	{
		String effortListAsString = assignment.getData(ResourceAssignment.TAG_DATERANGE_EFFORTS);
		return new DateRangeEffortList(effortListAsString);
	}
	
	public double getTotalCost(ResourceAssignment assignment, DateRange dateRange) throws Exception
	{
		ProjectResource resource = ProjectResource.find(project, assignment.getResourceRef());
		if (resource == null)
			return 0.0;
	
		double totalUnits = getTotalUnits(assignment, dateRange);
		double costPerUnit = resource.getCostPerUnit();
	
		return totalUnits * costPerUnit;
	}
	
	public double calculateBudgetCost(BaseObject baseObject, DateRange dateRange, double costAllocationPercentage) throws Exception
	{
		return baseObject.getBudgetCost(dateRange) * costAllocationPercentage;
	}
	
	private Project project;
}
