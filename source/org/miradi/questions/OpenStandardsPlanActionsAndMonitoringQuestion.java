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

package org.miradi.questions;

import org.miradi.dialogs.dashboard.HtmlResourceLongDescriptionProvider;
import org.miradi.main.EAM;

public class OpenStandardsPlanActionsAndMonitoringQuestion extends DynamicChoiceWithRootChoiceItem
{
	@Override
	protected ChoiceItemWithChildren createHeaderChoiceItem() throws Exception
	{
		ChoiceItemWithChildren headerChoiceItem = new ChoiceItemWithChildren(PLAN_ACTIONS_AND_MONITORING_CODE, getPlanActionsAndMonitoringHeaderLabel(), new HtmlResourceLongDescriptionProvider(MAIN_DESCRIPTION_FILENAME));
		
		ChoiceItemWithChildren processStep2a = new ChoiceItemWithChildren(PROCESS_STEP_2A_CODE, getProcessStep2aLabel(), EAM.emptyText(), new HtmlResourceLongDescriptionProvider(DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME));
		headerChoiceItem.addChild(processStep2a);
		processStep2a.addChild(new ChoiceItem(DEVELOP_GOALS_FOR_EACH_TARGET_CODE, getDevelopGoalsForEachTargetLabel(), new HtmlResourceLongDescriptionProvider(DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME)));
		processStep2a.addChild(new ChoiceItem(IDENTIFY_KEY_FACTORS_AND_DRAFT_STRATEGIES_CODE, getIdentifyKeyFactorsAndDraftStrategiesLabel(), new HtmlResourceLongDescriptionProvider(DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME)));
		processStep2a.addChild(new ChoiceItem(RANK_DRAFT_STRATEGIES_CODE, getRankDraftStrategiesLabel(), new HtmlResourceLongDescriptionProvider(DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME)));
		processStep2a.addChild(new ChoiceItem(CREATE_RESULTS_CHAINS_SHOWING_ASSUMPTIONS_CODE, getCreateResultsChainsShowingAssumptionsLabel(), new HtmlResourceLongDescriptionProvider(DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME)));
		processStep2a.addChild(new ChoiceItem(DEVELOP_OBJECTIVES_CODE, getDevelopObjectivesLabel(), new HtmlResourceLongDescriptionProvider(DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME)));
		processStep2a.addChild(new ChoiceItem(FINALIZE_STRATEGIC_PLAN_CODE, getFinalizeStrategicPlanLabel(), new HtmlResourceLongDescriptionProvider(DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME)));
		processStep2a.addChild(new ChoiceItem(FINALIZE_MONITORING_PLAN_CODE, getFinalizeMonitoringPlanLabel(), new HtmlResourceLongDescriptionProvider(DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME)));
		
		ChoiceItemWithChildren processStep2bChoiceItem = new ChoiceItemWithChildren(PROCESS_STEP_2B_CODE, getProcessStep2bLabel(), EAM.emptyText(), new HtmlResourceLongDescriptionProvider(DEVELOP_FORMAL_MONITORING_PLAN_RIGHT_SIDE_FILE_NAME));
		headerChoiceItem.addChild(processStep2bChoiceItem);
		processStep2bChoiceItem.addChild(new ChoiceItem(DEFINE_AUDIENCES_AND_INFORMATION_NEEDS_CODE, getDefineAudiencesAndInformationNeedsLabel(), new HtmlResourceLongDescriptionProvider(DEVELOP_FORMAL_MONITORING_PLAN_RIGHT_SIDE_FILE_NAME)));
		processStep2bChoiceItem.addChild(new ChoiceItem(DEFINE_INDICATORS_CODE, getDefineIndicatorsLabel(), new HtmlResourceLongDescriptionProvider(DEVELOP_FORMAL_MONITORING_PLAN_RIGHT_SIDE_FILE_NAME)));
		
		ChoiceItemWithChildren processStep2cChoiceItem = new ChoiceItemWithChildren(PROCESS_STEP_2C_CODE, getProcessStep2cLabel(), EAM.emptyText(), new HtmlResourceLongDescriptionProvider(OPERATIONAL_PLAN_RIGHT_SIDE_FILE_NAME));
		headerChoiceItem.addChild(processStep2cChoiceItem);
		processStep2cChoiceItem.addChild(new ChoiceItem(ASSESS_HUMAN_FINANCIAL_AND_OTHER_RESOURCES_CODE, getAssessHumanFinacialAndOtherResourcesLabel(), new HtmlResourceLongDescriptionProvider(OPERATIONAL_PLAN_RIGHT_SIDE_FILE_NAME)));
		processStep2cChoiceItem.addChild(new ChoiceItem(ASSESS_RISKS_CODE, getAssessRisksLabel(), new HtmlResourceLongDescriptionProvider(OPERATIONAL_PLAN_RIGHT_SIDE_FILE_NAME)));
		processStep2cChoiceItem.addChild(new ChoiceItem(PLAN_PROJECT_LIFESPAN_AND_EXIT_STRATEGY_CODE, getPlanProjectLifespanAndExitStrategyLabel(), new HtmlResourceLongDescriptionProvider(OPERATIONAL_PLAN_RIGHT_SIDE_FILE_NAME)));
		
		return headerChoiceItem;
	}

