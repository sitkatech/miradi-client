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

import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.ProjectResource;
import org.miradi.utils.DateRange;

public class TestTimePeriodCostsMap extends TestCaseWithProject
{
	public TestTimePeriodCostsMap(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		
		dateUnitTotal = new DateUnit("");
		dateUnit2009 = createSingleYearDateUnit(2009);
		dateUnit2008 = createSingleYearDateUnit(2008);
		dateUnit2007 = createSingleYearDateUnit(2007);
		year2009Q1 = new DateUnit("2009Q1");
	}
	
	public void testBasics() throws Exception
	{
		getProject().setProjectDates(2005, 2011);
		TimePeriodCostsMap timePeriodCostsMap = new TimePeriodCostsMap();
		assertTrue("time period costs map is not empty?", timePeriodCostsMap.isEmpty());
		
		ProjectResource projectResource = createProjectResource();
		TimePeriodCosts timePeriodCosts1 = getProject().createTimePeriodCosts(500.0, projectResource.getRef(), 10.0);
		TimePeriodCosts timePeriodCosts2 = getProject().createTimePeriodCosts(600.0, projectResource.getRef(), 20.0);
		
		DateUnit dateUnit1 = createSingleYearDateUnit(2008);
		DateUnit dateUnit2 = createSingleYearDateUnit(2009);
		timePeriodCostsMap.add(dateUnit1, timePeriodCosts1);
		timePeriodCostsMap.add(dateUnit2, timePeriodCosts2);
		
		verifyGetTimePeriodCostsForSpecificDateUnit(timePeriodCostsMap, timePeriodCosts1, dateUnit1);
		verifyGetTimePeriodCostsForSpecificDateUnit(timePeriodCostsMap, timePeriodCosts2, dateUnit2);
		
		verifyRolledUpDates(timePeriodCostsMap, "2008-01-01", "2009-12-31");
		
		TimePeriodCosts timePeriodCosts = getProject().createTimePeriodCosts(500.0, projectResource.getRef(), 10.0);
		timePeriodCostsMap.add(new DateUnit(), timePeriodCosts);
		verifyRolledUpDates(timePeriodCostsMap, "2005-01-01", "2011-12-31");
	}

	private void verifyRolledUpDates(TimePeriodCostsMap timePeriodCostsMap, String expectedStartDate, String expectedEndDate) throws Exception
	{
		final DateRange projectStartEndDateRange = getProject().getProjectCalendar().getProjectPlanningDateRange();
		DateRange rolledUpDates = timePeriodCostsMap.getRolledUpDateRange(projectStartEndDateRange);
		assertEquals("wrong rolled up end date? ", expectedStartDate, rolledUpDates.getStartDate().toIsoDateString());
		assertEquals("wrong rolled up end date? ", expectedEndDate, rolledUpDates.getEndDate().toIsoDateString());
	}

