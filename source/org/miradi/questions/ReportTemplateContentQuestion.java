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
package org.miradi.questions;

import org.miradi.main.EAM;

public class ReportTemplateContentQuestion extends StaticChoiceQuestion
{
	public ReportTemplateContentQuestion()
	{
		super(getContentChoices());
	}

	static ChoiceItem[] getContentChoices()
	{		
		return new ChoiceItem[] {
				new ChoiceItem(SUMMARY_VIEW_PROJECT_TAB_CODE, EAM.text("Project Summary")),
				new ChoiceItem(SUMMARY_VIEW_TEAM_TAB_CODE, getTeamLabel()),
				new ChoiceItem(SUMMARY_VIEW_ORGANIZATION_TAB_CODE, getOraganizationLabel()),
				new ChoiceItem(SUMMARY_VIEW_SCOPE_TAB_CODE, EAM.text("Scope")),
				new ChoiceItem(SUMMARY_VIEW_LOCATION_TAB_CODE, EAM.text("Location")),
				new ChoiceItem(SUMMARY_VIEW_PLANNING_TAB_CODE, EAM.text("Planning Parameters")),
				new ChoiceItem(SUMMARY_VIEW_TNC_TAB_CODE, EAM.text("TNC")),
				new ChoiceItem(SUMMARY_VIEW_WWF_TAB_CODE, EAM.text("WWF")),
				new ChoiceItem(SUMMARY_VIEW_WCS_TAB_CODE, EAM.text("WCS")),
				new ChoiceItem(SUMMARY_VIEW_RARE_TAB_CODE, EAM.text("RARE")),
				new ChoiceItem(SUMMARY_VIEW_FOS_TAB_CODE, EAM.text("FOS")),
				
				new ChoiceItem(DIAGRAM_VIEW_CONCEPTUAL_MODEL_TAB_CODE, EAM.text("Conceptual Model")),
				new ChoiceItem(DIAGRAM_VIEW_RESULTS_CHAINS_TAB_CODE, EAM.text("Results Chains")),
				
				new ChoiceItem(TARGET_VIABILITY_VIEW_VIABILITY_TAB_TABLE_CODE, getTargetViabilityTableLabel()),
				new ChoiceItem(TARGET_VIABILITY_VIEW_VIABILITY_TAB_DETAILS_CODE, getTargetViabilityDetailsLabel()),
				
				new ChoiceItem(THREAT_RATING_VIEW_CODE, getThreatRatingsLabel()),
				new ChoiceItem(THREAT_RATING_DETAILS_CODE, getThreatRatingDetailsLabel()),
				
				new ChoiceItem(ACTION_PLAN_OBJECTIVE_BASED_REPORT_CODE, getActionPlanObjectiveBasedActionPlanLabel()),
				new ChoiceItem(ACTION_PLAN_STRATEGY_BASED_REPORT_CODE, getActionPlanStrategyBasedActionPlanLabel()),
				new ChoiceItem(PLANNING_VIEW_MONITORING_PLAN_CODE, getMonitoringPlanLabel()),
				new ChoiceItem(PLANNING_VIEW_WORK_PLAN_CODE, getWorkPlanLabel()),
				new ChoiceItem(PLANNING_VIEW_RESOURCES_CODE, getResourcesLabel()),
				new ChoiceItem(PLANNING_VIEW_ACCOUNTING_CODE_CODE, getAccountingCodesLabel()),
				new ChoiceItem(PLANNING_VIEW_FUNDING_SOURCE_CODE, getFundingSourcesLabel()),
				new ChoiceItem(BUDGET_CATEGORY_ONE_CODE, getCategoryOneLabel()),
				new ChoiceItem(BUDGET_CATEGORY_TWO_CODE, getCategoryTwoLabel()),
				new ChoiceItem(ANALYSIS_CODE, getAnalysisLabel()),
				new ChoiceItem(PROGRESS_REPORT_CODE, getProgressReportLabel()),
				new ChoiceItem(LEGEND_TABLE_REPORT_CODE, getLegendTableLabel()),
		};
	}

	public static String getTargetViabilityTableLabel()
	{
		return EAM.text("Target Viability - Table");
	}
	
	public static String getTargetViabilityDetailsLabel()
	{
		return EAM.text("Target Viability - Details");
	}

	public static String getLegendTableLabel()
	{
		return EAM.text("Legend Table");
	}

	public static String getProgressReportLabel()
	{
		return EAM.text("Progress Report");
	}

	public static String getFundingSourcesLabel()
	{
		return EAM.text("Funding Sources");
	}
	
