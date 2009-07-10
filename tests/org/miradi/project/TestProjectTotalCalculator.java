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

package org.miradi.project;

import org.miradi.diagram.PersistentDiagramModel;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Indicator;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;

public class TestProjectTotalCalculator extends TestCaseWithProject
{
	public TestProjectTotalCalculator(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		
		ORef resultsChainRef = getProject().createObject(ResultsChainDiagram.getObjectType());
		ResultsChainDiagram resultsChain = ResultsChainDiagram.find(getProject(), resultsChainRef);
		resultsChainDiagramModel = new PersistentDiagramModel(getProject());
		resultsChainDiagramModel.fillFrom(resultsChain);
		
		fred = getProject().createAndPopulateProjectResource();
		calculator = new ProjectTotalCalculator(getProject());
		dateUnit = getProject().createDateUnit(YEAR_2008, YEAR_2009);
	}
	
	public void testResultsChainDraftStrategyProjectTotal() throws Exception
	{
		DiagramFactor resultsChainDraftStrategy = getProject().createAndAddFactorToDiagram(resultsChainDiagramModel.getDiagramObject(), Strategy.getObjectType());
		addResourceAssignment(resultsChainDraftStrategy.getWrappedFactor());
		getProject().turnOnDraft((Strategy)resultsChainDraftStrategy.getWrappedFactor());
		TimePeriodCostsMap totalsWithDraftStrategy = calculator.calculateProjectTotals();
		
		assertEquals("Results chain Draft Strategy included in project totals?", 0, totalsWithDraftStrategy.size());
	}

	public void testResultsChainStrategyProjectTotal() throws Exception
	{
		DiagramFactor resultsChainNonDraftStrategy = getProject().createAndAddFactorToDiagram(resultsChainDiagramModel.getDiagramObject(), Strategy.getObjectType());
		addResourceAssignment(resultsChainNonDraftStrategy.getWrappedFactor());
		TimePeriodCostsMap totalsWithNonDraftStrategy = calculator.calculateProjectTotals();
		TimePeriodCosts strategyTimePeriodCosts = totalsWithNonDraftStrategy.getTimePeriodCostsForSpecificDateUnit(dateUnit);
		
		assertEquals("did not include strategy inside results chain?", 10.0, strategyTimePeriodCosts.calculateResourcesTotalUnits().getValue());
	}

	public void testIndicatorProjectTotal() throws Exception
	{
		Indicator indicatorWithResourceAssignment = getProject().createAndPopulateIndicator();
		addResourceAssignment(indicatorWithResourceAssignment);
		TimePeriodCostsMap totalsWithIndicator = calculator.calculateProjectTotals();
		assertEquals("did not include indicator in totals?", 1, totalsWithIndicator.size());
		TimePeriodCosts indictorTimePeriodCosts = totalsWithIndicator.getTimePeriodCostsForSpecificDateUnit(dateUnit);
		
		assertEquals("wrong resources total units calculation with indicator?", 10.0, indictorTimePeriodCosts.calculateResourcesTotalUnits().getValue());
	}

	public void testConceptualModelDraftStrategyProjectTotal() throws Exception
	{
		DiagramFactor conceptualModelDraftStrategy = getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
		getProject().turnOnDraft((Strategy)conceptualModelDraftStrategy.getWrappedFactor());
		addResourceAssignment(conceptualModelDraftStrategy.getWrappedFactor());
		
		assertEquals("ConceptualModel Draft Strategy included in project totals?", 0, calculator.calculateProjectTotals().size());
	}

	public void testConceptualModelStrategyProjectTotal() throws Exception
	{
		DiagramFactor conceptualModelStrategy = getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
		addResourceAssignment(conceptualModelStrategy.getWrappedFactor());
		assertEquals("ConceptualModel Strategy included in project totals?", 0, calculator.calculateProjectTotals().size());
	}

	public void testEmptyProjectTotal() throws Exception
	{
		assertEquals("no results chains created?", 1, getProject().getResultsChainDiagramPool().size());
		assertEquals("Empty project had non-zero totals data?", 0, calculator.calculateProjectTotals().size());
	}

	private void addResourceAssignment(BaseObject wrappedFactor) throws Exception
	{
		getProject().addResourceAssignment(wrappedFactor, fred, 10, YEAR_2008, YEAR_2009);
	}
	
	private static final int YEAR_2008 = 2008;
	private static final int YEAR_2009 = 2009;
	
	private PersistentDiagramModel resultsChainDiagramModel;
	private ProjectResource fred;
	private ProjectTotalCalculator calculator;
	private DateUnit dateUnit;
}
