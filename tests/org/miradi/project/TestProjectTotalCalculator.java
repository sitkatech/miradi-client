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

import org.miradi.diagram.PersistentDiagramModel;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.questions.DiagramObjectDataInclusionQuestion;
import org.miradi.questions.WorkPlanVisibleRowsQuestion;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.KeyEcologicalAttributeSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.utils.OptionalDouble;

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
		
		ORef resultsChainRef = getProject().createObject(ResultsChainDiagramSchema.getObjectType());
		ResultsChainDiagram resultsChain = ResultsChainDiagram.find(getProject(), resultsChainRef);
		resultsChainDiagramModel = new PersistentDiagramModel(getProject());
		resultsChainDiagramModel.fillFrom(resultsChain);
		
		fred = getProject().createAndPopulateProjectResource();
		calculator = getProject().getTimePeriodCostsMapsCache().getProjectTotalCalculator();
		dateUnit = getProject().createDateUnit(YEAR_2008, YEAR_2009);
	}
	
	public void testMonitoringBudgetMode() throws Exception
	{
		createNonDraftStrategyWithAssignment(getConceptualModelDiagramObject());
		createCauseWithIndicatorWithAssignment(getConceptualModelDiagramObject());
		
		verifyCalculationBasedOnMode(WorkPlanVisibleRowsQuestion.SHOW_MONITORING_RELATED_ROWS_CODE, 100.0);
		verifyCalculationBasedOnMode(WorkPlanVisibleRowsQuestion.SHOW_ACTION_RELATED_ROWS_CODE, 100.0);
		verifyCalculationBasedOnMode(WorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE, 200.0);
	}

	protected void verifyCalculationBasedOnMode(String workPlanBudgetMode, double expectedTotal) throws Exception
	{
		calculator.setProjectTotalCalculatorStrategy(new ProjectTotalCalculatorStrategyDefault(workPlanBudgetMode));
		TimePeriodCostsMap projectTotals = calculator.calculateProjectAssignedTotals();
		assertEquals("Project totals time period costs map should not be empty?", 1, projectTotals.size());

		OptionalDouble calculateTotalBudgetCost = projectTotals.calculateTotalBudgetCost(getProject());		
		assertEquals("Incorrect project total", expectedTotal, calculateTotalBudgetCost.getValue());
	}
	
	public void testKeaIndicatorInResultsChain() throws Exception
	{
		DiagramFactor target = getProject().createAndAddFactorToDiagram(getResultsChainDiagramObject(), TargetSchema.getObjectType());
		getProject().turnOnTncMode((Target) target.getWrappedFactor());
		KeyEcologicalAttribute kea = getProject().createKea();
		ORefList keaRefs = new ORefList(kea);
		getProject().fillObjectUsingCommand(target.getWrappedORef(), Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, keaRefs.convertToIdList(KeyEcologicalAttributeSchema.getObjectType()).toString());
		
		Indicator indicatorWithResourceAssignment = getProject().createIndicator(kea);
		addResourceAssignment(indicatorWithResourceAssignment);
		
		turnOnDataFromResultsChainOnly();
		verifyCalculatedValues();
		
		turnOnDataFromBothDiagramTypes();
		verifyCalculatedValues();
		
		turnOnDataFromConceptualDiagramOnly();
		verifyEmptyProjectTotalTimePeriodCostsMap();
	}
	
	public void testProjectTotalWithDraftStrategyIndicator() throws Exception
	{
		DiagramFactor diagramFactor = createNonDraftStrategyWithAssignment(getConceptualModelDiagramObject());
		Strategy strategy = Strategy.find(getProject(), diagramFactor.getWrappedORef());
		Indicator indicator = getProject().createIndicator(strategy);
		getProject().addResourceAssignment(indicator, 10.0, new DateUnit());
		verifyCalculatedValues(20.0, 200.0);
		
		getProject().turnOnDraft(strategy);
		verifyProjectTotalTimePeriodCostsMap(0);
	}

	public void testResultsChainDraftStrategyProjectTotal() throws Exception
	{
		createDraftStrategyWithAssignment(getResultsChainDiagramObject());
		
		turnOnDataFromResultsChainOnly();
		verifyEmptyProjectTotalTimePeriodCostsMap();
		
		turnOnDataFromConceptualDiagramOnly();
		verifyEmptyProjectTotalTimePeriodCostsMap();
		
		turnOnDataFromBothDiagramTypes();
		verifyEmptyProjectTotalTimePeriodCostsMap();
	}

	public void testResultsChainStrategyProjectTotal() throws Exception
	{
		createNonDraftStrategyWithAssignment(getResultsChainDiagramObject());
		
		turnOnDataFromResultsChainOnly();
		verifyCalculatedValues();
		
		turnOnDataFromBothDiagramTypes();
		verifyCalculatedValues();
		
		turnOnDataFromConceptualDiagramOnly();
		verifyEmptyProjectTotalTimePeriodCostsMap();
	}

	public void testConceptualModelIndicatorProjectTotal() throws Exception
	{
		createCauseWithIndicatorWithAssignment(getConceptualModelDiagramObject());
	
		turnOnDataFromConceptualDiagramOnly();
		verifyCalculatedValues();
		
		turnOnDataFromBothDiagramTypes();
		verifyCalculatedValues();
		
		turnOnDataFromResultsChainOnly();
		verifyEmptyBudgetTotalCost();
	}

	public void testResultsChainIndicatorProjectTotal() throws Exception
	{
		createCauseWithIndicatorWithAssignment(getResultsChainDiagramObject());

		turnOnDataFromResultsChainOnly();
		verifyCalculatedValues();
		
		turnOnDataFromBothDiagramTypes();
		verifyCalculatedValues();
		
		turnOnDataFromConceptualDiagramOnly();
		verifyEmptyBudgetTotalCost();
	}

	public void testConceptualModelDraftStrategyProjectTotal() throws Exception
	{
		createDraftStrategyWithAssignment(getConceptualModelDiagramObject());
	
		turnOnDataFromConceptualDiagramOnly();
		verifyEmptyProjectTotalTimePeriodCostsMap();
		
		turnOnDataFromResultsChainOnly();
		verifyEmptyProjectTotalTimePeriodCostsMap();
		
		turnOnDataFromBothDiagramTypes();
		verifyEmptyProjectTotalTimePeriodCostsMap();
	}

	public void testConceptualModelStrategyProjectTotal() throws Exception
	{
		createNonDraftStrategyWithAssignment(getConceptualModelDiagramObject());
		
		turnOnDataFromConceptualDiagramOnly();
		verifyCalculatedValues();
		
		turnOnDataFromBothDiagramTypes();
		verifyCalculatedValues();
		
		turnOnDataFromResultsChainOnly();
		verifyEmptyBudgetTotalCost();
	}
	
	public void testStrategyOnEachDiagramProjectTotal() throws Exception
	{
		createNonDraftStrategyWithAssignment(getConceptualModelDiagramObject());
		createNonDraftStrategyWithAssignment(getResultsChainDiagramObject());
		createCauseWithIndicatorWithAssignment(getConceptualModelDiagramObject());
		createCauseWithIndicatorWithAssignment(getResultsChainDiagramObject());
		
		turnOnDataFromConceptualDiagramOnly();
		verifyCalculatedValues(TEN_WORK_UNITS * 2, 200.00);
		
		turnOnDataFromBothDiagramTypes();
		verifyCalculatedValues(TEN_WORK_UNITS * 4, 400.0);
		
		turnOnDataFromResultsChainOnly();
		verifyCalculatedValues(TEN_WORK_UNITS * 2, 200.0);
	}

	public void testEmptyProjectTotal() throws Exception
	{
		assertEquals("no results chains created?", 1, getProject().getResultsChainDiagramPool().size());
		assertEquals("Empty project had non-zero totals data?", 0, calculator.calculateProjectAssignedTotals().size());
	}
	
	private void verifyEmptyBudgetTotalCost() throws Exception
	{
		OptionalDouble totalBudgetCost = calculator.calculateProjectAssignedTotals().calculateTotalBudgetCost(getProject());
		assertFalse("ConceptualModel Strategy is included in project totals?", totalBudgetCost.hasValue());
	}
	
	private void verifyEmptyProjectTotalTimePeriodCostsMap() throws Exception
	{
		assertEquals("Should have empty project total time perdiod costs map?", 0, calculator.calculateProjectAssignedTotals().size());
	}
	
	private void verifyCalculatedValues() throws Exception
	{
		verifyCalculatedValues(TEN_WORK_UNITS, 100.0);
	}

	private void verifyCalculatedValues(final double expectedWorkUnits,	final double expectedTotalBudgetCost) throws Exception
	{
		verifyCalculatedValues(1, expectedWorkUnits, expectedTotalBudgetCost);
	}

	private void verifyCalculatedValues(final int expectedProjectTotalTimePeriodMapCount, final double expectedWorkUnits, final double expectedTotalBudgetCost) throws Exception
	{
		TimePeriodCostsMap projectTotals = verifyProjectTotalTimePeriodCostsMap(expectedProjectTotalTimePeriodMapCount);

		TimePeriodCosts timePeriodCosts = projectTotals.getTimePeriodCostsForSpecificDateUnit(dateUnit);
		assertEquals("Incorrect total work units?", expectedWorkUnits, timePeriodCosts.getTotalWorkUnits().getValue());
		
		OptionalDouble calculateTotalBudgetCost = projectTotals.calculateTotalBudgetCost(getProject());		
		assertEquals("Incorrect project total", expectedTotalBudgetCost, calculateTotalBudgetCost.getValue());
	}

	private TimePeriodCostsMap verifyProjectTotalTimePeriodCostsMap(final int expectedProjectTotalTimePeriodMapCount) throws Exception
	{
		TimePeriodCostsMap projectTotals = calculator.calculateProjectAssignedTotals();
		assertEquals("Project totals time period costs map should not be empty?", expectedProjectTotalTimePeriodMapCount, projectTotals.size());
		
		return projectTotals;
	}
	
	private void turnOnDataFromResultsChainOnly() throws Exception
	{
		turnOnDiagramObjectDataFromCode(DiagramObjectDataInclusionQuestion.INCLUDE_RESULTS_CHAIN_DATA_CODE);
	}
	
	private void turnOnDataFromConceptualDiagramOnly() throws Exception
	{
		turnOnDiagramObjectDataFromCode(DiagramObjectDataInclusionQuestion.INCLUDE_CONCEPTUAL_MODEL_DATA_CODE);
	}
	
	private void turnOnDataFromBothDiagramTypes() throws Exception
	{
		turnOnDiagramObjectDataFromCode(DiagramObjectDataInclusionQuestion.INCLUDE_BOTH_DIAGRAM_DATA_CODE);
	}
	
	private void turnOnDiagramObjectDataFromCode(String code) throws Exception
	{
		getProject().fillObjectUsingCommand(getProject().getMetadata(), ProjectMetadata.TAG_WORK_PLAN_DIAGRAM_DATA_INCLUSION, code);	
	}
	
	private void createDraftStrategyWithAssignment(DiagramObject diagramModel) throws Exception
	{
		DiagramFactor draftStrategy = createNonDraftStrategyWithAssignment(diagramModel);
		getProject().turnOnDraft((Strategy)draftStrategy.getWrappedFactor());
	}
	
	private DiagramFactor createNonDraftStrategyWithAssignment(DiagramObject diagramObject) throws Exception
	{
		DiagramFactor nonDraftStrategy = getProject().createAndAddFactorToDiagram(diagramObject, StrategySchema.getObjectType());
		addResourceAssignment(nonDraftStrategy.getWrappedFactor());
		
		return nonDraftStrategy;
	}
	
	private void createCauseWithIndicatorWithAssignment(DiagramObject diagramObject) throws Exception
	{
		DiagramFactor diagramFactor = getProject().createAndAddFactorToDiagram(diagramObject, CauseSchema.getObjectType());
		Indicator indicator = getProject().createIndicator(diagramFactor.getWrappedFactor());
		addResourceAssignment(indicator);
	}
	
	private DiagramObject getConceptualModelDiagramObject()
	{
		return getProject().getTestingDiagramModel().getDiagramObject();
	}
	
	private DiagramObject getResultsChainDiagramObject()
	{
		return resultsChainDiagramModel.getDiagramObject();
	}
	
	private void addResourceAssignment(BaseObject wrappedFactor) throws Exception
	{
		getProject().addResourceAssignment(wrappedFactor, fred, TEN_WORK_UNITS, YEAR_2008, YEAR_2009);
	}
	
	private static final int TEN_WORK_UNITS = 10;
	private static final int YEAR_2008 = 2008;
	private static final int YEAR_2009 = 2009;
	
	private PersistentDiagramModel resultsChainDiagramModel;
	private ProjectResource fred;
	private ProjectTotalCalculator calculator;
	private DateUnit dateUnit;
}
