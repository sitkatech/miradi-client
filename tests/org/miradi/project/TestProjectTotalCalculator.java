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
import org.miradi.ids.IdList;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;

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
	
	public void testKeaIndicatorInResultsChain() throws Exception
	{
		DiagramFactor target = getProject().createAndAddFactorToDiagram(resultsChainDiagramModel.getDiagramObject(), Target.getObjectType());
		getProject().turnOnTncMode((Target) target.getWrappedFactor());
		KeyEcologicalAttribute kea = getProject().createKea();
		ORefList keaRefs = new ORefList(kea);
		getProject().fillObjectUsingCommand(target.getWrappedORef(), Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, keaRefs.convertToIdList(KeyEcologicalAttribute.getObjectType()).toString());
		
		Indicator indicatorWithResourceAssignment = getProject().createIndicator();
		addResourceAssignment(indicatorWithResourceAssignment);
		IdList indicatorIds = new IdList(Indicator.getObjectType());
		indicatorIds.add(indicatorWithResourceAssignment.getId());
		getProject().fillObjectUsingCommand(kea.getRef(), KeyEcologicalAttribute.TAG_INDICATOR_IDS, indicatorIds.toString());
		
		TimePeriodCostsMap totalsWithIndicator = calculator.calculateProjectTotals();
		TimePeriodCosts indictorTimePeriodCosts = totalsWithIndicator.getTimePeriodCostsForSpecificDateUnit(dateUnit);
		assertEquals("did not include kea indicators in totals", 10.0, indictorTimePeriodCosts.getResourcesTotalUnits().getValue());
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
		
		assertEquals("did not include strategy inside results chain?", 10.0, strategyTimePeriodCosts.getResourcesTotalUnits().getValue());
	}

	public void testConceptualModelIndicatorProjectTotal() throws Exception
	{
		Indicator indicatorWithResourceAssignment = getProject().createAndPopulateIndicator();
		addResourceAssignment(indicatorWithResourceAssignment);
		IdList indicatorIds = new IdList(Indicator.getObjectType());
		indicatorIds.add(indicatorWithResourceAssignment.getId());
		
		DiagramFactor diagramFactor = getProject().createAndAddFactorToDiagram(Cause.getObjectType());
		getProject().fillObjectUsingCommand(diagramFactor.getWrappedORef(), Cause.TAG_INDICATOR_IDS, indicatorIds.toString());
		
		assertEquals("did not include indicator in totals?", 0, calculator.calculateProjectTotals().size());
	}
	
	public void testResultsChainIndicatorProjectTotal() throws Exception
	{
		Indicator indicatorWithResourceAssignment = getProject().createAndPopulateIndicator();
		addResourceAssignment(indicatorWithResourceAssignment);
		IdList indicatorIds = new IdList(Indicator.getObjectType());
		indicatorIds.add(indicatorWithResourceAssignment.getId());
		
		DiagramFactor diagramFactor = getProject().createAndAddFactorToDiagram(resultsChainDiagramModel.getDiagramObject(), Cause.getObjectType());
		getProject().fillObjectUsingCommand(diagramFactor.getWrappedORef(), Cause.TAG_INDICATOR_IDS, indicatorIds.toString());
		
		TimePeriodCostsMap totalsWithIndicator = calculator.calculateProjectTotals();
		assertEquals("did not include results chain indicator in totals?", 1, totalsWithIndicator.size());
		TimePeriodCosts indictorTimePeriodCosts = totalsWithIndicator.getTimePeriodCostsForSpecificDateUnit(dateUnit);
		
		assertEquals("wrong resources total units calculation with indicator?", 10.0, indictorTimePeriodCosts.getResourcesTotalUnits().getValue());
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
