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

import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.project.Project;

public class ReportTemplateContentQuestion extends DynamicChoiceQuestion
{
	public ReportTemplateContentQuestion(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public Project getProject()
	{
		return project;
	}
	
	@Override
	public ChoiceItem[] getChoices()
	{
		Vector<ChoiceItem> choices = new Vector<ChoiceItem>();
		choices.addAll(createSummaryViewSectionChoices());
		choices.addAll(createDiagramViewSectionChoices());
		choices.addAll(createViabilityViewSectionChoices());
		choices.addAll(createThreatRatingViewSectionChoices());
		choices.addAll(createStrategicPlanViewSectionChoices());
		choices.addAll(createWorkPlanViewSectionChoices());
		
		choices.add(new ChoiceItem(PROGRESS_REPORT_CODE, getProgressReportLabel()));

		choices.add(new ChoiceItem(LEGEND_TABLE_REPORT_CODE, getLegendTableLabel()));
		
		return choices.toArray(new ChoiceItem[0]);
	}

	private Vector<ChoiceItem> createSummaryViewSectionChoices()
	{
		Vector<ChoiceItem> choices = new Vector<ChoiceItem>();
		choices.add(new ChoiceItem(SUMMARY_VIEW_PROJECT_TAB_CODE, EAM.text("Project Summary")));
		choices.add(new ChoiceItem(SUMMARY_VIEW_TEAM_TAB_CODE, getTeamLabel()));
		choices.add(new ChoiceItem(SUMMARY_VIEW_ORGANIZATION_TAB_CODE, getOraganizationLabel()));
		choices.add(new ChoiceItem(SUMMARY_VIEW_SCOPE_TAB_CODE, EAM.text("Scope")));
		choices.add(new ChoiceItem(SUMMARY_VIEW_LOCATION_TAB_CODE, EAM.text("Locaton")));
		choices.add(new ChoiceItem(SUMMARY_VIEW_PLANNING_TAB_CODE, EAM.text("Planning Parameters")));
		choices.add(new ChoiceItem(SUMMARY_VIEW_TNC_TAB_CODE, EAM.text("TNC")));
		choices.add(new ChoiceItem(SUMMARY_VIEW_WWF_TAB_CODE, EAM.text("WWF")));
		choices.add(new ChoiceItem(SUMMARY_VIEW_WCS_TAB_CODE, EAM.text("WCS")));
		choices.add(new ChoiceItem(SUMMARY_VIEW_RARE_TAB_CODE, EAM.text("RARE")));
		choices.add(new ChoiceItem(SUMMARY_VIEW_FOS_TAB_CODE, EAM.text("FOS")));
		return choices;
	}

	private Vector<ChoiceItem> createDiagramViewSectionChoices()
	{
		Vector<ChoiceItem> choices = new Vector<ChoiceItem>();
		choices.add(new ChoiceItem(DIAGRAM_VIEW_CONCEPTUAL_MODEL_TAB_CODE, EAM.text("Conceptual Model")));
		choices.add(new ChoiceItem(DIAGRAM_VIEW_RESULTS_CHAINS_TAB_CODE, EAM.text("Results Chains")));
		return choices;
	}
	
	private Vector<ChoiceItem> createViabilityViewSectionChoices()
	{
		Vector<ChoiceItem> choices = new Vector<ChoiceItem>();
		choices.add(new ChoiceItem(TARGET_VIABILITY_VIEW_VIABILITY_TAB_TABLE_CODE, getTargetViabilityTableLabel()));
		choices.add(new ChoiceItem(TARGET_VIABILITY_VIEW_VIABILITY_TAB_DETAILS_CODE, getTargetViabilityDetailsLabel()));
		return choices;
	}
	
	private Vector<ChoiceItem> createThreatRatingViewSectionChoices()
	{
		Vector<ChoiceItem> choices = new Vector<ChoiceItem>();
		choices.add(new ChoiceItem(THREAT_RATING_VIEW_CODE, getThreatRatingsLabel()));
		choices.add(new ChoiceItem(THREAT_RATING_DETAILS_CODE, getThreatRatingDetailsLabel()));
		return choices;
	}
	
	private Vector<ChoiceItem> createStrategicPlanViewSectionChoices()
	{
		Vector<ChoiceItem> choices = new Vector<ChoiceItem>();
		choices.add(new ChoiceItem(ACTION_PLAN_OBJECTIVE_BASED_REPORT_CODE, getActionPlanObjectiveBasedActionPlanLabel()));
		choices.add(new ChoiceItem(ACTION_PLAN_STRATEGY_BASED_REPORT_CODE, getActionPlanStrategyBasedActionPlanLabel()));
		choices.add(new ChoiceItem(PLANNING_VIEW_MONITORING_PLAN_CODE, getMonitoringPlanLabel()));
		return choices;
	}
	
	private Vector<ChoiceItem> createWorkPlanViewSectionChoices()
	{
		Vector<ChoiceItem> choices = new Vector<ChoiceItem>();
		choices.add(new ChoiceItem(PLANNING_VIEW_WORK_PLAN_CODE, getWorkPlanLabel()));
		choices.add(new ChoiceItem(PLANNING_VIEW_RESOURCES_CODE, getResourcesLabel()));
		choices.add(new ChoiceItem(PLANNING_VIEW_ACCOUNTING_CODE_CODE, getAccountingCodesLabel()));
		choices.add(new ChoiceItem(PLANNING_VIEW_FUNDING_SOURCE_CODE, getFundingSourcesLabel()));
		choices.add(new ChoiceItem(BUDGET_CATEGORY_ONE_CODE, getCategoryOneLabel()));
		choices.add(new ChoiceItem(BUDGET_CATEGORY_TWO_CODE, getCategoryTwoLabel()));
		choices.add(new ChoiceItem(ANALYSIS_CODE, getAnalysisLabel()));
		return choices;
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
		return EAM.text("Action Plan: Objective-based");
	}
	
	public static String getActionPlanStrategyBasedActionPlanLabel()
	{
		return EAM.text("Action Plan: Strategy-based");
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
	public static final String ACTION_PLAN_STRATEGY_BASED_REPORT_CODE = "PlanningViewActionPlanStrategyBasedCode";
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
	
	public static final String CUSTOM_TABLE_PREFIX = "CUSTOM:";
	
	private Project project;
}