	private void verifyGetTimePeriodCostsForSpecificDateUnit(TimePeriodCostsMap timePeriodCostsMap, TimePeriodCosts timePeriodCosts, DateUnit dateUnit)
	{
		TimePeriodCosts foundTimePeriodCosts = timePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnit);
		assertEquals("Single TPC wasn't found?", foundTimePeriodCosts, timePeriodCosts);
	}
	
	public void testMergeAddWithEmpty() throws Exception
	{
		ProjectResource resource = createProjectResource();
		double expenses = 1.0;
		double workUnits = 2.0;
		double resourceCosts = workUnits * resource.getCostPerUnit();
		
		TimePeriodCostsMap timePeriodCostsMap = createTimePeriodCostsMap(expenses, resource, workUnits);

		verifyMergeAdd(expenses, resourceCosts, new TimePeriodCostsMap(), timePeriodCostsMap);
		verifyMergeAdd(expenses, resourceCosts, timePeriodCostsMap, new TimePeriodCostsMap());
	}

	public void testMergeAddWithDifferentResrouce() throws Exception
	{
		ProjectResource resource1 = createProjectResource();
		ProjectResource resource2 = createProjectResource();
		double expenses1 = 1.0;
		double expenses2 = 2.0;
		double workUnits1 = 4.0;
		double workUnits2 = 8.0;
		double resourceCosts1 = workUnits1 * resource1.getCostPerUnit();
		double resourceCosts2 = workUnits2 * resource2.getCostPerUnit();

		TimePeriodCostsMap timePeriodCostsMap1 = createTimePeriodCostsMap(expenses1, resource1, workUnits1);
		TimePeriodCostsMap timePeriodCostsMap2 = createTimePeriodCostsMap(expenses2, resource2, workUnits2);
		
		verifyMergeAdd(expenses1+expenses2, resourceCosts1+resourceCosts2, timePeriodCostsMap1, timePeriodCostsMap2);
	}
	
	public void testMergeAddWithSameResource() throws Exception
	{
		ProjectResource resource = createProjectResource();
		double expenses1 = 1.0;
		double expenses2 = 2.0;
		double workUnits1 = 4.0;
		double workUnits2 = 8.0;
		double resourceCosts = (workUnits1 + workUnits2) * resource.getCostPerUnit();

		TimePeriodCostsMap timePeriodCostsMap1 = createTimePeriodCostsMap(expenses1, resource, workUnits1);
		TimePeriodCostsMap timePeriodCostsMap2 = createTimePeriodCostsMap(expenses2, resource, workUnits2);
		
		verifyMergeAdd(expenses1+expenses2, resourceCosts, timePeriodCostsMap1, timePeriodCostsMap2);
	}
	
	public void testMergeAddDifferentDates() throws Exception
	{
		ProjectResource resource = createProjectResource();
		double expenses1 = 1.0;
		double expenses2 = 2.0;
		double workUnits = 4.0;
		double resourceCosts1 = workUnits * resource.getCostPerUnit();

		DateUnit q1 = dateUnit2008.getSubDateUnits().get(0);
		DateUnit q2 = dateUnit2008.getSubDateUnits().get(1);
		TimePeriodCostsMap timePeriodCostsMap1 = createTimePeriodCostsMap(q1, expenses1, resource, workUnits);
		TimePeriodCostsMap timePeriodCostsMap2 = createTimePeriodCostsMap(q2, expenses2);
		TimePeriodCostsMap mergedTimePeriodCostsMap = new TimePeriodCostsMap();
		
		verifyMergeAdd(expenses1, resourceCosts1, mergedTimePeriodCostsMap, timePeriodCostsMap1, q1);
		verifyMergeAdd(expenses2, 0.0, mergedTimePeriodCostsMap, timePeriodCostsMap2, q2);
		assertEquals(expenses1+expenses2, mergedTimePeriodCostsMap.calculateTimePeriodCosts(dateUnit2008).getTotalExpense().getValue());
		assertEquals(workUnits, mergedTimePeriodCostsMap.calculateTimePeriodCosts(dateUnit2008).getResourceWorkUnits(resource.getRef()).getValue());
	}

	public void testMergeAddingIncompletedMaps() throws Exception
	{
		ProjectResource resource = createProjectResource();
		double expenses = 1.0;
		double workUnits = 2.0;
		double resourceCosts = workUnits * resource.getCostPerUnit();

		TimePeriodCostsMap mapWithOnlyExpenses = createTimePeriodCostsMap(dateUnit2008, expenses);
		TimePeriodCostsMap mapWithOnlyResourceWorkUnits = createTimePeriodCostsMap(dateUnit2008, resource.getRef(), workUnits);
		
		verifyMergeAdd(expenses, resourceCosts, mapWithOnlyExpenses, mapWithOnlyResourceWorkUnits);
	}
	
	private void verifyMergeAdd(double expectedExpense, double expectedResourcesCost, TimePeriodCostsMap destinationTimePeriodCostsMap, TimePeriodCostsMap timePeriodCostsMapToBeMerged) throws Exception
	{
		verifyMergeAdd(expectedExpense,expectedResourcesCost, destinationTimePeriodCostsMap, timePeriodCostsMapToBeMerged,dateUnit2008);
	}

	private void verifyMergeAdd(double expectedExpense, double expectedResourcesCost, TimePeriodCostsMap destinationTimePeriodCostsMap, TimePeriodCostsMap timePeriodCostsMapToBeMerged, DateUnit dateUnitForTimePeriodCosts) throws Exception
	{
		double expectedTotalCost = expectedExpense + expectedResourcesCost;
		destinationTimePeriodCostsMap.mergeAll(timePeriodCostsMapToBeMerged);
		TimePeriodCosts  foundTimePeriodCosts = destinationTimePeriodCostsMap.calculateTimePeriodCosts(dateUnitForTimePeriodCosts);
		assertEquals("wrong expense after merge?", expectedExpense, foundTimePeriodCosts.getTotalExpense().getValue());
		assertEquals("wrong total cost after merge?", expectedTotalCost, foundTimePeriodCosts.calculateTotalCost(getProject()).getValue());
	}
	
	public void testMergeNonConflictingExpenses() throws Exception
	{
		TimePeriodCostsMap strategyTimePeriodCostsMap = createTimePeriodCostsMap(dateUnitTotal, 2.0);
		TimePeriodCostsMap activityTimePeriodCostsMap = createTimePeriodCostsMap(dateUnit2007, 1.0);
		activityTimePeriodCostsMap.mergeNonConflicting(strategyTimePeriodCostsMap);
		verifyMergedExpense(1.0, activityTimePeriodCostsMap, dateUnitTotal);
		verifyMergedExpense(1.0, activityTimePeriodCostsMap, dateUnit2007);
		
		TimePeriodCostsMap strategyTimePeriodCostsMap2 = createTimePeriodCostsMap(dateUnit2008, 2.0);
		TimePeriodCostsMap activityTimePeriodCostsMap2 = createTimePeriodCostsMap(dateUnit2007, 2.0);
		activityTimePeriodCostsMap2.mergeNonConflicting(strategyTimePeriodCostsMap2);
		verifyMergedExpense(2.0, activityTimePeriodCostsMap2, dateUnit2008);
		verifyMergedExpense(2.0, activityTimePeriodCostsMap2, dateUnit2007);
		
		TimePeriodCostsMap strategyTimePeriodCostsMap3 = createTimePeriodCostsMap(dateUnit2008, 1.0);
		TimePeriodCostsMap activityTimePeriodCostsMap3 = createTimePeriodCostsMap(dateUnitTotal, 2.0);
		activityTimePeriodCostsMap3.mergeNonConflicting(strategyTimePeriodCostsMap3);
		verifyMergedExpense(1.0, activityTimePeriodCostsMap3, dateUnit2008);
		verifyMergedExpense(3.0, activityTimePeriodCostsMap3, dateUnitTotal);
	}
	
	private void verifyMergedExpense(double expectedExpense, TimePeriodCostsMap timePeriodCostsMap, DateUnit dateUnit) throws Exception
	{
		TimePeriodCosts timePeriodCosts = timePeriodCostsMap.calculateTimePeriodCosts(dateUnit);
		assertEquals("wrong expense after merge?", expectedExpense, timePeriodCosts.getTotalExpense().getValue());			
	}
	
	public void testMergeNonConlictingWorkUnits() throws Exception
	{	
		ProjectResource fred = createProjectResource();
		TimePeriodCostsMap strategyTimePeriodCostsMap = createTimePeriodCostsMap(dateUnitTotal, fred.getRef(), 2.0);
		TimePeriodCostsMap activityTimePeriodCostsMap = createTimePeriodCostsMap(dateUnit2007, fred.getRef(), 1.0);
		activityTimePeriodCostsMap.mergeNonConflicting(strategyTimePeriodCostsMap);
		verifyMergedWorkUnits(1.0, activityTimePeriodCostsMap, dateUnitTotal);
		verifyMergedWorkUnits(1.0, activityTimePeriodCostsMap, dateUnit2007);
		
		TimePeriodCostsMap strategyTimePeriodCostsMap2 = createTimePeriodCostsMap(dateUnit2008, fred.getRef(), 2.0);
		TimePeriodCostsMap activityTimePeriodCostsMap2 = createTimePeriodCostsMap(dateUnit2007, fred.getRef(), 2.0);
		activityTimePeriodCostsMap2.mergeNonConflicting(strategyTimePeriodCostsMap2);
		verifyMergedWorkUnits(2.0, activityTimePeriodCostsMap2, dateUnit2008);
		verifyMergedWorkUnits(2.0, activityTimePeriodCostsMap2, dateUnit2007);
		
		TimePeriodCostsMap strategyTimePeriodCostsMap3 = createTimePeriodCostsMap(dateUnit2008, fred.getRef(), 1.0);
		TimePeriodCostsMap activityTimePeriodCostsMap3 = createTimePeriodCostsMap(dateUnitTotal, fred.getRef(), 2.0);
		activityTimePeriodCostsMap3.mergeNonConflicting(strategyTimePeriodCostsMap3);
		verifyMergedWorkUnits(1.0, activityTimePeriodCostsMap3, dateUnit2008);
		verifyMergedWorkUnits(3.0, activityTimePeriodCostsMap3, dateUnitTotal);
		
	}
	
	private void verifyMergedWorkUnits(double expectedWorkUnit, TimePeriodCostsMap mergedTimePeriodCostsMap, DateUnit dateUnit) throws Exception
	{
		TimePeriodCosts timePeriodCosts = mergedTimePeriodCostsMap.calculateTimePeriodCosts(dateUnit);
		assertEquals("wrong total workUnits?", expectedWorkUnit, timePeriodCosts.getTotalWorkUnits().getValue());
	}
	
	public void testMergeNonConflictingExpenseAndWorkUnits() throws Exception
	{
		ProjectResource fred = createProjectResource();
		TimePeriodCostsMap workUnitsTimePeriodCostsMap = createTimePeriodCostsMap(dateUnit2007, fred.getRef(), 1.0);
		TimePeriodCostsMap expenseTimePeriodCostsMap = createTimePeriodCostsMap(dateUnit2007, 13.0);
		workUnitsTimePeriodCostsMap.mergeNonConflicting(expenseTimePeriodCostsMap);
		
		TimePeriodCosts timePeriodCosts = workUnitsTimePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnit2007);
		assertEquals("wrong expense after megrge?", 13.0, timePeriodCosts.getTotalExpense().getValue());
		assertEquals("wrong work untis after merge?", 1.0, timePeriodCosts.getResourceWorkUnits(fred.getRef()).getValue());
	}
	
	public void testMergeNonConflictingSameResourceSameYear() throws Exception
	{
		ProjectResource fred = createProjectResource();
		TimePeriodCostsMap timePeriodCostsMap1 = createTimePeriodCostsMap(dateUnit2007, fred.getRef(), 1.0);
		TimePeriodCostsMap timePeriodCostsMap2 = createTimePeriodCostsMap(dateUnit2007, fred.getRef(), 2.0);
		timePeriodCostsMap2.mergeNonConflicting(timePeriodCostsMap1);
		TimePeriodCosts timePeriodCosts2007AfterMerge = timePeriodCostsMap2.getTimePeriodCostsForSpecificDateUnit(dateUnit2007);
		assertEquals("wrong work units for resource?", 2.0, timePeriodCosts2007AfterMerge.getTotalWorkUnits().getValue());
	}
	
	public void testMultipleAssignmentsWithinSameTimePeriodCostsMapMerge() throws Exception
	{
		ProjectResource fred = createProjectResource();
		ProjectResource jill = createProjectResource();
		TimePeriodCostsMap timePeriodCostsMap1 = createTimePeriodCostsMap(year2009Q1, fred.getRef(), 13.0);
		TimePeriodCosts jillTimePeriodCosts = getProject().createTimePeriodCosts(jill.getRef(), 2.0);
		timePeriodCostsMap1.add(dateUnit2009, jillTimePeriodCosts);
		
		TimePeriodCostsMap timePeriodCostsMap = new TimePeriodCostsMap();
		timePeriodCostsMap.mergeNonConflicting(timePeriodCostsMap1);
		
		TimePeriodCosts timePeriodCosts = timePeriodCostsMap.calculateTimePeriodCosts(dateUnitTotal);
		assertEquals("wrong work units?", 15.0, timePeriodCosts.getTotalWorkUnits().getValue());
	}
	
	private TimePeriodCostsMap createTimePeriodCostsMap(double expense, ProjectResource projectResource, double units)
	{
		return createTimePeriodCostsMap(dateUnit2008, expense, projectResource, units);
	}
	
	private TimePeriodCostsMap createTimePeriodCostsMap(DateUnit dateUnitToUse, double expense, ProjectResource projectResource, double units)
	{
		return createTimePeriodCostsMap(dateUnitToUse, expense, projectResource.getRef(), units);
	}
	
	private TimePeriodCostsMap createTimePeriodCostsMap(DateUnit dateUnitToUse, double expense, ORef projectResourceRef, double units)
	{
		TimePeriodCosts timePeriodCosts = getProject().createTimePeriodCosts(expense, projectResourceRef, units);
		TimePeriodCostsMap timePeriodCostsMap = new TimePeriodCostsMap();
		timePeriodCostsMap.add(dateUnitToUse, timePeriodCosts);
		
		return timePeriodCostsMap;
	}
	
	private TimePeriodCostsMap createTimePeriodCostsMap(DateUnit dateUnitToUse, ORef projectResourceRef, double units)
	{
		TimePeriodCosts timePeriodCosts = getProject().createTimePeriodCosts(projectResourceRef, units);
		TimePeriodCostsMap timePeriodCostsMap = new TimePeriodCostsMap();
		timePeriodCostsMap.add(dateUnitToUse, timePeriodCosts);
		
		return timePeriodCostsMap;
	}
	
	private TimePeriodCostsMap createTimePeriodCostsMap(DateUnit dateUnitToUse, double expense)
	{
		TimePeriodCostsMap timePeriodCostsMap = new TimePeriodCostsMap();
		TimePeriodCosts timePeriodCosts = getProject().createTimePeriodCosts(expense);
		timePeriodCostsMap.add(dateUnitToUse, timePeriodCosts);
		
		return timePeriodCostsMap;
	}
		
	private ProjectResource createProjectResource() throws Exception
	{
		ProjectResource projectResource = getProject().createAndPopulateProjectResource();
		getProject().fillCostPerUnitField(projectResource, "10");
		return projectResource;
	}	

	private DateUnit createSingleYearDateUnit(int year) throws Exception
	{
		return getProject().createSingleYearDateUnit(year);
	}

	private DateUnit dateUnit2009;
	private DateUnit dateUnit2008;
	private DateUnit dateUnit2007;
	private DateUnit dateUnitTotal;
	private DateUnit year2009Q1;
}
