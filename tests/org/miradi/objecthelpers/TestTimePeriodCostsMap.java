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
	}

	private void verifyGetTimePeriodCostsForSpecificDateUnit(TimePeriodCostsMap timePeriodCostsMap, TimePeriodCosts timePeriodCosts, DateUnit dateUnit)
	{
		TimePeriodCosts foundTimePeriodCosts = timePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnit);
		assertEquals("Single TPC wasn't found?", foundTimePeriodCosts, timePeriodCosts);
	}
	
	public void testMergeOverlay() throws Exception
	{	
		TimePeriodCostsMap timePeriodCostsMap2006 = new TimePeriodCostsMap();
		ProjectResource projectResourcePaul = createProjectResource();
		TimePeriodCosts timePeriodCosts2006 = getProject().createTimePeriodCosts(20.0, projectResourcePaul.getRef(), 10.0);
		DateUnit dateUnit2006 = getProject().createSingleYearDateUnit(2006);
		timePeriodCostsMap2006.add(dateUnit2006, timePeriodCosts2006);
		assertEquals(timePeriodCosts2006, timePeriodCostsMap2006.getTimePeriodCostsForSpecificDateUnit(dateUnit2006));
		
		TimePeriodCostsMap timePeriodCostsMap2007 = new TimePeriodCostsMap();
		DateUnit dateUnit2007 = getProject().createSingleYearDateUnit(2007);
		TimePeriodCosts timePeriodCosts2007 = getProject().createTimePeriodCosts(22.0, projectResourcePaul.getRef(), 12.0);
		TimePeriodCosts timePeriodCosts2007Q1 = getProject().createTimePeriodCosts(23.0, projectResourcePaul.getRef(), 11.0);
		timePeriodCostsMap2007.add(dateUnit2007, timePeriodCosts2007);
		DateUnit dateUnit2007Q1 = new DateUnit("2007Q1");
		timePeriodCostsMap2007.add(dateUnit2007Q1, timePeriodCosts2007Q1);
		
		assertEquals("wrong expense?", 22.0, timePeriodCosts2007.getExpense().getValue());
		assertEquals("wrong calculated project resource?", 120.0, timePeriodCosts2007.calculateProjectResources(getProject()).getValue());
		
		TimePeriodCostsMap projectTimePeriodCostsMap = new TimePeriodCostsMap();
		DateUnit projectDateUnit = getProject().createDateUnit(2000, 2010);
		projectTimePeriodCostsMap.mergeOverlay(timePeriodCostsMap2006, projectDateUnit);
		TimePeriodCosts specificTimePeriodCostsFor2006 = projectTimePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnit2006);
		assertEquals("Merging larger unit changed existing data?", timePeriodCosts2006, specificTimePeriodCostsFor2006);
		
		projectTimePeriodCostsMap.mergeOverlay(timePeriodCostsMap2007, projectDateUnit);
		TimePeriodCosts specificTimePeriodCostsFor2007 = projectTimePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnit2007);
		assertEquals("Merging larger unit changed existing data?", timePeriodCosts2007, specificTimePeriodCostsFor2007);
		
		TimePeriodCostsMap timePeriodCostsMapSecond2007 = new TimePeriodCostsMap();
		ProjectResource projectResourceJon = createProjectResource();
		TimePeriodCosts timePeriodCostsSecond2007 = getProject().createTimePeriodCosts(25.0, projectResourceJon.getRef(), 15.0);
		timePeriodCostsMapSecond2007.add(dateUnit2007, timePeriodCostsSecond2007);
		projectTimePeriodCostsMap.mergeOverlay(timePeriodCostsMapSecond2007, projectDateUnit);
		TimePeriodCosts timePeriodCostsAfterOverlay = specificTimePeriodCostsFor2007;
		assertEquals("wrong expense after merge overlay?", 47.0, timePeriodCostsAfterOverlay.getExpense().getValue());
		OptionalDouble projectResourceCost = timePeriodCostsAfterOverlay.calculateProjectResources(getProject());
		assertEquals("wrong project resource cost?", 270.0, projectResourceCost.getValue());
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
		mergedTimePeriodCostsMap.mergeAdd(timePerdiodCostsMap2, dateUnit2008);
		TimePeriodCosts timePeriodCosts3 = mergedTimePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnit2008);
		assertEquals("wrong expense?", 2.0, timePeriodCosts3.getExpense().getValue());
		assertEquals("wrong unit cost?", 30.0, timePeriodCosts3.calculateProjectResources(getProject()).getValue());
		
		mergedTimePeriodCostsMap.mergeAdd(timePeriodCostsMap1, dateUnit2008);		
		TimePeriodCosts timePeriodCosts4 = mergedTimePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnit2008);
		assertEquals("wrong expense?", 3.0, timePeriodCosts4.getExpense().getValue());
		assertEquals("wrong unit cost?", 50.0, timePeriodCosts4.calculateProjectResources(getProject()).getValue());
		
		verifyMergeAddingIncompletedMaps(projectResource, dateUnit2008);
	}

	private void verifyMergeAddingIncompletedMaps(ProjectResource projectResource, DateUnit dateUnit2008)
	{
		TimePeriodCostsMap incompleteMapWithoutResource = new TimePeriodCostsMap();
		TimePeriodCosts incompleteWithoutResource = new TimePeriodCosts();		
		incompleteWithoutResource.setExpense(new OptionalDouble(10.0));
		incompleteMapWithoutResource.add(dateUnit2008, incompleteWithoutResource);
		
		TimePeriodCostsMap incompleteMapWithoutExpense = new TimePeriodCostsMap();
		TimePeriodCosts incompleteWithoutExpense = new TimePeriodCosts();		
		incompleteWithoutExpense.addResourceCost(projectResource.getRef(), new OptionalDouble(3.0));
		incompleteMapWithoutExpense.add(dateUnit2008, incompleteWithoutExpense);
		
		incompleteMapWithoutResource.mergeAdd(incompleteMapWithoutExpense, dateUnit2008);
		TimePeriodCosts timePeriodCostsWithExpenseAndResource = incompleteMapWithoutResource.getTimePeriodCostsForSpecificDateUnit(dateUnit2008);
		OptionalDouble resourceExpense = timePeriodCostsWithExpenseAndResource.calculateProjectResources(getProject());
		assertEquals("wrong resource expense?", 30.0, resourceExpense.getValue());
	}
	
	private ProjectResource createProjectResource() throws Exception
	{
		ProjectResource projectResource = getProject().createAndPopulateProjectResource();
		getProject().fillCostPerUnitField(projectResource, "10");
		return projectResource;
	}	
}
