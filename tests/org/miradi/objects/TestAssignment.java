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

import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.project.ProjectForTesting;
import org.miradi.project.TestDateUnit;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.OptionalDouble;

public class TestAssignment extends ObjectTestCase
{
	public TestAssignment(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		verifyFields(ObjectType.RESOURCE_ASSIGNMENT);
	}
	
	public void testGetWorkUnits() throws Exception
	{
		getProject().setSingleYearProjectDate(2008);
		ResourceAssignment assignment = getProject().createResourceAssignment();
		ProjectResource projectResource = getProject().createAndPopulateProjectResource();
		getProject().fillObjectUsingCommand(assignment, ResourceAssignment.TAG_RESOURCE_ID, projectResource.getId().toString());
		
		DateUnit dateUnit = getProject().createDateUnit(2008);
		assertFalse("Empty assignment has work unit values?", ProjectForTesting.calculateRawTimePeriodCosts(assignment, dateUnit).hasValue());

		DateUnit dateUnit1 = TestDateUnit.month12;
		DateUnit dateUnit2 = TestDateUnit.month01;
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(createDateUnitEffort(2, dateUnit1));
		dateUnitEffortList.add(createDateUnitEffort(5, dateUnit2));

		getProject().fillObjectUsingCommand(assignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toString());

		assertEquals("wrong assignment work units?", 2.0, ProjectForTesting.calculateTimePeriodCosts(assignment, dateUnit1));
		assertEquals("wrong assignment work units?", 5.0, ProjectForTesting.calculateTimePeriodCosts(assignment, dateUnit2));
		
		DateUnit totalProjectDateUnit = new DateUnit();
		assertEquals("wrong totals work units", 7.0, ProjectForTesting.calculateTimePeriodCosts(assignment, totalProjectDateUnit));
	}
	
	public void testEffortOutsideOfProjectDateRange() throws Exception
	{
		getProject().setProjectStartDate(2008);
		getProject().setProjectEndDate(2009);
		ResourceAssignment assignment = getProject().createResourceAssignment();
		ProjectResource projectResource = getProject().createAndPopulateProjectResource();
		getProject().fillObjectUsingCommand(assignment, ResourceAssignment.TAG_RESOURCE_ID, projectResource.getId().toString());
		fillAssignment(assignment, new DateUnit("2007-12"));
		assertTrue("Value outside of project date range was included", assignment.getTotalBudgetCost().hasNoValue());
		verifyResourceAssignmentCount(assignment, 0);
	}
	
	public void testEffortsInsideOfProjectDateRange() throws Exception
	{
		getProject().setProjectStartDate(2008);
		getProject().setProjectEndDate(2009);
		ResourceAssignment assignment = getProject().createResourceAssignment();
		ProjectResource projectResource = getProject().createAndPopulateProjectResource();
		getProject().fillObjectUsingCommand(assignment, ResourceAssignment.TAG_RESOURCE_ID, projectResource.getId().toString());
		fillAssignment(assignment, new DateUnit("2008-12"));
		OptionalDouble total = assignment.getTotalBudgetCost();
		assertTrue("Value within project date range was not included", total.hasValue());
		assertEquals("wrong total for assignment?", (EFFORT_QUANTIGY * projectResource.getCostPerUnit()), total.getValue());
		verifyResourceAssignmentCount(assignment, 1);
	}
	
	public void testAssignmentWithNoData() throws Exception
	{
		ResourceAssignment assignment = getProject().createResourceAssignment();
		ProjectResource projectResource = getProject().createAndPopulateProjectResource();
		getProject().fillObjectUsingCommand(assignment, ResourceAssignment.TAG_RESOURCE_ID, projectResource.getId().toString());
		verifyResourceAssignmentCount(assignment, 1);
	}

	private void verifyResourceAssignmentCount(ResourceAssignment assignment, int expectedResourcCount) throws Exception
	{
		TimePeriodCostsMap totalTimePeriodCostsMap = assignment.getTotalTimePeriodCostsMap();
		ORefSet resourceRefs = totalTimePeriodCostsMap.getAllProjectResourceRefs();
		assertEquals("wrong resource refs count?", expectedResourcCount, resourceRefs.size());
	}

	private void fillAssignment(ResourceAssignment assignment, DateUnit dateUnit) throws Exception
	{
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(createDateUnitEffort(EFFORT_QUANTIGY, dateUnit));
		getProject().fillObjectUsingCommand(assignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toString());
	}

	public DateUnitEffort createDateUnitEffort(int unitQuantatiy, DateUnit dateUnit) throws Exception
	{
		return new DateUnitEffort(dateUnit, unitQuantatiy);
	}

	private static final int EFFORT_QUANTIGY = 200;
}
