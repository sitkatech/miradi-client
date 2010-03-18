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

package org.miradi.objecthelpers;

import org.martus.util.MultiCalendar;
import org.miradi.ids.IdList;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.Task;
import org.miradi.utils.DateUnitEffortList;

public class TestBaseObjectRollupValues extends TestCaseWithProject
{
	public TestBaseObjectRollupValues(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		
		year2009Q1 = new DateUnit("2009Q1");
		year2009 = getProject().createDateUnit(2009);
	}
	
	public void testWorkUnitsRollupWithProjectStartEndDateSet() throws Exception
	{
		getProject().setProjectStartDate(MultiCalendar.createFromGregorianYearMonthDay(2009, 1, 1));
		getProject().setProjectEndDate(MultiCalendar.createFromGregorianYearMonthDay(2009, 12, 31));
		verifyWorkUnitsRollup();
	}

	private void verifyWorkUnitsRollup() throws Exception
	{
		Indicator indicator = getProject().createIndicatorWithCauseParent();
		Task methodWith2ResourceAssignments = getProject().createTask(indicator);
				
		getProject().addResourceAssignment(methodWith2ResourceAssignments, 13.0, year2009Q1);
		getProject().addResourceAssignment(methodWith2ResourceAssignments, 2.0, year2009);
		
		TimePeriodCostsMap timePeriodCostsMap = indicator.getTotalTimePeriodCostsMap();
		TimePeriodCosts timePeriodCostsTotal = timePeriodCostsMap.calculateTimePeriodCosts(new DateUnit());
		assertEquals("Wrong total for indicator rollup?", 15.0, timePeriodCostsTotal.getTotalWorkUnits().getValue());
		
		TimePeriodCosts timePeriodCosts2009 = timePeriodCostsMap.calculateTimePeriodCosts(year2009);
		assertEquals("wrong total for 2009?", 15.0, timePeriodCosts2009.getTotalWorkUnits().getValue());
	}
	
	public void testRollupResourceAssignmentsWithNoQuantities() throws Exception
	{
		ResourceAssignment assignment = getProject().createResourceAssignment();
		assignment.setData(ResourceAssignment.TAG_DATEUNIT_EFFORTS, new DateUnitEffortList().toString());

		Task activityWithResourceAssignment = getProject().createActivity();
		IdList currentAssignmentIdList = activityWithResourceAssignment.getResourceAssignmentIdList();
		currentAssignmentIdList.add(assignment.getId());
		activityWithResourceAssignment.setData(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, currentAssignmentIdList.toString());
		
		assertNotEquals("Ignored the resource assignment who?", 0, activityWithResourceAssignment.getTotalTimePeriodCostsMap().size());
	}
	
	public void testRollupExpenseAssignmentsWithNoQuantities() throws Exception
	{
		Task activityWithExpenseAssignment = getProject().createActivity();
		getProject().addExpenseAssignment(activityWithExpenseAssignment, new DateUnitEffortList());
		
		assertNotEquals("Ignored the expense assignment who?", 0, activityWithExpenseAssignment.getTotalTimePeriodCostsMap().size());
	}
	
	public void testRollupExpenseAssignmentsWithNoAssignments() throws Exception
	{
		Task activityWithNoAssignments = getProject().createActivity();
		assertEquals("Had a who?", 0, activityWithNoAssignments.getTotalTimePeriodCostsMap().size());
	}
	
	private DateUnit year2009Q1;
	private DateUnit year2009;
}
