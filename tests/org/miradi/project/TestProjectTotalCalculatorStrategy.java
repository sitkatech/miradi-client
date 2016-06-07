/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

public class TestProjectTotalCalculatorStrategy extends TestProjectTotalCalculator
{
	public TestProjectTotalCalculatorStrategy(String name)
	{
		super(name);
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		setUpWorkPlanCalculatorStrategy();
	}

	public void testMonitoringBudgetMode() throws Exception
	{
		super.testMonitoringBudgetMode();
	}

	public void testKeaIndicatorInResultsChain() throws Exception
	{
		super.testKeaIndicatorInResultsChain(0);
	}

	public void testProjectTotalWithDraftStrategyIndicator() throws Exception
	{
		super.testProjectTotalWithDraftStrategyIndicator(10.0, 100.0);
	}

	public void testResultsChainDraftStrategyProjectTotal() throws Exception
	{
		super.testResultsChainDraftStrategyProjectTotal();
	}

	public void testResultsChainStrategyProjectTotal() throws Exception
	{
		super.testResultsChainStrategyProjectTotal();
	}

	public void testConceptualModelIndicatorProjectTotal() throws Exception
	{
		super.testConceptualModelIndicatorProjectTotal(0);
	}

	public void testResultsChainIndicatorProjectTotal() throws Exception
	{
		super.testResultsChainIndicatorProjectTotal(0);
	}

	public void testConceptualModelDraftStrategyProjectTotal() throws Exception
	{
		super.testConceptualModelDraftStrategyProjectTotal();
	}

	public void testConceptualModelStrategyProjectTotal() throws Exception
	{
		super.testConceptualModelStrategyProjectTotal();
	}

	public void testStrategyOnEachDiagramProjectTotal() throws Exception
	{
		super.testStrategyOnEachDiagramProjectTotal(false);
	}

	public void testEmptyProjectTotal() throws Exception
	{
		super.testEmptyProjectTotal();
	}
}
