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

	protected void setUpDefaultCalculatorStrategy()
	{
		String currentWorkBudgetMode = calculator.getProjectTotalCalculatorStrategy().getWorkPlanBudgetMode();
		calculator.setProjectTotalCalculatorStrategy(new ProjectTotalCalculatorStrategyDefault(currentWorkBudgetMode));
	}

	protected void setUpSharedWorkPlanCalculatorStrategy()
	{
		String currentWorkBudgetMode = calculator.getProjectTotalCalculatorStrategy().getWorkPlanBudgetMode();
		calculator.setProjectTotalCalculatorStrategy(new ProjectTotalCalculatorStrategySharedWorkPlan(currentWorkBudgetMode));
	}

	protected void testMonitoringBudgetMode() throws Exception
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

	protected void testKeaIndicatorInResultsChain() throws Exception
	{
		testKeaIndicatorInResultsChain(1);
	}

	protected void testKeaIndicatorInResultsChain(final int expectedProjectTotalTimePeriodMapCount) throws Exception
	{
		DiagramFactor target = getProject().createAndAddFactorToDiagram(getResultsChainDiagramObject(), TargetSchema.getObjectType());
		getProject().turnOnTncMode((Target) target.getWrappedFactor());
		KeyEcologicalAttribute kea = getProject().createKea();
		ORefList keaRefs = new ORefList(kea);
		getProject().fillObjectUsingCommand(target.getWrappedORef(), Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, keaRefs.convertToIdList(KeyEcologicalAttributeSchema.getObjectType()).toString());

		Indicator indicatorWithResourceAssignment = getProject().createIndicator(kea);
		addResourceAssignment(indicatorWithResourceAssignment);

		turnOnDataFromResultsChainOnly();
		verifyCalculatedValues(expectedProjectTotalTimePeriodMapCount);

		turnOnDataFromBothDiagramTypes();
		verifyCalculatedValues(expectedProjectTotalTimePeriodMapCount);

		turnOnDataFromConceptualDiagramOnly();
		verifyEmptyProjectTotalTimePeriodCostsMap();
	}

	protected void testProjectTotalWithDraftStrategyIndicator() throws Exception
	{
		testProjectTotalWithDraftStrategyIndicator(20.0, 200.0);
	}

	protected void testProjectTotalWithDraftStrategyIndicator(final double expectedWorkUnits, final double expectedTotalBudgetCost) throws Exception
	{
		DiagramFactor diagramFactor = createNonDraftStrategyWithAssignment(getConceptualModelDiagramObject());
		Strategy strategy = Strategy.find(getProject(), diagramFactor.getWrappedORef());
		Indicator indicator = getProject().createIndicator(strategy);
		getProject().addResourceAssignment(indicator, 10.0, new DateUnit());
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

	protected void testConceptualModelIndicatorProjectTotal() throws Exception
	{
		testConceptualModelIndicatorProjectTotal(1);
	}

	protected void testConceptualModelIndicatorProjectTotal(final int expectedProjectTotalTimePeriodMapCount) throws Exception
	{
		createCauseWithIndicatorWithAssignment(getConceptualModelDiagramObject());

		turnOnDataFromConceptualDiagramOnly();
		verifyCalculatedValues(expectedProjectTotalTimePeriodMapCount);

		turnOnDataFromBothDiagramTypes();
		verifyCalculatedValues(expectedProjectTotalTimePeriodMapCount);

		turnOnDataFromResultsChainOnly();
		verifyEmptyBudgetTotalCost();
	}

	protected void testResultsChainIndicatorProjectTotal() throws Exception
	{
		testResultsChainIndicatorProjectTotal(1);
	}

	protected void testResultsChainIndicatorProjectTotal(final int expectedProjectTotalTimePeriodMapCount) throws Exception
	{
		createCauseWithIndicatorWithAssignment(getResultsChainDiagramObject());

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

	protected void testStrategyOnEachDiagramProjectTotal() throws Exception
	{
		testStrategyOnEachDiagramProjectTotal(true);
	}

	protected void testStrategyOnEachDiagramProjectTotal(final boolean expectIndicatorAssignmentsToBeCounted) throws Exception
	{
		createNonDraftStrategyWithAssignment(getConceptualModelDiagramObject());
		createNonDraftStrategyWithAssignment(getResultsChainDiagramObject());
		createCauseWithIndicatorWithAssignment(getConceptualModelDiagramObject());
		createCauseWithIndicatorWithAssignment(getResultsChainDiagramObject());

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

	protected void verifyEmptyBudgetTotalCost() throws Exception
	{
		OptionalDouble totalBudgetCost = calculator.calculateProjectAssignedTotals().calculateTotalBudgetCost(getProject());
		assertFalse("ConceptualModel Strategy is included in project totals?", totalBudgetCost.hasValue());
	}

	protected void verifyEmptyProjectTotalTimePeriodCostsMap() throws Exception
	{
		assertEquals("Should have empty project total time perdiod costs map?", 0, calculator.calculateProjectAssignedTotals().size());
	}

	protected void verifyCalculatedValues() throws Exception
	{
		verifyCalculatedValues(TEN_WORK_UNITS, 100.0);
	}

	protected void verifyCalculatedValues(final int expectedProjectTotalTimePeriodMapCount) throws Exception
	{
		verifyCalculatedValues(expectedProjectTotalTimePeriodMapCount, TEN_WORK_UNITS, 100.0);
	}

	protected void verifyCalculatedValues(final double expectedWorkUnits, final double expectedTotalBudgetCost) throws Exception
	{
		verifyCalculatedValues(1, expectedWorkUnits, expectedTotalBudgetCost);
	}

	protected void verifyCalculatedValues(final int expectedProjectTotalTimePeriodMapCount, final double expectedWorkUnits, final double expectedTotalBudgetCost) throws Exception
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

	protected TimePeriodCostsMap verifyProjectTotalTimePeriodCostsMap(final int expectedProjectTotalTimePeriodMapCount) throws Exception
	{
		TimePeriodCostsMap projectTotals = calculator.calculateProjectAssignedTotals();
		assertEquals("Project totals time period costs map should not be empty?", expectedProjectTotalTimePeriodMapCount, projectTotals.size());
		
		return projectTotals;
	}

	protected void turnOnDataFromResultsChainOnly() throws Exception
	{
		turnOnDiagramObjectDataFromCode(DiagramObjectDataInclusionQuestion.INCLUDE_RESULTS_CHAIN_DATA_CODE);
	}

	protected void turnOnDataFromConceptualDiagramOnly() throws Exception
	{
		turnOnDiagramObjectDataFromCode(DiagramObjectDataInclusionQuestion.INCLUDE_CONCEPTUAL_MODEL_DATA_CODE);
	}

	protected void turnOnDataFromBothDiagramTypes() throws Exception
	{
		turnOnDiagramObjectDataFromCode(DiagramObjectDataInclusionQuestion.INCLUDE_BOTH_DIAGRAM_DATA_CODE);
	}

	protected void turnOnDiagramObjectDataFromCode(String code) throws Exception
	{
		getProject().fillObjectUsingCommand(getProject().getMetadata(), ProjectMetadata.TAG_WORK_PLAN_DIAGRAM_DATA_INCLUSION, code);	
	}

	protected void createDraftStrategyWithAssignment(DiagramObject diagramModel) throws Exception
	{
		DiagramFactor draftStrategy = createNonDraftStrategyWithAssignment(diagramModel);
		getProject().turnOnDraft((Strategy)draftStrategy.getWrappedFactor());
	}

	protected DiagramFactor createNonDraftStrategyWithAssignment(DiagramObject diagramObject) throws Exception
	{
		DiagramFactor nonDraftStrategy = getProject().createAndAddFactorToDiagram(diagramObject, StrategySchema.getObjectType());
		addResourceAssignment(nonDraftStrategy.getWrappedFactor());
		
		return nonDraftStrategy;
	}

	protected void createCauseWithIndicatorWithAssignment(DiagramObject diagramObject) throws Exception
	{
		DiagramFactor diagramFactor = getProject().createAndAddFactorToDiagram(diagramObject, CauseSchema.getObjectType());
		Indicator indicator = getProject().createIndicator(diagramFactor.getWrappedFactor());
		addResourceAssignment(indicator);
	}

	protected DiagramObject getConceptualModelDiagramObject()
	{
		return getProject().getTestingDiagramModel().getDiagramObject();
	}

	protected DiagramObject getResultsChainDiagramObject()
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
