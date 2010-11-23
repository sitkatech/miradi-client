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

package org.miradi.main.menu;

import java.util.HashMap;

import org.miradi.actions.jump.ActionJumpAssessResources;
import org.miradi.actions.jump.ActionJumpAssessRisks;
import org.miradi.actions.jump.ActionJumpAssessStakeholders;
import org.miradi.actions.jump.ActionJumpDevelopMap;
import org.miradi.actions.jump.ActionJumpDiagramWizardCreateInitialModelStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardDefineAudienceStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardDefineTargetsStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardHumanWelfareTargetsStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardIdentifyDirectThreatStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardIdentifyIndirectThreatStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardResultsChainSelectStrategyStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardReviewModelAndAdjustStep;
import org.miradi.actions.jump.ActionJumpMonitoringWizardDefineIndicatorsStep;
import org.miradi.actions.jump.ActionJumpPlanProjectLifespan;
import org.miradi.actions.jump.ActionJumpPlanningWizardFinalizeMonitoringPlanStep;
import org.miradi.actions.jump.ActionJumpPlanningWizardFinalizeStrategicPlanStep;
import org.miradi.actions.jump.ActionJumpRankDraftStrategiesStep;
import org.miradi.actions.jump.ActionJumpSelectChainStep;
import org.miradi.actions.jump.ActionJumpStrategicPlanDevelopGoalStep;
import org.miradi.actions.jump.ActionJumpStrategicPlanDevelopObjectivesStep;
import org.miradi.actions.jump.ActionJumpSummaryWizardDefineProjecScope;
import org.miradi.actions.jump.ActionJumpSummaryWizardDefineTeamMembers;
import org.miradi.actions.jump.ActionJumpSummaryWizardRolesAndResponsibilities;
import org.miradi.actions.jump.ActionJumpTargetViabilityMethodChoiceStep;
import org.miradi.actions.jump.ActionJumpThreatMatrixOverviewStep;
import org.miradi.questions.OpenStandardsConceptualizeQuestion;
import org.miradi.questions.OpenStandardsPlanActionsAndMonitoringQuestion;

public class OpenStandardsCodeToMenuItemDetailsProviderMap extends HashMap<String, Class>
{
	public OpenStandardsCodeToMenuItemDetailsProviderMap()
	{
		put(OpenStandardsConceptualizeQuestion.SELECT_INTIAL_TEAM_MEMBERS_CODE, ActionJumpSummaryWizardDefineTeamMembers.class);
		put(OpenStandardsConceptualizeQuestion.AGREE_ON_ROLES_AND_RESPONSIBILITIES_CODE, ActionJumpSummaryWizardRolesAndResponsibilities.class);
		
		put(OpenStandardsConceptualizeQuestion.DEFINE_PROJECT_SCOPE_CODE, ActionJumpSummaryWizardDefineProjecScope.class);
		put(OpenStandardsConceptualizeQuestion.DEVELOP_MAP_OF_PROJECT_AREA_CODE, ActionJumpDevelopMap.class);
		put(OpenStandardsConceptualizeQuestion.SELECT_CONSERVATION_TARGETS_CODE, ActionJumpDiagramWizardDefineTargetsStep.class);
		put(OpenStandardsConceptualizeQuestion.ADD_HUMAN_WELFARE_TARGETS_IF_DESIRED_CODE, ActionJumpDiagramWizardHumanWelfareTargetsStep.class);
		put(OpenStandardsConceptualizeQuestion.DESCRIBE_STATUS_OF_TARGETS_CODE, ActionJumpTargetViabilityMethodChoiceStep.class);
		
		put(OpenStandardsConceptualizeQuestion.IDENTIFY_DIRECT_THREATS_CODE, ActionJumpDiagramWizardIdentifyDirectThreatStep.class);
		put(OpenStandardsConceptualizeQuestion.RANK_DIRECT_THREATS_CODE, ActionJumpThreatMatrixOverviewStep.class);
		
		put(OpenStandardsConceptualizeQuestion.IDENTIFY_INDIRECT_THREATS_AND_OPPORTUNITIES_CODE, ActionJumpDiagramWizardIdentifyIndirectThreatStep.class);
		put(OpenStandardsConceptualizeQuestion.ASSESS_STAKEHOLDERS_CODE, ActionJumpAssessStakeholders.class);
		put(OpenStandardsConceptualizeQuestion.CREATE_INITIAL_CONCEPTUAL_MODEL_CODE, ActionJumpDiagramWizardCreateInitialModelStep.class);
		put(OpenStandardsConceptualizeQuestion.GROUND_THRUTH_AND_REVISE_MODEL_CODE, ActionJumpDiagramWizardReviewModelAndAdjustStep.class);
		
				
		put(OpenStandardsPlanActionsAndMonitoringQuestion.DEVELOP_GOALS_FOR_EACH_TARGET_CODE ,ActionJumpStrategicPlanDevelopGoalStep.class);
		put(OpenStandardsPlanActionsAndMonitoringQuestion.IDENTIFY_KEY_FACTORS_AND_DRAFT_STRATEGIES_CODE, ActionJumpSelectChainStep.class);
		put(OpenStandardsPlanActionsAndMonitoringQuestion.RANK_DRAFT_STRATEGIES_CODE, ActionJumpRankDraftStrategiesStep.class);
		put(OpenStandardsPlanActionsAndMonitoringQuestion.CREATE_RESULTS_CHAINS_SHOWING_ASSUMPTIONS_CODE, ActionJumpDiagramWizardResultsChainSelectStrategyStep.class);
		put(OpenStandardsPlanActionsAndMonitoringQuestion.DEVELOP_OBJECTIVES_CODE, ActionJumpStrategicPlanDevelopObjectivesStep.class);
		put(OpenStandardsPlanActionsAndMonitoringQuestion.FINALIZE_STRATEGIC_PLAN_CODE, ActionJumpPlanningWizardFinalizeStrategicPlanStep.class);
		put(OpenStandardsPlanActionsAndMonitoringQuestion.DEFINE_AUDIENCES_AND_INFORMATION_NEEDS_CODE, ActionJumpDiagramWizardDefineAudienceStep.class);
		put(OpenStandardsPlanActionsAndMonitoringQuestion.DEFINE_INDICATORS_CODE, ActionJumpMonitoringWizardDefineIndicatorsStep.class);
		put(OpenStandardsPlanActionsAndMonitoringQuestion.FINALIZE_MONITORING_PLAN_CODE, ActionJumpPlanningWizardFinalizeMonitoringPlanStep.class);
		put(OpenStandardsPlanActionsAndMonitoringQuestion.ASSESS_HUMAN_FINANCIAL_AND_OTHER_RESOURCES_CODE, ActionJumpAssessResources.class);
		put(OpenStandardsPlanActionsAndMonitoringQuestion.ASSESS_RISKS_CODE, ActionJumpAssessRisks.class);
		put(OpenStandardsPlanActionsAndMonitoringQuestion.PLAN_PROJECT_LIFESPAN_AND_EXIT_STRATEGY_CODE, ActionJumpPlanProjectLifespan.class);
	}
}