	public static String getCategoryOneLabel()
	{
		return EAM.text("Budget Category One");
	}
	
	public static String getCategoryTwoLabel()
	{
		return EAM.text("Budget Category Two");
	}
	
	public static String getAnalysisLabel()
	{
		return EAM.text("Analysis");
	}

	public static String getAccountingCodesLabel()
	{
		return EAM.text("Accounting Codes");
	}

	public static String getResourcesLabel()
	{
		return EAM.text("Resources");
	}

	public static String getWorkPlanLabel()
	{
		return EAM.text("Work Plan");
	}

	public static String getMonitoringPlanLabel()
	{
		return EAM.text("Monitoring Plan");
	}

	public static String getActionPlanObjectiveBasedActionPlanLabel()
	{
		return EAM.text("Objectives contain Strategies");
	}
	
	public static String getActionPlanStrategyBasedActionPlanLabel()
	{
		return EAM.text("Strategies contain Objectives");
	}

	public static String getThreatRatingsLabel()
	{
		return EAM.text("Threat Ratings - Table");
	}
	
	public static String getThreatRatingDetailsLabel()
	{
		return EAM.text("Threat Ratings - Details");
	}
	
	public static String getTeamLabel()
	{
		return EAM.text("Team");
	}
	
	public static String getOraganizationLabel()
	{
		return EAM.text("Organization");
	}
	
	public static final String SUMMARY_VIEW_PROJECT_TAB_CODE = "SummaryViewProjectTab";
	public static final String SUMMARY_VIEW_TEAM_TAB_CODE = "SummaryViewTeamTab";
	public static final String SUMMARY_VIEW_ORGANIZATION_TAB_CODE = "SummaryViewOrganizationTab";
	public static final String SUMMARY_VIEW_SCOPE_TAB_CODE = "SummaryViewScopeTab";
	public static final String SUMMARY_VIEW_LOCATION_TAB_CODE = "SummaryViewLocationTab";
	public static final String SUMMARY_VIEW_PLANNING_TAB_CODE = "SummaryViewPlanningTab";
	public static final String SUMMARY_VIEW_TNC_TAB_CODE = "SummaryViewTncTab";
	public static final String SUMMARY_VIEW_WWF_TAB_CODE = "SummaryViewWwfTab";
	public static final String SUMMARY_VIEW_WCS_TAB_CODE = "SummaryViewWcsTab";
	public static final String SUMMARY_VIEW_RARE_TAB_CODE = "SummaryViewRareTab";
	public static final String SUMMARY_VIEW_FOS_TAB_CODE = "SummaryViewFosTab";
	public static final String DIAGRAM_VIEW_CONCEPTUAL_MODEL_TAB_CODE = "DiagramViewConceptualModelTab";
	public static final String DIAGRAM_VIEW_RESULTS_CHAINS_TAB_CODE = "DiagramViewResultsChainTab";
	public static final String TARGET_VIABILITY_VIEW_VIABILITY_TAB_TABLE_CODE = "ViabilityViewViabilityTab";
	public static final String TARGET_VIABILITY_VIEW_VIABILITY_TAB_DETAILS_CODE = "ViabilityViewViabilityTabDetails";
	public static final String THREAT_RATING_VIEW_CODE = "ThreatRatings";
	public static final String THREAT_RATING_DETAILS_CODE = "ThreatRatingDetails";
	public static final String ACTION_PLAN_OBJECTIVE_BASED_REPORT_CODE = "PlanningViewStrategicPlan";
	public static final String ACTION_PLAN_STRATEGY_BASED_REPORT_CODE = "PlanningViewActionPlanStrategyBasedCode½";
	public static final String PLANNING_VIEW_MONITORING_PLAN_CODE = "PlanningViewMonitoringPlan";
	public static final String PLANNING_VIEW_WORK_PLAN_CODE = "PlanningViewWorkPlan";
	public static final String PLANNING_VIEW_RESOURCES_CODE = "PlanningViewResourcesTab";
	public static final String PLANNING_VIEW_ACCOUNTING_CODE_CODE = "PlanningViewAccountingCodesTab";
	public static final String PLANNING_VIEW_FUNDING_SOURCE_CODE = "PlanningViewFundingSourceTab";
	public static final String BUDGET_CATEGORY_ONE_CODE = "CategoryOneTab";
	public static final String BUDGET_CATEGORY_TWO_CODE = "CategoryTwoTab";
	public static final String ANALYSIS_CODE = "AnalysisTab";
	public static final String PROGRESS_REPORT_CODE = "ProgressReport";
	public static final String LEGEND_TABLE_REPORT_CODE = "LegendTableReport";
}
