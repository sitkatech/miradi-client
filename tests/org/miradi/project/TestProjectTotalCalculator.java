/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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
import org.miradi.objects.*;
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

	protected void setUpWorkPlanCalculatorStrategy()
	{
		String currentWorkBudgetMode = calculator.getProjectTotalCalculatorStrategy().getWorkPlanBudgetMode();
		calculator.setProjectTotalCalculatorStrategy(new ProjectTotalCalculatorStrategy(currentWorkBudgetMode));
	}

	protected void testMonitoringBudgetMode() throws Exception
	{
		DiagramFactor diagramFactor = createNonDraftStrategyWithAssignment(getConceptualModelDiagramObject());
		Strategy strategy = (Strategy) diagramFactor.getWrappedFactor();
		createMonitoringActivity(strategy);

		verifyCalculationBasedOnMode(WorkPlanVisibleRowsQuestion.SHOW_MONITORING_RELATED_ROWS_CODE, 212.0);
		verifyCalculationBasedOnMode(WorkPlanVisibleRowsQuestion.SHOW_ACTION_RELATED_ROWS_CODE, 100.0);
		verifyCalculationBasedOnMode(WorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE, 212.0);
	}

	private void verifyCalculationBasedOnMode(String workPlanBudgetMode, double expectedTotal) throws Exception
	{
		calculator.setProjectTotalCalculatorStrategy(new ProjectTotalCalculatorStrategy(workPlanBudgetMode));
		TimePeriodCostsMap projectTotals = calculator.calculateProjectAssignedTotals();
		assertEquals("Project totals time period costs map should not be empty?", 1, projectTotals.size());

		OptionalDouble calculateTotalBudgetCost = projectTotals.calculateTotalBudgetCost(getProject());		
		assertEquals("Incorrect project total", expectedTotal, calculateTotalBudgetCost.getValue());
	}

	protected void testKeaIndicatorInResultsChain(final int expectedProjectTotalTimePeriodMapCount) throws Exception
	{
		DiagramFactor target = getProject().createAndAddFactorToDiagram(getResultsChainDiagramObject(), TargetSchema.getObjectType());
		getProject().turnOnTncMode((Target) target.getWrappedFactor());
		KeyEcologicalAttribute kea = getProject().createKea();
		ORefList keaRefs = new ORefList(kea);
		getProject().fillObjectUsingCommand(target.getWrappedORef(), Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, keaRefs.convertToIdList(KeyEcologicalAttributeSchema.getObjectType()).toString());

		getProject().createIndicator(kea);

		turnOnDataFromResultsChainOnly();
		verifyCalculatedValues(expectedProjectTotalTimePeriodMapCount);

		turnOnDataFromBothDiagramTypes();
		verifyCalculatedValues(expectedProjectTotalTimePeriodMapCount);

		turnOnDataFromConceptualDiagramOnly();
		verifyEmptyProjectTotalTimePeriodCostsMap();
	}

	protected void testProjectTotalWithDraftStrategyIndicator(final double expectedWorkUnits, final double expectedTotalBudgetCost) throws Exception
	{
		DiagramFactor diagramFactor = createNonDraftStrategyWithAssignment(getConceptualModelDiagramObject());
		Strategy strategy = Strategy.find(getProject(), diagramFactor.getWrappedORef());
		Indicator indicator = getProject().createIndicator(strategy);
		verifyCalculatedValues(expectedWorkUnits, expectedTotalBudgetCost);

		getProject().turnOnDraft(strategy);
		verifyProjectTotalTimePeriodCostsMap(0);
	}

	protected void testResultsChainDraftStrategyProjectTotal() throws Exception
	{
		createDraftStrategyWithAssignment(getResultsChainDiagramObject());
		
		turnOnDataFromResultsChainOnly();
		verifyEmptyProjectTotalTimePeriodCostsMap();
		
		turnOnDataFromConceptualDiagramOnly();
		verifyEmptyProjectTotalTimePeriodCostsMap();
		
		turnOnDataFromBothDiagramTypes();
		verifyEmptyProjectTotalTimePeriodCostsMap();
	}

	protected void testResultsChainStrategyProjectTotal() throws Exception
	{
		createNonDraftStrategyWithAssignment(getResultsChainDiagramObject());
		
		turnOnDataFromResultsChainOnly();
		verifyCalculatedValues();
		
		turnOnDataFromBothDiagramTypes();
		verifyCalculatedValues();
		
		turnOnDataFromConceptualDiagramOnly();
		verifyEmptyProjectTotalTimePeriodCostsMap();
	}

	protected void testConceptualModelIndicatorProjectTotal(final int expectedProjectTotalTimePeriodMapCount) throws Exception
	{
		createCauseWithIndicator(getConceptualModelDiagramObject());

		turnOnDataFromConceptualDiagramOnly();
		verifyCalculatedValues(expectedProjectTotalTimePeriodMapCount);

		turnOnDataFromBothDiagramTypes();
		verifyCalculatedValues(expectedProjectTotalTimePeriodMapCount);

		turnOnDataFromResultsChainOnly();
		verifyEmptyBudgetTotalCost();
	}

	protected void testResultsChainIndicatorProjectTotal(final int expectedProjectTotalTimePeriodMapCount) throws Exception
	{
		createCauseWithIndicator(getResultsChainDiagramObject());

		turnOnDataFromResultsChainOnly();
		verifyCalculatedValues(expectedProjectTotalTimePeriodMapCount);

		turnOnDataFromBothDiagramTypes();
		verifyCalculatedValues(expectedProjectTotalTimePeriodMapCount);

		turnOnDataFromConceptualDiagramOnly();
		verifyEmptyBudgetTotalCost();
	}

	protected void testConceptualModelDraftStrategyProjectTotal() throws Exception
	{
		createDraftStrategyWithAssignment(getConceptualModelDiagramObject());
	
		turnOnDataFromConceptualDiagramOnly();
		verifyEmptyProjectTotalTimePeriodCostsMap();
		
		turnOnDataFromResultsChainOnly();
		verifyEmptyProjectTotalTimePeriodCostsMap();
		
		turnOnDataFromBothDiagramTypes();
		verifyEmptyProjectTotalTimePeriodCostsMap();
	}

	protected void testConceptualModelStrategyProjectTotal() throws Exception
	{
		createNonDraftStrategyWithAssignment(getConceptualModelDiagramObject());
		
		turnOnDataFromConceptualDiagramOnly();
		verifyCalculatedValues();
		
		turnOnDataFromBothDiagramTypes();
		verifyCalculatedValues();
		
		turnOnDataFromResultsChainOnly();
		verifyEmptyBudgetTotalCost();
	}

	protected void testStrategyOnEachDiagramProjectTotal(final boolean expectIndicatorAssignmentsToBeCounted) throws Exception
	{
		createNonDraftStrategyWithAssignment(getConceptualModelDiagramObject());
		createNonDraftStrategyWithAssignment(getResultsChainDiagramObject());
		createCauseWithIndicator(getConceptualModelDiagramObject());
		createCauseWithIndicator(getResultsChainDiagramObject());

		turnOnDataFromConceptualDiagramOnly();
		if (expectIndicatorAssignmentsToBeCounted)
			verifyCalculatedValues(TEN_WORK_UNITS * 2, 200.00);
		else
			verifyCalculatedValues(TEN_WORK_UNITS, 100.00);

		turnOnDataFromBothDiagramTypes();
		if (expectIndicatorAssignmentsToBeCounted)
			verifyCalculatedValues(TEN_WORK_UNITS * 4, 400.0);
		else
			verifyCalculatedValues(TEN_WORK_UNITS * 2, 200.0);

		turnOnDataFromResultsChainOnly();
		if (expectIndicatorAssignmentsToBeCounted)
			verifyCalculatedValues(TEN_WORK_UNITS * 2, 200.0);
		else
			verifyCalculatedValues(TEN_WORK_UNITS, 100.00);
	}

	protected void testEmptyProjectTotal() throws Exception
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

	private void verifyCalculatedValues(final int expectedProjectTotalTimePeriodMapCount) throws Exception
	{
		verifyCalculatedValues(expectedProjectTotalTimePeriodMapCount, TEN_WORK_UNITS, 100.0);
	}

	private void verifyCalculatedValues(final double expectedWorkUnits, final double expectedTotalBudgetCost) throws Exception
	{
		verifyCalculatedValues(1, expectedWorkUnits, expectedTotalBudgetCost);
	}

	private void verifyCalculatedValues(final int expectedProjectTotalTimePeriodMapCount, final double expectedWorkUnits, final double expectedTotalBudgetCost) throws Exception
	{
		TimePeriodCostsMap projectTotals = verifyProjectTotalTimePeriodCostsMap(expectedProjectTotalTimePeriodMapCount);

		if (expectedProjectTotalTimePeriodMapCount > 0)
		{
			TimePeriodCosts timePeriodCosts = projectTotals.getTimePeriodCostsForSpecificDateUnit(dateUnit);
			assertEquals("Incorrect total work units?", expectedWorkUnits, timePeriodCosts.getTotalWorkUnits().getValue());

			OptionalDouble calculateTotalBudgetCost = projectTotals.calculateTotalBudgetCost(getProject());
			assertEquals("Incorrect project total", expectedTotalBudgetCost, calculateTotalBudgetCost.getValue());
		}
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

	private void createMonitoringActivity(Strategy strategy) throws Exception
	{
		Task monitoringActivity = getProject().createActivity(strategy);
		getProject().populateTask(monitoringActivity, "Some Monitoring Activity", true);
	}

	private void createCauseWithIndicator(DiagramObject diagramObject) throws Exception
	{
		DiagramFactor diagramFactor = getProject().createAndAddFactorToDiagram(diagramObject, CauseSchema.getObjectType());
		getProject().createIndicator(diagramFactor.getWrappedFactor());
	}

	private DiagramObject getConceptualModelDiagramObject()
	{
		return getProject().getTestingDiagramModel().getDiagramObject();
	}

	private DiagramObject getResultsChainDiagramObject()
	{
		return resultsChainDiagramModel.getDiagramObject();
	}

	protected void addResourceAssignment(BaseObject wrappedFactor) throws Exception
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
