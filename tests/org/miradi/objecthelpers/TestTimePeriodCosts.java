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

public class TestTimePeriodCosts extends TestCaseWithProject
{
	public TestTimePeriodCosts(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		TimePeriodCosts timePeriodCosts = new TimePeriodCosts();
		OptionalDouble emptyDouble = timePeriodCosts.getResourceWorkUnits(ORef.INVALID);
		assertFalse("getUnits for bogus resource returned a value?", emptyDouble.hasValue());
		
		ProjectResource projectResource = createProjectResource();
		timePeriodCosts.addWorkUnit(projectResource.getRef(), ORef.INVALID, new OptionalDouble(10.0));
		assertEquals("wrong units cost?", 10.0, timePeriodCosts.getResourceWorkUnits(projectResource.getRef()).getValue());
		assertEquals("wrong project resources sum?", 100.0, timePeriodCosts.calculateTotalCost(getProject()).getValue());
		
		timePeriodCosts.setExpense(new OptionalDouble(20.0));
		assertEquals("wrong expense?", 20.0, timePeriodCosts.getExpense().getValue());
		assertEquals("wrong total cost?", 100.0 + 20.0, timePeriodCosts.calculateTotalCost(getProject()).getValue());
	}
	
	public void testBasicEquals()
	{
		TimePeriodCosts emptyTimePeriodCosts = new TimePeriodCosts();
		assertEquals("empty TPC not equal to itself?", emptyTimePeriodCosts, emptyTimePeriodCosts);
	}
		
	public void testEquals() throws Exception
	{
		ProjectResource fred = createProjectResource();
		TimePeriodCosts timePeriodCosts1 = getProject().createTimePeriodCosts(20.0, fred.getRef(), 10.0);
		
		assertEquals("time period costs is not equals to itself?", timePeriodCosts1, timePeriodCosts1);
		
		TimePeriodCosts timePeriodCosts2 = getProject().createTimePeriodCosts(500.0, fred.getRef(), 10.0);
		assertNotEquals("Different expenses were equal?", timePeriodCosts1, timePeriodCosts2);
		assertNotEquals("Different expenses were equal?", timePeriodCosts2, timePeriodCosts1);
		
		timePeriodCosts2.setExpense(new OptionalDouble(20.0));
		assertEquals("Identical TPC's were not equal?", timePeriodCosts1, timePeriodCosts2);
		assertEquals("Identical TPC's were not equal?", timePeriodCosts2, timePeriodCosts1);
		
		ProjectResource jill = createProjectResource();
		timePeriodCosts1.addWorkUnit(jill.getRef(), ORef.INVALID, new OptionalDouble(30.0));
		assertNotEquals("Different units for resource were not equal?", timePeriodCosts1, timePeriodCosts2);
		assertNotEquals("Different units for resource were not equal?", timePeriodCosts2, timePeriodCosts1);
	}
	
	public void testEqualsWithFundingSource() throws Exception
	{
		TimePeriodCosts timePeriodCosts1 = new TimePeriodCosts();
		ORef fundingSourceRef1 = getProject().createFundingSource().getRef();
		timePeriodCosts1.addWorkUnit(ORef.INVALID, fundingSourceRef1, new OptionalDouble(10.0));
		assertEquals("time period costs with funding source is not equals to itself?", timePeriodCosts1, timePeriodCosts1);
		
		TimePeriodCosts timePeriodCosts2 = new TimePeriodCosts();
		assertNotEquals("TPC with fundingSource is equal to TPC without fundingSource?", timePeriodCosts1, timePeriodCosts2);
		assertNotEquals("TPC with fundingSource is equal to TPC without fundingSource?", timePeriodCosts2, timePeriodCosts1);
		
		timePeriodCosts2.addWorkUnit(ORef.INVALID, fundingSourceRef1, new OptionalDouble(10.0));
		assertEquals("Identical TPCs with fundsingSource were not equal", timePeriodCosts1, timePeriodCosts2);
		assertEquals("Identical TPCs with fundsingSource were not equal", timePeriodCosts2, timePeriodCosts1);
	}
	
	public void testAdd() throws Exception
	{
		ORef projectResourceRef1 = createProjectResource().getRef();
		TimePeriodCosts timePeriodCosts1 = getProject().createTimePeriodCosts(20.0, projectResourceRef1, 10.0);
		verifyAddition(timePeriodCosts1, new TimePeriodCosts(), projectResourceRef1, 20.0, 10.0);
		
		TimePeriodCosts timePeriodCosts2 = getProject().createTimePeriodCosts(1.0, projectResourceRef1, 1.0);
		verifyAddition(timePeriodCosts1, timePeriodCosts2, projectResourceRef1, 21.0, 11.0);
		
		ORef projectResourceRef2 = createProjectResource().getRef();
		TimePeriodCosts timePeriodCosts3 = getProject().createTimePeriodCosts(3.0, projectResourceRef2, 10.0);
		verifyAddition(timePeriodCosts1, timePeriodCosts3, projectResourceRef2, 24.0, 10.0);
	}
	
	public void testFundingSourceWorkUnitsMapBasics() throws Exception
	{
		TimePeriodCosts timePeriodCosts = new TimePeriodCosts();
		ORef fundingSourceRef = getProject().createFundingSource().getRef();
		timePeriodCosts.addWorkUnit(ORef.INVALID, fundingSourceRef, new OptionalDouble(10.0));
		OptionalDouble retrievedWorkUnits = timePeriodCosts.getFundingSourceWorkUnits(fundingSourceRef);
		assertTrue("funding source workUnits should have value?", retrievedWorkUnits.hasValue());
		assertEquals("incorrect workUnits for fundingSource?", 10.0, retrievedWorkUnits.getValue());
	}
	
	public void testTotalWorkUnitsAfterMergeAllProjectResourcesInPlace() throws Exception
	{
		TimePeriodCosts totalTimePeriodCosts = new TimePeriodCosts();
		assertTrue("total work units not blank?", totalTimePeriodCosts.getTotalWorkUnits().hasNoValue());
		
		ProjectResource fred = createProjectResource();
		TimePeriodCosts fredTen = getProject().createTimePeriodCosts(fred.getRef(), 10.0);
		totalTimePeriodCosts.mergeAllProjectResourcesInPlace(fredTen);
		assertEquals("wrong total work units?", 10.0, totalTimePeriodCosts.getTotalWorkUnits().getValue());
		
		totalTimePeriodCosts.mergeAllProjectResourcesInPlace(fredTen);
		assertEquals("wrong total work units?", 20.0, totalTimePeriodCosts.getTotalWorkUnits().getValue());		
	}

	private void verifyAddition(TimePeriodCosts mainTimePeriodCosts, TimePeriodCosts timePeriodCostsToAdd, ORef projectResourceRef1, double expectedExpense, double expectedUnits)
	{
		mainTimePeriodCosts.add(timePeriodCostsToAdd);
		assertEquals("incorrect expense after adding a timePeriodCosts", expectedExpense, mainTimePeriodCosts.getExpense().getValue());
		assertEquals("incorrect project resource after adding a timePeriodCosts", expectedUnits, mainTimePeriodCosts.getResourceWorkUnits(projectResourceRef1).getValue());
	}
	
	private ProjectResource createProjectResource() throws Exception
	{
		ProjectResource projectResource = getProject().createAndPopulateProjectResource();
		getProject().fillCostPerUnitField(projectResource, "10");
		return projectResource;
	}
}
