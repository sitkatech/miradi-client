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

package org.miradi.dialogs.base;

import java.util.HashMap;

import org.miradi.actions.AbstractJumpMenuAction;
import org.miradi.dialogs.dashboard.AbstractLongDescriptionProvider;
import org.miradi.main.EAM;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.OpenStandardsPlanActionsAndMonitoringQuestion;

public class OpenStandardsPlanActionsAndMonitoringQuestionPanel extends AbstractOpenStandardsQuestionPanel
{
	public OpenStandardsPlanActionsAndMonitoringQuestionPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, new OpenStandardsPlanActionsAndMonitoringQuestion());
	}
	
	@Override
	protected void addRow(ChoiceItem choiceItem, int indentCount) throws Exception
	{
		super.addRow(choiceItem, indentCount);
		
		String code = choiceItem.getCode();
		AbstractLongDescriptionProvider longDescriptionProvider = choiceItem.getLongDescriptionProvider();
		AbstractJumpMenuAction action = getMainWindow().getActions().getJumpMenuAction(code);
		String stepName = getMainWindow().getWizardManager().stripJumpPrefix(action.getClass());
		longDescriptionProvider.setWizardStepName(stepName);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.DEVELOP_GOALS_FOR_EACH_TARGET_CODE))
			addDevelopGoalsForEachTargetRow(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.IDENTIFY_KEY_FACTORS_AND_DRAFT_STRATEGIES_CODE))
			addIdentifyKeyFactorsAndDraftStrategiesRow(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.RANK_DRAFT_STRATEGIES_CODE))
			addRankDraftStrategiesrow(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.CREATE_RESULTS_CHAINS_SHOWING_ASSUMPTIONS_CODE))
			addCreateResultsChainsShowingAssumptionsRow(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.DEVELOP_OBJECTIVES_CODE))
			addDevelopObjectivesRow(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.FINALIZE_STRATEGIC_PLAN_CODE))
			addFinalizeStrategicPlanRow(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.FINALIZE_MONITORING_PLAN_CODE))
			addFinalizeMonitoringPlanRow(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.DEFINE_AUDIENCES_AND_INFORMATION_NEEDS_CODE))
			addDefineAudiencesAndInformationNeedsRow(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.DEFINE_INDICATORS_CODE))
			addDefineIndicatorsRow(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.ASSESS_HUMAN_FINANCIAL_AND_OTHER_RESOURCES_CODE))
			addAssessHumanFinacialAndOtherResourcesRow(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.ASSESS_RISKS_CODE))
			addAssessRisksRow(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.PLAN_PROJECT_LIFESPAN_AND_EXIT_STRATEGY_CODE))
			addPlanProjectLifespanAndExitStrategyRow(longDescriptionProvider, indentCount);
	}

	private void addDevelopGoalsForEachTargetRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%targetWithGoalCount", getDashboardData(Dashboard.PSEUDO_TARGET_COUNT));
		tokenReplacementMap.put("%targetCount", getDashboardData(Dashboard.PSEUDO_TARGET_COUNT));
		String text = EAM.text("%targetWithGoalCount of %targetCount Targets have Goals");
		addRow(tokenReplacementMap, text, longDescriptionProvider, indentCount);
		
		HashMap<String, String> tokenReplacementMap2 = new HashMap<String, String>();
		tokenReplacementMap2.put("%goalsCount", getDashboardData(Dashboard.PSEUDO_GOAL_COUNT));
		addRow(tokenReplacementMap2, EAM.text("%goalsCount goals Created"), longDescriptionProvider, indentCount);
	}

	private void addIdentifyKeyFactorsAndDraftStrategiesRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%draftStrategiesCount", getDashboardData(Dashboard.PSEUDO_DRAFT_STRATEGY_COUNT));
		String text = EAM.text("%draftStrategiesCount Draft Strategies Created");

		addRow(tokenReplacementMap, text, longDescriptionProvider, indentCount);
	}

	private void addRankDraftStrategiesrow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%rankedDraftStrategiesCount", getDashboardData(Dashboard.PSEUDO_RANKED_DRAFT_STRATEGY_COUNT));
		String text = EAM.text("%rankedDraftStrategiesCount Draft Strategies Ranked");

		addRow(tokenReplacementMap, text, longDescriptionProvider, indentCount);
	}

	private void addCreateResultsChainsShowingAssumptionsRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		HashMap<String, String> tokenReplacementMap1 = new HashMap<String, String>();
		tokenReplacementMap1.put("%strategyCount", getDashboardData(Dashboard.PSEUDO_STRATEGY_COUNT));
		String text = EAM.text("%strategyCount Strategies Created");
		addRow(tokenReplacementMap1, text, longDescriptionProvider, indentCount);
		
		HashMap<String, String> tokenReplacementMap2 = new HashMap<String, String>();
		tokenReplacementMap2.put("%strategyWithTaxonomyCount", getDashboardData(Dashboard.PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT));
		addRow(tokenReplacementMap2, EAM.text("%strategyWithTaxonomyCount with taxonomy assignments"), longDescriptionProvider, indentCount);
		
		HashMap<String, String> tokenReplacementMap3 = new HashMap<String, String>();
		tokenReplacementMap3.put("%resultsChainCount", getDashboardData(Dashboard.PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT));
		addRow(tokenReplacementMap3, EAM.text("%resultsChainCount Results Chains Created"), longDescriptionProvider, indentCount);
	}

	private void addDevelopObjectivesRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		HashMap<String, String> tokenReplacementMap1 = new HashMap<String, String>();
		tokenReplacementMap1.put("%objectiveCount", getDashboardData(Dashboard.PSEUDO_OBJECTIVE_COUNT));
		addRow(tokenReplacementMap1, EAM.text("%objectiveCount Objectives Created"), longDescriptionProvider, indentCount);
		
		HashMap<String, String> tokenReplacementMap2 = new HashMap<String, String>();
		tokenReplacementMap2.put("%resultsChainCount", getDashboardData(Dashboard.PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT));
		tokenReplacementMap2.put("%resultsChainWithObjectiveCount", getDashboardData(Dashboard.PSEUDO_RESULTS_CHAIN_WITH_OBJECTIVE_COUNT));
		addRow(tokenReplacementMap2, EAM.text("%resultsChainWithObjectiveCount of %resultsChainCount RCs have at least 1 objective"), longDescriptionProvider, indentCount);
	}

	private void addFinalizeStrategicPlanRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%objectivesRelevantToStrategiesPercentage", getDashboardData(Dashboard.PSEUDO_OBJECTIVES_RELEVANT_TO_STRATEGIES_PERCENTAGE));
		addRow(tokenReplacementMap, EAM.text("%objectivesRelevantToStrategiesPercentage % of Objectives relevant to a Strategy"), longDescriptionProvider, indentCount);
		
		HashMap<String, String> tokenReplacementMap2 = new HashMap<String, String>();
		tokenReplacementMap2.put("%strategiesIrrelevantToObjectivesCount", getDashboardData(Dashboard.PSEUDO_IRRELEVANT_STRATEGIES_TO_OBJECTIVES_COUNT));
		addRow(tokenReplacementMap2, EAM.text("%strategiesIrrelevantToObjectivesCount Strategies that do not contribute to an Objective"), longDescriptionProvider, indentCount);
	}

	private void addFinalizeMonitoringPlanRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%objectivesRelevantToIndicatorsPercentage", getDashboardData(Dashboard.PSEUDO_OBJECTIVES_RELEVANT_TO_INDICATORS_PERCENTAGE));
		addRow(tokenReplacementMap, EAM.text("%objectivesRelevantToIndicatorsPercentage % of objectives with relevant indicators"), longDescriptionProvider, indentCount);
	}

	private void addDefineAudiencesAndInformationNeedsRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRow("", EAM.text("Use Comments Field?"), longDescriptionProvider, indentCount);
	}

	private void addDefineIndicatorsRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%keaIndicators", getDashboardData(Dashboard.PSEUDO_KEA_INDICATORS_COUNT));
		addRow(tokenReplacementMap, EAM.text("%keaIndicators Indicators associated to KEA's"), longDescriptionProvider, indentCount);
		
		HashMap<String, String> tokenReplacementMap2 = new HashMap<String, String>();
		tokenReplacementMap2.put("%factorIndicators", getDashboardData(Dashboard.PSEUDO_FACTOR_INDICATORS_COUNT));
		addRow(tokenReplacementMap2, EAM.text("%factorIndicators indicators associate to Factors"), longDescriptionProvider, indentCount);
	}

	private void addAssessHumanFinacialAndOtherResourcesRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount)
	{
	}

	private void addAssessRisksRow(AbstractLongDescriptionProvider longDescriptionProvider,	int indentCount)
	{
	}

	private void addPlanProjectLifespanAndExitStrategyRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%projectPlanningStartDate", getDashboardData(Dashboard.PSEUDO_PROJECT_PLANNING_START_DATE));
		tokenReplacementMap.put("%projectPlanningEndDate", getDashboardData(Dashboard.PSEUDO_PROJECT_PLANNING_END_DATE));
		addRow(tokenReplacementMap, EAM.text("%projectPlanningStartDate - %projectPlanningEndDate"), longDescriptionProvider, indentCount);
	}

	@Override
	public String getPanelDescription()
	{
		return "OpenStandardsPlanActionsAndMonitoringQuestionPanel";
	}
}
