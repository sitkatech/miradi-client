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
	protected void addRow(ChoiceItem choiceItem, int level) throws Exception
	{
		super.addRow(choiceItem, level);
		
		String code = choiceItem.getCode();
		AbstractLongDescriptionProvider longDescriptionProvider = choiceItem.getLongDescriptionProvider();
		AbstractJumpMenuAction action = getMainWindow().getActions().getJumpMenuAction(code);
		String stepName = getMainWindow().getWizardManager().stripJumpPrefix(action.getClass());
		longDescriptionProvider.setWizardStepName(stepName);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.DEVELOP_GOALS_FOR_EACH_TARGET_CODE))
			addDevelopGoalsForEachTargetRow(longDescriptionProvider, level);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.IDENTIFY_KEY_FACTORS_AND_DRAFT_STRATEGIES_CODE))
			addIdentifyKeyFactorsAndDraftStrategiesRow(longDescriptionProvider, level);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.RANK_DRAFT_STRATEGIES_CODE))
			addRankDraftStrategiesrow(longDescriptionProvider, level);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.CREATE_RESULTS_CHAINS_SHOWING_ASSUMPTIONS_CODE))
			addCreateResultsChainsShowingAssumptionsRow(longDescriptionProvider, level);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.DEVELOP_OBJECTIVES_CODE))
			addDevelopObjectivesRow(longDescriptionProvider, level);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.FINALIZE_STRATEGIC_PLAN_CODE))
			addFinalizeStrategicPlanRow(longDescriptionProvider, level);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.FINALIZE_MONITORING_PLAN_CODE))
			addFinalizeMonitoringPlanRow(longDescriptionProvider, level);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.DEFINE_AUDIENCES_AND_INFORMATION_NEEDS_CODE))
			addDefineAudiencesAndInformationNeedsRow(longDescriptionProvider, level);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.DEFINE_INDICATORS_CODE))
			addDefineIndicatorsRow(longDescriptionProvider, level);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.ASSESS_HUMAN_FINANCIAL_AND_OTHER_RESOURCES_CODE))
			addAssessHumanFinacialAndOtherResourcesRow(longDescriptionProvider, level);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.ASSESS_RISKS_CODE))
			addAssessRisksRow(longDescriptionProvider, level);
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.PLAN_PROJECT_LIFESPAN_AND_EXIT_STRATEGY_CODE))
			addPlanProjectLifespanAndExitStrategyRow(longDescriptionProvider, level);
	}

	private void addDevelopGoalsForEachTargetRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowWithTemplateAndTwoPseudoFields(EAM.text("%X of %Y Targets have Goals"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_TARGETS_WITH_GOALS_COUNT, Dashboard.PSEUDO_TARGET_COUNT);
		addRowWithTemplateAndPseudoField(EAM.text("%X goals Created"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_GOAL_COUNT);
	}

	private void addIdentifyKeyFactorsAndDraftStrategiesRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowWithTemplateAndPseudoField(EAM.text("%X Draft Strategies Created"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_DRAFT_STRATEGY_COUNT);
	}

	private void addRankDraftStrategiesrow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowWithTemplateAndPseudoField(EAM.text("%X Draft Strategies Ranked"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_RANKED_DRAFT_STRATEGY_COUNT);
	}

	private void addCreateResultsChainsShowingAssumptionsRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowWithTemplateAndPseudoField(EAM.text("%X Strategies Created"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_STRATEGY_COUNT);
		addRowWithTemplateAndPseudoField(EAM.text("%X with taxonomy assignments"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT);
		addRowWithTemplateAndPseudoField(EAM.text("%X Results Chains Created"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_RESULTS_CHAIN_COUNT);
	}

	private void addDevelopObjectivesRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowWithTemplateAndPseudoField(EAM.text("%X Objectives Created"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_OBJECTIVE_COUNT);
		addRowWithTemplateAndTwoPseudoFields(EAM.text("%Y of %X RCs have at least 1 objective"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT, Dashboard.PSEUDO_RESULTS_CHAIN_WITH_OBJECTIVE_COUNT);
	}

	private void addFinalizeStrategicPlanRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowWithTemplateAndPseudoField(EAM.text("%X % of Objectives relevant to a Strategy"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_OBJECTIVES_RELEVANT_TO_STRATEGIES_PERCENTAGE);		
		addRowWithTemplateAndPseudoField(EAM.text("%X Strategies that do not contribute to an Objective"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_IRRELEVANT_STRATEGIES_TO_OBJECTIVES_COUNT);
	}

	private void addFinalizeMonitoringPlanRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowWithTemplateAndPseudoField(EAM.text("%X % of objectives with relevant indicators"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_OBJECTIVES_RELEVANT_TO_INDICATORS_PERCENTAGE);
	}

	private void addDefineAudiencesAndInformationNeedsRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRow("", EAM.text("Use Comments Field?"), longDescriptionProvider, indentCount);
	}

	private void addDefineIndicatorsRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowWithTemplateAndPseudoField(EAM.text("%X Indicators associated to KEA's"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_KEA_INDICATORS_COUNT);
		addRowWithTemplateAndPseudoField(EAM.text("%X indicators associate to Factors"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_FACTOR_INDICATORS_COUNT);
	}

	private void addAssessHumanFinacialAndOtherResourcesRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount)
	{
	}

	private void addAssessRisksRow(AbstractLongDescriptionProvider longDescriptionProvider,	int indentCount)
	{
	}

	private void addPlanProjectLifespanAndExitStrategyRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowWithTemplateAndTwoPseudoFields(EAM.text("%Y - %X"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_PROJECT_PLANNING_START_DATE, Dashboard.PSEUDO_PROJECT_PLANNING_END_DATE);
	}

	@Override
	public String getPanelDescription()
	{
		return "OpenStandardsPlanActionsAndMonitoringQuestionPanel";
	}
}
