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

import java.util.Set;

import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.FundingSource;
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
		OptionalDouble emptyDouble = timePeriodCosts.getResourceWorkUnits(INVALID_RESOURCE_REF);
		assertFalse("getUnits for bogus resource returned a value?", emptyDouble.hasValue());
		
		ProjectResource projectResource = createProjectResource();
		timePeriodCosts = new TimePeriodCosts(projectResource.getRef(), INVALID_FUNDING_SOURCE_REF, new OptionalDouble(10.0));
		assertEquals("wrong units cost?", 10.0, timePeriodCosts.getResourceWorkUnits(projectResource.getRef()).getValue());
		assertEquals("wrong project resources sum?", 100.0, timePeriodCosts.calculateTotalCost(getProject()).getValue());
		
		getProject().addExpense(timePeriodCosts, 20.0);
		assertEquals("wrong expense?", 20.0, timePeriodCosts.getTotalExpense().getValue());
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
		
		timePeriodCosts2 = getProject().createTimePeriodCosts(20.0, fred.getRef(), 10.0);
		assertEquals("Identical TPC's were not equal?", timePeriodCosts1, timePeriodCosts2);
		assertEquals("Identical TPC's were not equal?", timePeriodCosts2, timePeriodCosts1);

		ProjectResource jill = createProjectResource();
		timePeriodCosts1.add(new TimePeriodCosts(jill.getRef(), INVALID_FUNDING_SOURCE_REF, new OptionalDouble(30.0)));
		assertNotEquals("Different units for resource were not equal?", timePeriodCosts1, timePeriodCosts2);
		assertNotEquals("Different units for resource were not equal?", timePeriodCosts2, timePeriodCosts1);
	}
	
	public void testEqualsWithFundingSource() throws Exception
	{
		ORef fundingSourceRef1 = createFundingSource();
		TimePeriodCosts timePeriodCosts1 = new TimePeriodCosts(INVALID_RESOURCE_REF, fundingSourceRef1, new OptionalDouble(10.0));
		assertEquals("time period costs with funding source is not equals to itself?", timePeriodCosts1, timePeriodCosts1);
		
		TimePeriodCosts timePeriodCosts2 = new TimePeriodCosts();
		assertNotEquals("TPC with fundingSource is equal to TPC without fundingSource?", timePeriodCosts1, timePeriodCosts2);
		assertNotEquals("TPC with fundingSource is equal to TPC without fundingSource?", timePeriodCosts2, timePeriodCosts1);
		
		TimePeriodCosts timePeriodCosts3 = new TimePeriodCosts(INVALID_RESOURCE_REF, fundingSourceRef1, new OptionalDouble(10.0));
		assertEquals("Identical TPCs with fundsingSource were not equal", timePeriodCosts1, timePeriodCosts3);
		assertEquals("Identical TPCs with fundsingSource were not equal", timePeriodCosts3, timePeriodCosts1);
	}
	
	public void testAddWithResourceOnly() throws Exception
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
	
	public void testAddWithFundingSourceOnly() throws Exception
	{
		ORef fundingSourceRef = createFundingSource();
		TimePeriodCosts timePeriodCosts = new TimePeriodCosts(INVALID_RESOURCE_REF, fundingSourceRef, new OptionalDouble(10.0));
		
		timePeriodCosts.add(new TimePeriodCosts());
		assertEquals("wrong work unit for funding source after addition?", 10.0, timePeriodCosts.getFundingSourceWorkUnits(fundingSourceRef).getValue());
		
		timePeriodCosts.add(timePeriodCosts);
		assertEquals("wrong work unit for funding source after addition?", 20.0, timePeriodCosts.getFundingSourceWorkUnits(fundingSourceRef).getValue());
	}
	
	public void testFundingSourceWorkUnitsMapBasics() throws Exception
	{
		assertEquals("funding source should be empty?", 0, new TimePeriodCosts().getFundingSourceWorkUnitsRefSet().size());
		
		ORef fundingSourceRef = createFundingSource();
		TimePeriodCosts timePeriodCosts = new TimePeriodCosts(INVALID_RESOURCE_REF, fundingSourceRef, new OptionalDouble(10.0));
		OptionalDouble retrievedWorkUnits = timePeriodCosts.getFundingSourceWorkUnits(fundingSourceRef);
		assertTrue("funding source workUnits should have value?", retrievedWorkUnits.hasValue());
		assertEquals("incorrect workUnits for fundingSource?", 10.0, retrievedWorkUnits.getValue());
	}
	
	public void testFundingSourceExpenseMapBasics() throws Exception
	{
		assertEquals("funding source expense map should be empty?", 0, new TimePeriodCosts().getFundingSourceExpensesRefSet().size());
		
		ORef fundingSourceRef = createFundingSource();
		TimePeriodCosts timePeriodCosts = new TimePeriodCosts(fundingSourceRef, new OptionalDouble(10.0));
		OptionalDouble retrievedExpenses = timePeriodCosts.getFundingSourceExpenses(fundingSourceRef);
		assertTrue("funding source expenses should have value?", retrievedExpenses.hasValue());
		assertEquals("incorrect expenses for fundingSource?", 10.0, retrievedExpenses.getValue());
	}
	
	public void testTotalWorkUnitsAfterMergeAllProjectResourcesInPlace() throws Exception
	{
		TimePeriodCosts totalTimePeriodCosts = new TimePeriodCosts();
		assertTrue("total work units not blank?", totalTimePeriodCosts.getTotalWorkUnits().hasNoValue());
		
		ProjectResource fred = createProjectResource();
		TimePeriodCosts fredTen = getProject().createTimePeriodCosts(fred.getRef(), 10.0);
		totalTimePeriodCosts.mergeAllWorkUnitMapsInPlace(fredTen);
		assertEquals("wrong total work units?", 10.0, totalTimePeriodCosts.getTotalWorkUnits().getValue());
		
		totalTimePeriodCosts.mergeAllWorkUnitMapsInPlace(fredTen);
		assertEquals("wrong total work units?", 20.0, totalTimePeriodCosts.getTotalWorkUnits().getValue());		
	}
	
	public void testMergeAllForFundingSource() throws Exception
	{
		TimePeriodCosts totalTimePeriodCosts = new TimePeriodCosts();
		
		ORef fundingSourceRef = createFundingSource();
		TimePeriodCosts timePeriodCosts = new TimePeriodCosts(INVALID_RESOURCE_REF, fundingSourceRef, new OptionalDouble(11.0));
		
		totalTimePeriodCosts.mergeAllWorkUnitMapsInPlace(timePeriodCosts);
		assertTrue("funding source does not have value after merge?", totalTimePeriodCosts.getFundingSourceWorkUnits(fundingSourceRef).hasValue());
		assertEquals("funding source was not merged?", 11.0, totalTimePeriodCosts.getFundingSourceWorkUnits(fundingSourceRef).getValue());
		
		totalTimePeriodCosts.mergeAllWorkUnitMapsInPlace(timePeriodCosts);
		assertEquals("funding source was not merged?", 22.0, totalTimePeriodCosts.getFundingSourceWorkUnits(fundingSourceRef).getValue());
	}
	
	public void testProjectResourceFilter() throws Exception
	{
		TimePeriodCosts empty = new TimePeriodCosts();
		empty.filterProjectResources(new ORefSet());
		assertEquals("resources map should still be empty after filtering nothing?", 0, empty.getResourceRefSet().size());

		ProjectResource fred = createProjectResource();
		empty.filterProjectResources(new ORefSet(fred));
		assertEquals("resources map should still be empty after filter?", 0, empty.getResourceRefSet().size());
		
		ProjectResource jill = createProjectResource();
		TimePeriodCosts withJillAndFred = new TimePeriodCosts();
		withJillAndFred.add(new TimePeriodCosts(jill.getRef(), INVALID_FUNDING_SOURCE_REF, new OptionalDouble(12.0)));
		withJillAndFred.add(new TimePeriodCosts(fred.getRef(), INVALID_FUNDING_SOURCE_REF, new OptionalDouble(13.0)));
		
		assertEquals("wrong resources count?", 2, withJillAndFred.getResourceRefSet().size());
		assertEquals("wrong total work units for fred and jill?", 25.0, withJillAndFred.getTotalWorkUnits().getValue());
		
		withJillAndFred.filterProjectResources(new ORefSet(jill));
		Set<ORef> afterFilteringOutFred = withJillAndFred.getResourceRefSet();
		assertEquals("wrong resources count after filtering?", 1, afterFilteringOutFred.size());
		assertTrue("jill was removed during filtering?", afterFilteringOutFred.contains(jill.getRef()));
		assertEquals("wrong totals work Units after fred filtered out?", 12.0, withJillAndFred.getTotalWorkUnits().getValue());
		
		withJillAndFred.filterProjectResources(new ORefSet(fred));
		Set<ORef> afterFilteringOutJillAndFred = withJillAndFred.getResourceRefSet();
		assertEquals("wrong resources count after filtering out fred?", 0, afterFilteringOutJillAndFred.size());
		assertTrue("wrong totals work Units after fred filtered out?", withJillAndFred.getTotalWorkUnits().hasNoValue());
	}
	
	public void testFilter() throws Exception
	{
		ProjectResource jill = createProjectResource();
		ProjectResource fred = createProjectResource();
		ORef fundingSourceRefForJill = createFundingSource();
		ORef fundingSourceRefForFred = createFundingSource();
		TimePeriodCosts withWorkUnits = new TimePeriodCosts();
		withWorkUnits.add(new TimePeriodCosts(jill.getRef(), fundingSourceRefForJill, new OptionalDouble(12.0)));
		withWorkUnits.add(new TimePeriodCosts(fred.getRef(), fundingSourceRefForFred, new OptionalDouble(13.0)));
		
		withWorkUnits.filterProjectResources(new ORefSet(jill));
		assertFalse("fred was not filtered out?", withWorkUnits.getResourceRefSet().contains(fred.getRef()));
		assertFalse("funding source for fred was not removed?", withWorkUnits.getFundingSourceWorkUnitsRefSet().contains(fundingSourceRefForFred));
		
		withWorkUnits.filterFundingSourcesWorkUnits(new ORefSet(fundingSourceRefForJill));
		assertEquals("jill should not have been filtered out?", 1, withWorkUnits.getResourceRefSet().size());
		assertEquals("funding source related to jill was filtered out?", 1, withWorkUnits.getFundingSourceWorkUnitsRefSet().size());
		
		withWorkUnits.add(new TimePeriodCosts(jill.getRef(), fundingSourceRefForJill, new OptionalDouble(12.0)));
		withWorkUnits.add(new TimePeriodCosts(fred.getRef(), fundingSourceRefForFred, new OptionalDouble(13.0)));
		withWorkUnits.filterFundingSourcesWorkUnits(new ORefSet(fundingSourceRefForJill));
		assertFalse("fred was not filtered out?", withWorkUnits.getResourceRefSet().contains(fred.getRef()));
		assertFalse("funding source for fred was not removed?", withWorkUnits.getFundingSourceWorkUnitsRefSet().contains(fundingSourceRefForFred));
		
		TimePeriodCosts withExpenses = new TimePeriodCosts();
		withExpenses.add(new TimePeriodCosts(fundingSourceRefForJill, new OptionalDouble(10.0)));
		withExpenses.add(new TimePeriodCosts(fundingSourceRefForFred, new OptionalDouble(11.0)));
		withExpenses.filterFundingSourcesExpenses(new ORefSet(fundingSourceRefForJill));
		assertFalse("fred was not filtered out?", withExpenses.getFundingSourceExpensesRefSet().contains(fundingSourceRefForFred));
	}

	private ORef createFundingSource() throws Exception
	{
		return getProject().createFundingSource().getRef();
	}

	private void verifyAddition(TimePeriodCosts mainTimePeriodCosts, TimePeriodCosts timePeriodCostsToAdd, ORef projectResourceRef1, double expectedExpense, double expectedUnits)
	{
		mainTimePeriodCosts.add(timePeriodCostsToAdd);
		assertEquals("incorrect expense after adding a timePeriodCosts", expectedExpense, mainTimePeriodCosts.getTotalExpense().getValue());
		assertEquals("incorrect project resource after adding a timePeriodCosts", expectedUnits, mainTimePeriodCosts.getResourceWorkUnits(projectResourceRef1).getValue());
	}
	
	private ProjectResource createProjectResource() throws Exception
	{
		ProjectResource projectResource = getProject().createAndPopulateProjectResource();
		getProject().fillCostPerUnitField(projectResource, "10");
		return projectResource;
	}
	
	private ORef INVALID_RESOURCE_REF = ORef.createInvalidWithType(ProjectResource.getObjectType());
	private ORef INVALID_FUNDING_SOURCE_REF = ORef.createInvalidWithType(FundingSource.getObjectType());
}
