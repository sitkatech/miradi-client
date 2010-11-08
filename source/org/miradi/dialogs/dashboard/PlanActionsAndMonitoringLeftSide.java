/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.dashboard;

import java.util.HashMap;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.Dashboard;

public class PlanActionsAndMonitoringLeftSide extends LeftSidePanelWithSelectableRows
{
	public PlanActionsAndMonitoringLeftSide(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse);
		
		createHeaderRow(EAM.text("2. Plan Actions and Monitoring"), "", getMainDescriptionFileName(), getSummaryOverviewStepName());
		
		addDevelopStrategicPlanRow();
		addDevelopFormatMonitoringPlanRow();
		addDevelopOperationalPlanRow();
	}

	private void addDevelopOperationalPlanRow() throws Exception
	{
		createSubHeaderRow(EAM.text("2C. Develop Operational Plan"), OPERATIONAL_PLAN_RIGHT_SIDE_FILE_NAME);
		createDataRow(EAM.text("Assess Human Financial and other resources"), "", OPERATIONAL_PLAN_RIGHT_SIDE_FILE_NAME);
		
		createProjectPlanningStartEndDateRow();
	}

	private void createProjectPlanningStartEndDateRow() throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%projectPlanningStartDate", getDashboardData(Dashboard.PSEUDO_PROJECT_PLANNING_START_DATE));
		tokenReplacementMap.put("%projectPlanningEndDate", getDashboardData(Dashboard.PSEUDO_PROJECT_PLANNING_END_DATE));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%projectPlanningStartDate - %projectPlanningEndDate"), tokenReplacementMap);

		String title = EAM.text("Plan Project Lifespan and Exit Strategy");
		createDataRow(title, rightColumnTranslatedText, OPERATIONAL_PLAN_RIGHT_SIDE_FILE_NAME);
	}

	private void addDevelopFormatMonitoringPlanRow() throws Exception
	{
		String title2b = EAM.text("2B. Develop a formal monitoring plan");
		createSubHeaderRow(title2b, DEVELOP_FORMAL_MONITORING_PLAN_RIGHT_SIDE_FILE_NAME);
		
		createDataRow(EAM.text("Define Audiences and Information Needs:"), EAM.text("Use Comments Field?"), DEVELOP_FORMAL_MONITORING_PLAN_RIGHT_SIDE_FILE_NAME);
		createIndictorsRow();
		createFactorIndictorsRow();
		createObjectivesWithRelevantIndicatorsRow();
		
	}

	private void createObjectivesWithRelevantIndicatorsRow() throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Finalize Monitoring Plan:");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%objectivesRelevantToIndicatorsPercentage", getDashboardData(Dashboard.PSEUDO_OBJECTIVES_RELEVANT_TO_INDICATORS_PERCENTAGE));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%objectivesRelevantToIndicatorsPercentage % of objectives with relevant indicators"), tokenReplacementMap);

		createDataRow(leftColumnTranslatedText, rightColumnTranslatedText, DEVELOP_FORMAL_MONITORING_PLAN_RIGHT_SIDE_FILE_NAME);
	}

	private void createIndictorsRow() throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Define Indicators:");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%keaIndicators", getDashboardData(Dashboard.PSEUDO_KEA_INDICATORS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%keaIndicators Indicators associated to KEA's"), tokenReplacementMap);

		createDataRow(leftColumnTranslatedText, rightColumnTranslatedText, DEVELOP_FORMAL_MONITORING_PLAN_RIGHT_SIDE_FILE_NAME);
	}
	
	private void createFactorIndictorsRow() throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%factorIndicators", getDashboardData(Dashboard.PSEUDO_FACTOR_INDICATORS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%factorIndicators indicators associate to Factors"), tokenReplacementMap);

		createDataRow("", rightColumnTranslatedText, DEVELOP_FORMAL_MONITORING_PLAN_RIGHT_SIDE_FILE_NAME);
	}

	private void addDevelopStrategicPlanRow() throws Exception
	{
		createSubHeaderRow(EAM.text("2A. Develop Strategic Plan"), DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME);
		
		addTargetsWithGoalRow();
		addGoalCountRow();
		addDraftStrategiesCountRow();
		addRankedDraftStrategiesCountRow();
		addStrategyCountRow();
		addStrategyWithTaxonomyCountRow();
		addResultsChainCountRow();
		addObjectivesCountRow();
		addObjectivesInResultsChainsRow();
		addFinalStrategicPlanRow();
		addNonContributingStrategiesRow();
	}

	private void addFinalStrategicPlanRow() throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Finalize Strategic Plan");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%objectivesRelevantToStrategiesPercentage", getDashboardData(Dashboard.PSEUDO_OBJECTIVES_RELEVANT_TO_STRATEGIES_PERCENTAGE));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%objectivesRelevantToStrategiesPercentage % of Objectives relevant to a Strategy"), tokenReplacementMap);

		createDataRow(leftColumnTranslatedText, rightColumnTranslatedText, DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME);
	}
	
	private void addNonContributingStrategiesRow() throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%strategiesIrrelevantToObjectivesCount", getDashboardData(Dashboard.PSEUDO_IRRELEVANT_STRATEGIES_TO_OBJECTIVES_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%strategiesIrrelevantToObjectivesCount Strategies that do not contribute to an Objective"), tokenReplacementMap);

		createDataRow("", rightColumnTranslatedText, DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void addObjectivesCountRow() throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Develop Objectives");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%objectiveCount", getDashboardData(Dashboard.PSEUDO_OBJECTIVE_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%objectiveCount Objectives Created"), tokenReplacementMap);

		createDataRow(leftColumnTranslatedText, rightColumnTranslatedText, DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME);
	}
	
	private void addObjectivesInResultsChainsRow() throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%resultsChainCount", getDashboardData(Dashboard.PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT));
		tokenReplacementMap.put("%resultsChainWithObjectiveCount", getDashboardData(Dashboard.PSEUDO_RESULTS_CHAIN_WITH_OBJECTIVE_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%resultsChainWithObjectiveCount of %resultsChainCount RCs have at least 1 objective"), tokenReplacementMap);

		createDataRow("", rightColumnTranslatedText, DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME);		
	}

	private void addResultsChainCountRow() throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Create Results Chains");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%resultsChainCount", getDashboardData(Dashboard.PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%resultsChainCount Results Chains Created"), tokenReplacementMap);

		createDataRow(leftColumnTranslatedText, rightColumnTranslatedText, DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME);	
	}

	private void addStrategyWithTaxonomyCountRow() throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Strategies with taxonomy assingments");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%strategyWithTaxonomyCount", getDashboardData(Dashboard.PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%strategyWithTaxonomyCount with taxonomy assignments"), tokenReplacementMap);

		createDataRow(leftColumnTranslatedText, rightColumnTranslatedText, DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void addStrategyCountRow() throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Create Strategies");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%strategyCount", getDashboardData(Dashboard.PSEUDO_STRATEGY_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%strategyCount Strategies Created"), tokenReplacementMap);

		createDataRow(leftColumnTranslatedText, rightColumnTranslatedText, DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void addRankedDraftStrategiesCountRow() throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Rank Draft Strategies");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%rankedDraftStrategiesCount", getDashboardData(Dashboard.PSEUDO_RANKED_DRAFT_STRATEGY_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%rankedDraftStrategiesCount Draft Strategies Ranked"), tokenReplacementMap);

		createDataRow(leftColumnTranslatedText, rightColumnTranslatedText, DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void addDraftStrategiesCountRow() throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Create Draft Strategies");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%draftStrategiesCount", getDashboardData(Dashboard.PSEUDO_DRAFT_STRATEGY_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%draftStrategiesCount Draft Strategies Created"), tokenReplacementMap);

		createDataRow(leftColumnTranslatedText, rightColumnTranslatedText, DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void addGoalCountRow() throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%goalsCount", getDashboardData(Dashboard.PSEUDO_GOAL_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%goalsCount goals Created"), tokenReplacementMap);

		createDataRow("", rightColumnTranslatedText, DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void addTargetsWithGoalRow() throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Develop Goals for Each Target:");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%targetWithGoalCount", getDashboardData(Dashboard.PSEUDO_TARGET_COUNT));
		tokenReplacementMap.put("%targetCount", getDashboardData(Dashboard.PSEUDO_TARGET_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%targetWithGoalCount of %targetCount Targets have Goals"), tokenReplacementMap);

		createDataRow(leftColumnTranslatedText, rightColumnTranslatedText, DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME);
	}
	
	private SelectableRow createDataRow(String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName) throws Exception
	{
		return createDataRow(leftColumnTranslatedText, rightColumnTranslatedText, descriptionFileName, getSummaryOverviewStepName());
	}
	
	private void createSubHeaderRow(String leftColumnTranslatedText, String rightPanelHtmlFileName) throws Exception
	{
		createSubHeaderRow(leftColumnTranslatedText, rightPanelHtmlFileName, getSummaryOverviewStepName());
	}
	
	protected String getMainDescriptionFileName()
	{
		return "dashboard/2.html";
	}
	
	private static final String DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME = "dashboard/2A.html";
	private static final String DEVELOP_FORMAL_MONITORING_PLAN_RIGHT_SIDE_FILE_NAME = "dashboard/2B.html";
	private static final String OPERATIONAL_PLAN_RIGHT_SIDE_FILE_NAME = "dashboard/2C.html";
}
