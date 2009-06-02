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
import org.miradi.project.TestDateUnit;
import org.miradi.utils.DateRange;
import org.miradi.utils.OptionalDouble;

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
		
		dateUnit2008 = getProject().createSingleYearDateUnit(2008);	
	}
	
	public void testBasics() throws Exception
	{
		getProject().setProjectDates(2005, 2011);
		TimePeriodCostsMap timePeriodCostsMap = new TimePeriodCostsMap();
		assertTrue("time period costs map is not empty?", timePeriodCostsMap.isEmpty());
		
		ProjectResource projectResource = createProjectResource();
		TimePeriodCosts timePeriodCosts1 = getProject().createTimePeriodCosts(500.0, projectResource.getRef(), 10.0);
		TimePeriodCosts timePeriodCosts2 = getProject().createTimePeriodCosts(600.0, projectResource.getRef(), 20.0);
		
		DateUnit dateUnit1 = getProject().createSingleYearDateUnit(2008);
		DateUnit dateUnit2 = getProject().createSingleYearDateUnit(2009);
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
		final DateRange projectStartEndDateRange = getProject().getProjectCalendar().getProjectStartEndDateRange();
		DateRange rolledUpDates = timePeriodCostsMap.getRolledUpDateRange(projectStartEndDateRange);
		assertEquals("wrong rolled up end date? ", expectedStartDate, rolledUpDates.getStartDate().toIsoDateString());
		assertEquals("wrong rolled up end date? ", expectedEndDate, rolledUpDates.getEndDate().toIsoDateString());
	}

	private void verifyGetTimePeriodCostsForSpecificDateUnit(TimePeriodCostsMap timePeriodCostsMap, TimePeriodCosts timePeriodCosts, DateUnit dateUnit)
	{
		TimePeriodCosts foundTimePeriodCosts = timePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnit);
		assertEquals("Single TPC wasn't found?", foundTimePeriodCosts, timePeriodCosts);
	}
	
	public void testMergeOverlayWithOverlappingDateUnits() throws Exception
	{
		ProjectResource projectResourcePaul = createProjectResource();
		DateUnit dateUnit2007 = getProject().createSingleYearDateUnit(2007);
		TimePeriodCostsMap timePeriodCostsMap2007 = createTimePeriodCostsMap(dateUnit2007, 22.0, projectResourcePaul, 12.0);
		DateUnit smallerDateUnit = dateUnit2007.getSubDateUnits().get(0);
		TimePeriodCostsMap timePeriodCostsMap2007Q1 = createTimePeriodCostsMap(smallerDateUnit, 23.0, projectResourcePaul, 11.0);
		
		verifyMergeOverlay(23, timePeriodCostsMap2007, timePeriodCostsMap2007Q1);
		verifyMergeOverlay(23, timePeriodCostsMap2007Q1, timePeriodCostsMap2007);
	}

	private void verifyMergeOverlay(double expectedExpenses, TimePeriodCostsMap timePeriodCostsMap1, TimePeriodCostsMap timePeriodCostsMap2) throws Exception
	{
		TimePeriodCostsMap overlaidMap = new TimePeriodCostsMap();
		
		overlaidMap.mergeOverlay(timePeriodCostsMap1);
		assertEquals("wrong size after merging map?", 1, overlaidMap.size());
		
		overlaidMap.mergeOverlay(timePeriodCostsMap2);
		assertEquals("wrong size after merging map?", 1, overlaidMap.size());
		
		assertEquals(expectedExpenses, overlaidMap.calculateTimePeriodCosts(new DateUnit("")).getExpense().getValue());
	}

	public void testMergeOverlay() throws Exception
	{	
		DateUnit dateUnit2006 = getProject().createSingleYearDateUnit(2006);
		DateUnit dateUnit2007 = getProject().createSingleYearDateUnit(2007);
		DateUnit dateUnit2007Q1 = new DateUnit("2007Q1");
		
		ProjectResource projectResourcePaul = createProjectResource();
		
		TimePeriodCostsMap timePeriodCostsMap2006 = new TimePeriodCostsMap();
		TimePeriodCosts timePeriodCosts2006 = updateMapWithNewCreatedTimePeriodCosts(timePeriodCostsMap2006, dateUnit2006, 22.0, projectResourcePaul, 10.0);
		
		TimePeriodCostsMap timePeriodCostsMap2007 = new TimePeriodCostsMap();
		TimePeriodCosts timePeriodCosts2007 = updateMapWithNewCreatedTimePeriodCosts(timePeriodCostsMap2007, dateUnit2007, 22.0, projectResourcePaul, 12.0);
		TimePeriodCosts timePeriodCosts2007Q1 = updateMapWithNewCreatedTimePeriodCosts(timePeriodCostsMap2007, dateUnit2007Q1, 23.0, projectResourcePaul, 11.0);
		
		assertEquals("wrong expense?", 22.0, timePeriodCosts2007.getExpense().getValue());
		assertEquals("wrong calculated project resource?", 120.0 + 22.0, timePeriodCosts2007.calculateTotalCost(getProject()).getValue());
		
		TimePeriodCostsMap projectTimePeriodCostsMap = new TimePeriodCostsMap();
		projectTimePeriodCostsMap.mergeOverlay(timePeriodCostsMap2006);
		assertEquals("wrong content count after merge overlay?", 1, projectTimePeriodCostsMap.size());
		TimePeriodCosts specificTimePeriodCostsFor2006 = projectTimePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnit2006);
		assertEquals("Merging larger unit changed existing data?", timePeriodCosts2006, specificTimePeriodCostsFor2006);
		
		projectTimePeriodCostsMap.mergeOverlay(timePeriodCostsMap2007);
		assertEquals("wrong content count after merge overlay?", 2, projectTimePeriodCostsMap.size());
		assertTrue("time period costs map does not contain dateUnit as key?", projectTimePeriodCostsMap.containsSpecificDateUnit(dateUnit2007Q1));
		TimePeriodCosts specificTimePeriodCostsFor2007Q1 = projectTimePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnit2007Q1);
		assertEquals("Merging larger unit changed existing data?", timePeriodCosts2007Q1, specificTimePeriodCostsFor2007Q1);
		
		TimePeriodCostsMap timePeriodCostsMapSecond2007Q1 = new TimePeriodCostsMap();
		ProjectResource projectResourceJon = createProjectResource();
		updateMapWithNewCreatedTimePeriodCosts(timePeriodCostsMapSecond2007Q1, dateUnit2007Q1, 25.0, projectResourceJon, 15.0);
		
		projectTimePeriodCostsMap.mergeOverlay(timePeriodCostsMapSecond2007Q1);
		TimePeriodCosts timePeriodCostsAfterOverlay = specificTimePeriodCostsFor2007Q1;
		assertEquals("wrong expense after merge overlay?", (23.0 + 25.0), timePeriodCostsAfterOverlay.getExpense().getValue());
		
		OptionalDouble projectResourceCost = timePeriodCostsAfterOverlay.calculateTotalCost(getProject());
		assertEquals("wrong project resource cost?", (150.0 + 110.0) + 48.0, projectResourceCost.getValue());
	}

	private TimePeriodCosts updateMapWithNewCreatedTimePeriodCosts(TimePeriodCostsMap timePeriodCostsMap, DateUnit dateUnit, double expense, ProjectResource projectResource, double units)
	{
		TimePeriodCosts timePeriodCosts = createTimePeriodCosts(expense, projectResource, units);
		timePeriodCostsMap.add(dateUnit, timePeriodCosts);
		assertEquals(timePeriodCosts, timePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnit));
		
		return timePeriodCosts;
	}

	private TimePeriodCosts createTimePeriodCosts(double expenses, ProjectResource projectResource, double units)
	{
		return getProject().createTimePeriodCosts(expenses, projectResource.getRef(), units);
	}

	public void testSingleAdd() throws Exception
	{
		TimePeriodCostsMap timePeriodCostsMap = createTimePeriodCostsMap(1.0, createProjectResource(), 2.0);
		verifyMerge(new TimePeriodCostsMap(), timePeriodCostsMap, 1.0, 20.0 + 1.0);
		verifyMerge(timePeriodCostsMap, new TimePeriodCostsMap(), 1.0, 20.0 + 1.0);
	}

	public void testMergeAddWithDifferentResrouce() throws Exception
	{
		TimePeriodCostsMap timePeriodCostsMap1 = createTimePeriodCostsMap(1.0, createProjectResource(), 2.0);
		TimePeriodCostsMap timePeriodCostsMap2 = createTimePeriodCostsMap(1.0, createProjectResource(), 1.0);
		
		verifyMerge(timePeriodCostsMap1, timePeriodCostsMap2, 2.0, 30.0 + 2.0);
	}
	
	public void testMergeAddWithSameResource() throws Exception
	{
		ProjectResource projectResource = createProjectResource();
		TimePeriodCostsMap timePeriodCostsMap1 = createTimePeriodCostsMap(1.0, projectResource, 2.0);
		TimePeriodCostsMap timePeriodCostsMap2 = createTimePeriodCostsMap(3.0, projectResource, 3.0);
		
		verifyMerge(timePeriodCostsMap1, timePeriodCostsMap2, 4.0, 50.0 + 4.0);
	}
	
	public void testMergeDifferentDates() throws Exception
	{
		TimePeriodCostsMap timePeriodCostsMap1 = createTimePeriodCostsMap(TestDateUnit.month01, 1.0, createProjectResource(), 2.0);
		TimePeriodCostsMap timePeriodCostsMap2 = createTimePeriodCostsMap(TestDateUnit.month12, 3.0);
		
		TimePeriodCostsMap mergedTimePeriodCostsMap = new TimePeriodCostsMap();
		verifyMerge(mergedTimePeriodCostsMap, timePeriodCostsMap1, TestDateUnit.month01, 1.0, 20.0 + 1.0);
		verifyMerge(mergedTimePeriodCostsMap, timePeriodCostsMap2, TestDateUnit.month12, 3.0, 3.0);
	}

	public void testMergeAddingIncompletedMaps() throws Exception
	{
		ProjectResource projectResource = createProjectResource();
		TimePeriodCostsMap mapWithOnlyExpenses = createTimePeriodCostsMap(dateUnit2008, 10.0);
		TimePeriodCostsMap mapWithOnlyResourceWorkUnits = createTimePeriodCostsMap(dateUnit2008, projectResource.getRef(), 3.0);
		
		verifyMerge(mapWithOnlyExpenses, mapWithOnlyResourceWorkUnits, 10.0, ((3.0 * 10.0) + 10.0));
	}
	
	private void verifyMerge(TimePeriodCostsMap destinationTimePeriodCostsMap, TimePeriodCostsMap timePeriodCostsMapToBeMerged, double expectedExpense, double expectedTotalCost)
	{
		verifyMerge(destinationTimePeriodCostsMap,timePeriodCostsMapToBeMerged, dateUnit2008, expectedExpense,expectedTotalCost);
	}

	private void verifyMerge(TimePeriodCostsMap destinationTimePeriodCostsMap, TimePeriodCostsMap timePeriodCostsMapToBeMerged, DateUnit dateUnitForTimePeriodCosts,	double expectedExpense, double expectedTotalCost)
	{
		destinationTimePeriodCostsMap.mergeAdd(timePeriodCostsMapToBeMerged);
		TimePeriodCosts  foundTimePeriodCosts = destinationTimePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnitForTimePeriodCosts);
		assertEquals("wrong expense after merge?", expectedExpense, foundTimePeriodCosts.getExpense().getValue());
		assertEquals("wrong total cost after merge?", expectedTotalCost, foundTimePeriodCosts.calculateTotalCost(getProject()).getValue());
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
		TimePeriodCosts timePeriodCosts = getProject().createTimePeriodCosts(expense);
		TimePeriodCostsMap timePeriodCostsMap = new TimePeriodCostsMap();
		timePeriodCostsMap.add(dateUnitToUse, timePeriodCosts);
		
		return timePeriodCostsMap;
	}
		
	private ProjectResource createProjectResource() throws Exception
	{
		ProjectResource projectResource = getProject().createAndPopulateProjectResource();
		getProject().fillCostPerUnitField(projectResource, "10");
		return projectResource;
	}	

	private DateUnit dateUnit2008;
}
