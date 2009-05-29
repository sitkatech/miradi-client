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
	
	public void testBasics() throws Exception
	{
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
		
		DateRange rolledUpDates = timePeriodCostsMap.getRolledUpDates();
		assertEquals("wrong rolled up start date? ", "2008-01-01", rolledUpDates.getStartDate().toIsoDateString());
		assertEquals("wrong rolled up end date? ", "2009-12-31", rolledUpDates.getEndDate().toIsoDateString());
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
		TimePeriodCostsMap timePeriodCostsMap2007 = new TimePeriodCostsMap();
		updateMapWithNewCreatedTimePeriodCosts(timePeriodCostsMap2007, dateUnit2007, 22.0, projectResourcePaul, 12.0);
		
		DateUnit dateUnit2007Q1 = new DateUnit("2007Q1");
		TimePeriodCostsMap timePeriodCostsMap2007Q1 = new TimePeriodCostsMap();
		updateMapWithNewCreatedTimePeriodCosts(timePeriodCostsMap2007Q1, dateUnit2007Q1, 23.0, projectResourcePaul, 11.0);
		
		verifyMergeOverlay(timePeriodCostsMap2007, timePeriodCostsMap2007Q1);
		verifyMergeOverlay(timePeriodCostsMap2007Q1, timePeriodCostsMap2007);
	}

	private void verifyMergeOverlay(TimePeriodCostsMap timePeriodCostsMap1, TimePeriodCostsMap timePeriodCostsMap2) throws Exception
	{
		TimePeriodCostsMap overlaidMap = new TimePeriodCostsMap();
		
		overlaidMap.mergeOverlay(timePeriodCostsMap1);
		assertEquals("wrong size after merging map?", 1, overlaidMap.size());
		
		overlaidMap.mergeOverlay(timePeriodCostsMap2);
		assertEquals("wrong size after merging map?", 1, overlaidMap.size());
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
		assertEquals("wrong calculated project resource?", 120.0, timePeriodCosts2007.calculateResourceCosts(getProject()).getValue());
		
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
		
		OptionalDouble projectResourceCost = timePeriodCostsAfterOverlay.calculateResourceCosts(getProject());
		assertEquals("wrong project resource cost?", (150.0 + 110.0), projectResourceCost.getValue());
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

	public void testMergeAdd() throws Exception
	{
		ProjectResource projectResource = createProjectResource();
		TimePeriodCosts timePeriodCosts1 = getProject().createTimePeriodCosts(1.0, projectResource.getRef(), 2.0);
		TimePeriodCostsMap timePeriodCostsMap1 = new TimePeriodCostsMap();
		DateUnit dateUnit2008 = getProject().createSingleYearDateUnit(2008);
		timePeriodCostsMap1.add(dateUnit2008, timePeriodCosts1);
		
		TimePeriodCosts timePeriodCosts2 = getProject().createTimePeriodCosts(2.0, projectResource.getRef(), 3.0);
		TimePeriodCostsMap timePerdiodCostsMap2 = new TimePeriodCostsMap();
		timePerdiodCostsMap2.add(dateUnit2008, timePeriodCosts2);
		
		TimePeriodCostsMap mergedTimePeriodCostsMap = new TimePeriodCostsMap();
		mergedTimePeriodCostsMap.mergeAdd(timePerdiodCostsMap2);
		TimePeriodCosts timePeriodCosts3 = mergedTimePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnit2008);
		assertEquals("wrong expense?", 2.0, timePeriodCosts3.getExpense().getValue());
		assertEquals("wrong unit cost?", 30.0, timePeriodCosts3.calculateResourceCosts(getProject()).getValue());
		
		mergedTimePeriodCostsMap.mergeAdd(timePeriodCostsMap1);		
		TimePeriodCosts timePeriodCosts4 = mergedTimePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnit2008);
		assertEquals("wrong expense?", 3.0, timePeriodCosts4.getExpense().getValue());
		assertEquals("wrong unit cost?", 50.0, timePeriodCosts4.calculateResourceCosts(getProject()).getValue());
		
		verifyMergeAddingIncompletedMaps(projectResource, dateUnit2008);
		verifyMergeDifferentDates();
	}

	private void verifyMergeDifferentDates() throws Exception
	{
		ProjectResource projectResource = createProjectResource();
		TimePeriodCosts timePeriodCosts1 = getProject().createTimePeriodCosts(1.0, projectResource.getRef(), 2.0);
		TimePeriodCostsMap timePeriodCostsMap1 = new TimePeriodCostsMap();
		timePeriodCostsMap1.add(TestDateUnit.month01, timePeriodCosts1);
		
		TimePeriodCosts timePeriodCosts2 = getProject().createTimePeriodCosts(3.0, ORef.INVALID, 0.0);
		TimePeriodCostsMap timePeriodCostsMap2 = new TimePeriodCostsMap();
		timePeriodCostsMap2.add(TestDateUnit.month12, timePeriodCosts2);
		
		TimePeriodCostsMap mergedTimePeriodCostsMap = new TimePeriodCostsMap();
		mergedTimePeriodCostsMap.mergeAdd(timePeriodCostsMap1);
		TimePeriodCosts foundTimePeriodCosts1 = mergedTimePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(TestDateUnit.month01);
		assertEquals("wrong expense for dateunit", 1.0, foundTimePeriodCosts1.getExpense().getValue());
		
		mergedTimePeriodCostsMap.mergeAdd(timePeriodCostsMap2);
		TimePeriodCosts foundTimePeriodCosts2 = mergedTimePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(TestDateUnit.month12);
		assertEquals("wrong expense for dateunit", 3.0, foundTimePeriodCosts2.getExpense().getValue());
	}

	private void verifyMergeAddingIncompletedMaps(ProjectResource projectResource, DateUnit dateUnit2008)
	{
		TimePeriodCostsMap mapWithOnlyExpenses = new TimePeriodCostsMap();
		TimePeriodCosts incompleteWithoutResource = new TimePeriodCosts();		
		incompleteWithoutResource.setExpense(new OptionalDouble(10.0));
		mapWithOnlyExpenses.add(dateUnit2008, incompleteWithoutResource);
		
		TimePeriodCostsMap mapWithOnlyResourceWorkUnits = new TimePeriodCostsMap();
		TimePeriodCosts incompleteWithoutExpense = new TimePeriodCosts();		
		incompleteWithoutExpense.addResource(projectResource.getRef(), new OptionalDouble(3.0));
		mapWithOnlyResourceWorkUnits.add(dateUnit2008, incompleteWithoutExpense);
		
		mapWithOnlyExpenses.mergeAdd(mapWithOnlyResourceWorkUnits);
		TimePeriodCosts timePeriodCostsWithExpenseAndResource = mapWithOnlyExpenses.getTimePeriodCostsForSpecificDateUnit(dateUnit2008);
		OptionalDouble resourceExpense = timePeriodCostsWithExpenseAndResource.calculateResourceCosts(getProject());
		assertEquals("wrong resource expense?", (3.0 * 10.0), resourceExpense.getValue());
	}
	
	private ProjectResource createProjectResource() throws Exception
	{
		ProjectResource projectResource = getProject().createAndPopulateProjectResource();
		getProject().fillCostPerUnitField(projectResource, "10");
		return projectResource;
	}	
}
