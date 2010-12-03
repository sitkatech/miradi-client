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
		addRowHelper(EAM.text("%X of %Y Targets have Goals"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_TARGETS_WITH_GOALS_COUNT, Dashboard.PSEUDO_TARGET_COUNT);
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X goals Created"), Dashboard.PSEUDO_GOAL_COUNT);
	}

	private void addIdentifyKeyFactorsAndDraftStrategiesRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X Draft Strategies Created"), Dashboard.PSEUDO_DRAFT_STRATEGY_COUNT);
	}

	private void addRankDraftStrategiesrow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X Draft Strategies Ranked"), Dashboard.PSEUDO_RANKED_DRAFT_STRATEGY_COUNT);
	}

	private void addCreateResultsChainsShowingAssumptionsRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X Strategies Created"), Dashboard.PSEUDO_STRATEGY_COUNT);
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X with taxonomy assignments"), Dashboard.PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT);
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X Results Chains Created"), Dashboard.PSEUDO_RESULTS_CHAIN_COUNT);
	}

	private void addDevelopObjectivesRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X Objectives Created"), Dashboard.PSEUDO_OBJECTIVE_COUNT);
		addRowHelper(EAM.text("%Y of %X RCs have at least 1 objective"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT, Dashboard.PSEUDO_RESULTS_CHAIN_WITH_OBJECTIVE_COUNT);
	}

	private void addFinalizeStrategicPlanRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X % of Objectives relevant to a Strategy"), Dashboard.PSEUDO_OBJECTIVES_RELEVANT_TO_STRATEGIES_PERCENTAGE);		
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X Strategies that do not contribute to an Objective"), Dashboard.PSEUDO_IRRELEVANT_STRATEGIES_TO_OBJECTIVES_COUNT);
	}

	private void addFinalizeMonitoringPlanRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X % of objectives with relevant indicators"), Dashboard.PSEUDO_OBJECTIVES_RELEVANT_TO_INDICATORS_PERCENTAGE);
	}

	private void addDefineAudiencesAndInformationNeedsRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRow("", EAM.text("Use Comments Field?"), longDescriptionProvider, indentCount);
	}

	private void addDefineIndicatorsRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X Indicators associated to KEA's"), Dashboard.PSEUDO_KEA_INDICATORS_COUNT);
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X indicators associate to Factors"), Dashboard.PSEUDO_FACTOR_INDICATORS_COUNT);
	}

	private void addAssessHumanFinacialAndOtherResourcesRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount)
	{
	}

	private void addAssessRisksRow(AbstractLongDescriptionProvider longDescriptionProvider,	int indentCount)
	{
	}

	private void addPlanProjectLifespanAndExitStrategyRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(EAM.text("%Y - %X"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_PROJECT_PLANNING_START_DATE, Dashboard.PSEUDO_PROJECT_PLANNING_END_DATE);
	}

	@Override
	public String getPanelDescription()
	{
		return "OpenStandardsPlanActionsAndMonitoringQuestionPanel";
	}
}
