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
		
		TimePeriodCosts timePeriodCosts = new TimePeriodCosts();
		timePeriodCosts.setExpense(new OptionalDouble(500.0));
		ProjectResource projectResource = createProjectResource();
		timePeriodCosts.addResourceCost(projectResource.getRef(), new OptionalDouble(10.0));
		DateUnit dateUnit = getProject().createDateUnit(2000, 2008);
		
		timePeriodCostsMap.add(dateUnit, timePeriodCosts);
		TimePeriodCosts foundTimePeriodCosts = timePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnit);
		assertEquals("time period costs should not be equal?", foundTimePeriodCosts, timePeriodCosts);
	}

	public void testMergeAdd() throws Exception
	{
		TimePeriodCosts timePeriodCostsWithProjectResource = new TimePeriodCosts();
		ProjectResource projectResource = createProjectResource();
		timePeriodCostsWithProjectResource.addResourceCost(projectResource.getRef(), new OptionalDouble(10.0));
		DateUnit dateUnit = getProject().createDateUnit(2000, 2008);
		TimePeriodCostsMap resourceTimePeriodCostsMap = new TimePeriodCostsMap();
		resourceTimePeriodCostsMap.add(dateUnit, timePeriodCostsWithProjectResource);
		
		TimePeriodCosts timePeriodCostsWithExpense = new TimePeriodCosts();
		timePeriodCostsWithExpense.setExpense(new OptionalDouble(500.0));
		TimePeriodCostsMap expenseTimePerdiodCostsMap = new TimePeriodCostsMap();
		expenseTimePerdiodCostsMap.add(dateUnit, timePeriodCostsWithExpense);
		
		TimePeriodCostsMap mergedTimePeriodCostsMap = new TimePeriodCostsMap();
		mergedTimePeriodCostsMap.mergeAdd(expenseTimePerdiodCostsMap, dateUnit);
		mergedTimePeriodCostsMap.mergeAdd(resourceTimePeriodCostsMap, dateUnit);
		
		TimePeriodCosts expectedTimePeriodCosts = getProject().createTimePeriodCosts(500.0, projectResource.getRef(), 10.0);
		
		TimePeriodCosts timePeriodCosts = mergedTimePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnit);
		assertEquals("incorrect time period costs?", expectedTimePeriodCosts, timePeriodCosts);
	}
	
	private ProjectResource createProjectResource() throws Exception
	{
		ProjectResource projectResource = getProject().createAndPopulateProjectResource();
		getProject().fillCostPerUnitField(projectResource, "10");
		return projectResource;
	}
}