	public static String getPlanActionsAndMonitoringHeaderLabel()
	{
		return EAM.text("Menu|2. Plan Actions and Monitoring");
	}
	
	public static String getProcessStep2aLabel()
	{
		return EAM.text("ProcessStep|2A. Develop Strategic Plan");
	}
	
	public static String getProcessStep2bLabel()
	{
		return EAM.text("ProcessStep|2B. Develop Monitoring Plan");
	}

	public static String getProcessStep2cLabel()
	{
		return EAM.text("ProcessStep|2C. Develop Operational Plan");
	}

	public static String getDevelopGoalsForEachTargetLabel()
	{
		return EAM.text("Develop goals for each target"); 
	}
		
	public static String getIdentifyKeyFactorsAndDraftStrategiesLabel()
	{
		return EAM.text("Identify \"key factors\" and draft strategies"); 
	}
	
	public static String getRankDraftStrategiesLabel()
	{
		return EAM.text("Rank draft strategies"); 
	}
	
	public static String getCreateResultsChainsShowingAssumptionsLabel()
	{
		return EAM.text("Create results chains showing assumptions"); 
	}

	public static String getDevelopObjectivesLabel()
	{
		return EAM.text("Develop objectives"); 
	}
	
	public static String getFinalizeStrategicPlanLabel()
	{
		return EAM.text("Finalize strategic plan");
	}
	
	public static String getDefineAudiencesAndInformationNeedsLabel()
	{
		return EAM.text("Define audiences and information needs");
	}
	
	public static String getDefineIndicatorsLabel()
	{
		return EAM.text("Define indicators"); 
	}
	
	public static String getFinalizeMonitoringPlanLabel()
	{
		return EAM.text("Finalize monitoring plan");
	}

	public static String getAssessHumanFinacialAndOtherResourcesLabel()
	{
		return EAM.text("Assess human, financial and other resources");
	}
	
	public static String getAssessRisksLabel()
	{
		return EAM.text("Assess risks");
	}
	
	public static String getPlanProjectLifespanAndExitStrategyLabel()
	{
		return EAM.text("Plan project lifespan and exit strategy");
	}

	public static final String PLAN_ACTIONS_AND_MONITORING_CODE = "PlanActionsAndMonitoring";
	public static final String PROCESS_STEP_2A_CODE = "ProcessStep2A";
	public static final String PROCESS_STEP_2B_CODE = "ProcessStep2B";
	public static final String PROCESS_STEP_2C_CODE = "ProcessStep2C";
	public static final String DEVELOP_GOALS_FOR_EACH_TARGET_CODE = "DevelopGoalsGorEachTarget";
	public static final String IDENTIFY_KEY_FACTORS_AND_DRAFT_STRATEGIES_CODE = "IdentifyKeyFactorsAndDraftStrategies";
	public static final String RANK_DRAFT_STRATEGIES_CODE = "RankDraftStrategies";
	public static final String CREATE_RESULTS_CHAINS_SHOWING_ASSUMPTIONS_CODE = "CreateResultsChainsShowingAssumptions";
	public static final String DEVELOP_OBJECTIVES_CODE = "DevelopObjectives";
	public static final String FINALIZE_STRATEGIC_PLAN_CODE = "FinalizeStrategicPlan";
	public static final String DEFINE_AUDIENCES_AND_INFORMATION_NEEDS_CODE = "DefineAudiencesAndInformationNeeds";
	public static final String DEFINE_INDICATORS_CODE = "DefineIndicators";
	public static final String FINALIZE_MONITORING_PLAN_CODE = "FinalizeMonitoringPlan";
	public static final String ASSESS_HUMAN_FINANCIAL_AND_OTHER_RESOURCES_CODE = "AssessHumanFinacialAndOtherResources";
	public static final String ASSESS_RISKS_CODE = "AssessRisks";
	public static final String PLAN_PROJECT_LIFESPAN_AND_EXIT_STRATEGY_CODE = "PlanProjectLifespanAndExitStrategy";
	
	public static final String MAIN_DESCRIPTION_FILENAME =  "dashboard/2.html"; 
	private static final String DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME = "dashboard/2A.html";
	private static final String DEVELOP_FORMAL_MONITORING_PLAN_RIGHT_SIDE_FILE_NAME = "dashboard/2B.html";
	private static final String OPERATIONAL_PLAN_RIGHT_SIDE_FILE_NAME = "dashboard/2C.html";
}
