/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

public class ReportTemplateContentQuestion extends StaticChoiceQuestion
{
	public ReportTemplateContentQuestion()
	{
		super(getContentChoices());
	}

	static ChoiceItem[] getContentChoices()
	{		
		return new ChoiceItem[] {
				new ChoiceItem(SUMMARY_VIEW_PROJECT_TAB_CODE, "Project Summary"),
				new ChoiceItem(SUMMARY_VIEW_TEAM_TAB_CODE, "Team"),
				new ChoiceItem(SUMMARY_VIEW_ORGANIZATION_TAB_CODE, "Organization"),
				new ChoiceItem(SUMMARY_VIEW_SCOPE_TAB_CODE, "Scope"),
				new ChoiceItem(SUMMARY_VIEW_LOCATION_TAB_CODE, "Location"),
				new ChoiceItem(SUMMARY_VIEW_PLANNING_TAB_CODE, "Planning Parameters"),
				new ChoiceItem(SUMMARY_VIEW_TNC_TAB_CODE, "TNC"),
				new ChoiceItem(SUMMARY_VIEW_WWF_TAB_CODE, "WWF"),
				new ChoiceItem(SUMMARY_VIEW_WCS_TAB_CODE, "WCS"),
				new ChoiceItem(SUMMARY_VIEW_RARE_TAB_CODE, "RARE"),
				new ChoiceItem(SUMMARY_VIEW_FOS_TAB_CODE, "FOS"),
				
				new ChoiceItem(DIAGRAM_VIEW_CONCEPTUAL_MODEL_TAB_CODE, "Conceptual Model"),
				new ChoiceItem(DIAGRAM_VIEW_RESULTS_CHAINS_TAB_CODE, "Results Chains"),
				
				new ChoiceItem(VIABILITY_VIEW_VIABILITY_TAB_CODE, "Target Viability"),
				
				new ChoiceItem(THREAT_RATING_VIEW_CODE, "Threat Ratings"),
				
				new ChoiceItem(PLANNING_VIEW_PLANNING_TAB_CODE, "Planning"),
				new ChoiceItem(PLANNING_VIEW_RESOURCES_TAB_CODE, "Resources"),
				new ChoiceItem(PLANNING_VIEW_ACCOUNTING_CODE_TAB_CODE, "Accounting Codes"),
				new ChoiceItem(PLANNING_VIEW_FUNDING_SOURCE_TAB_CODE, "Funding Sources"),
		};
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
	public static final String VIABILITY_VIEW_VIABILITY_TAB_CODE = "ViabilityViewViabilityTab";
	public static final String THREAT_RATING_VIEW_CODE = "ThreatRatings";
	public static final String PLANNING_VIEW_PLANNING_TAB_CODE = "PlanningViewPlanningTab";
	public static final String PLANNING_VIEW_RESOURCES_TAB_CODE = "PlanningViewResourcesTab";
	public static final String PLANNING_VIEW_ACCOUNTING_CODE_TAB_CODE = "PlanningViewAccountingCodesTab";
	public static final String PLANNING_VIEW_FUNDING_SOURCE_TAB_CODE = "PlanningViewFundingSourceTab";
}
