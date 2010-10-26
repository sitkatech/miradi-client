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

import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.EAM;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.views.diagram.DiagramView;

public class ImplementActionsAndMonitoringDashboardTab extends AbstractDashboardTab
{
	public ImplementActionsAndMonitoringDashboardTab(Project projectToUse) throws Exception
	{
		super(projectToUse);
	}

	@Override
	protected String getMainDescriptionFileName()
	{
		return "dashboard/3.html";
	}

	@Override
	protected TwoColumnPanel createLeftPanel()
	{
		TwoColumnPanel leftMainPanel = new TwoColumnPanel();
		createHeaderRow(leftMainPanel, EAM.text("3. Implement Actions and Monitoring"), "", getMainDescriptionFileName(), DiagramView.getViewName());
		createSubHeaderRow(leftMainPanel, EAM.text("3A. Develop Short Term Work Plan"), DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
		
		createDetailActivitiesTasksAndResponsiblitiesRow(leftMainPanel);
		createActivitiesCountRow(leftMainPanel);
		createActivitiesAndTasksWithAssignmentsRow(leftMainPanel);
		createDetailMethodsTasksAndResponsibilitiesRow(leftMainPanel);
		createMethodsCountRow(leftMainPanel);
		createMethodsAndTasksWithAssignmentsRow(leftMainPanel);
		createProjectPlanningStartEndDateRow(leftMainPanel);
		createDevelopAndRefineProjectBudgetRow(leftMainPanel);
		createImplementPlansRow(leftMainPanel);
		
		return leftMainPanel;
	}
	
	private void createImplementPlansRow(TwoColumnPanel leftMainPanel)
	{
		createSubHeaderRow(leftMainPanel, EAM.text("3C. Implement Plans"), IMPLEMENT_PLANS_RIGHT_SIDE_FILENAME);
		createImplementStrategicAndMonitoringPlansRow(leftMainPanel);
	}

	private void createImplementStrategicAndMonitoringPlansRow(TwoColumnPanel leftMainPanel)
	{
		createDataRow(leftMainPanel, EAM.text("Implement Strategic and Monitoring Plans"), "", IMPLEMENT_PLANS_RIGHT_SIDE_FILENAME);
		createStrategiesAndActivitiesWithProgressReportsRow(leftMainPanel);
		createIndicatorsAndMethodsWithProgressReportsRow(leftMainPanel);
		createDataRow(leftMainPanel, EAM.text("Implement Work Plans"), "", IMPLEMENT_PLANS_RIGHT_SIDE_FILENAME);
	}

	private void createIndicatorsAndMethodsWithProgressReportsRow(TwoColumnPanel leftMainPanel)
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%indicatorsAndMethodsWithProgressReportsCount", getDashboardData(Dashboard.PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_COUNT));
		tokenReplacementMap.put("%indicatorsAndMethodsWithProgressReportsPercent", getDashboardData(Dashboard.PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_PERCENT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%indicatorsAndMethodsWithProgressReportsCount Indicators/methods (% %indicatorsAndMethodsWithProgressReportsPercent) have progress reports"), tokenReplacementMap);

		createDataRow(leftMainPanel, "", rightColumnTranslatedText, IMPLEMENT_PLANS_RIGHT_SIDE_FILENAME);
	}

	private void createStrategiesAndActivitiesWithProgressReportsRow(TwoColumnPanel leftMainPanel)
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%strategiesAndActivitiesWithProgressReportsCount", getDashboardData(Dashboard.PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_COUNT));
		tokenReplacementMap.put("%strategiesAndActivitiesWithProgressReportsPercent", getDashboardData(Dashboard.PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_PERCENT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%strategiesAndActivitiesWithProgressReportsCount Strategies/activities (% %strategiesAndActivitiesWithProgressReportsPercent) have progress reports"), tokenReplacementMap);

		createDataRow(leftMainPanel, "", rightColumnTranslatedText, IMPLEMENT_PLANS_RIGHT_SIDE_FILENAME);
	}

	private void createDevelopAndRefineProjectBudgetRow(TwoColumnPanel leftMainPanel)
	{
		createSubHeaderRow(leftMainPanel, EAM.text("3B. Develop and Refine Project Budget"), DEVELOP_AND_REFINE_PROJECT_BUDGET_RIGHT_SIDE_FILENAME);
		createEstimatedCostsForActivitiesAndMonitoring(leftMainPanel);
		createDataRow(leftMainPanel, EAM.text("Develop and Submit Funding Proposals"), "", DEVELOP_AND_REFINE_PROJECT_BUDGET_RIGHT_SIDE_FILENAME);
		createFinacialResourcesRow(leftMainPanel);
	}

	private void createFinacialResourcesRow(TwoColumnPanel leftMainPanel)
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%budgetSecuredPercent", getDashboardData(Dashboard.PSEUDO_BUDGET_SECURED_PERCENT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("Total Budget for Funding: %  %budgetSecuredPercent Budget Secured"), tokenReplacementMap);

		createDataRow(leftMainPanel, EAM.text("Obtain Finacial Resources"), rightColumnTranslatedText, DEVELOP_AND_REFINE_PROJECT_BUDGET_RIGHT_SIDE_FILENAME);		
	}

	private void createEstimatedCostsForActivitiesAndMonitoring(TwoColumnPanel leftMainPanel)
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%workCosts", getDashboardData(Dashboard.PSEUDO_TOTAL_PROJECT_RESOURCES_COSTS));
		tokenReplacementMap.put("%expenses", getDashboardData(Dashboard.PSEUDO_TOTAL_PROJECT_EXPENSES));
		tokenReplacementMap.put("%projectBudget", getDashboardData(Dashboard.PSEUDO_PROJECT_BUDGET));
		tokenReplacementMap.put("%currencySymbol", getEncodedCurrencySymbol());
		String rightColumnTranslatedText = EAM.substitute(EAM.text("Work Costs: %currencySymbol %workCosts, Expenses: %currencySymbol %expenses, Project Budget: %currencySymbol %projectBudget"), tokenReplacementMap);

		createDataRow(leftMainPanel, EAM.text("Estimate Costs for Activities and Monitoring"), rightColumnTranslatedText, DEVELOP_AND_REFINE_PROJECT_BUDGET_RIGHT_SIDE_FILENAME);
	}

	private String getEncodedCurrencySymbol()
	{
		return "\\" + getDashboardData(Dashboard.PSEUDO_CURRENCY_SYMBOL);
	}

	private void createProjectPlanningStartEndDateRow(TwoColumnPanel leftMainPanel)
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%workPlanStartDate", getDashboardData(Dashboard.PSEUDO_WORK_PLAN_START_DATE));
		tokenReplacementMap.put("%workPlanEndDate", getDashboardData(Dashboard.PSEUDO_WORK_PLAN_END_DATE));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%workPlanStartDate - %workPlanEndDate"), tokenReplacementMap);

		createDataRow(leftMainPanel, EAM.text("Develop Project Timeline or Calendar"), rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void createMethodsAndTasksWithAssignmentsRow(TwoColumnPanel leftMainPanel)
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%methodsAndTasksWithAssignmentsCount", getDashboardData(Dashboard.PSEUDO_METHODS_AND_TASKS_WITH_ASSIGNMENT_COUNT));
		tokenReplacementMap.put("%methodsAndTasksCount", getDashboardData(Dashboard.PSEUDO_METHODS_AND_TASKS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%methodsAndTasksWithAssignmentsCount of %methodsAndTasksCount Methods and Tasks have assignments"), tokenReplacementMap);

		createDataRow(leftMainPanel, "", rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void createMethodsCountRow(TwoColumnPanel leftMainPanel)
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%methodsCount", getDashboardData(Dashboard.PSEUDO_METHODS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%methodsCount Total methods created"), tokenReplacementMap);

		createDataRow(leftMainPanel, "", rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);		
	}

	private void createDetailMethodsTasksAndResponsibilitiesRow(TwoColumnPanel leftMainPanel)
	{
		String leftColumnTranslatedText = EAM.text("Detail Methods, Tasks and Responsibilities");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%indicatorsWithMethodsCount", getDashboardData(Dashboard.PSEUDO_INDICATORS_WITH_METHODS_COUNT));
		tokenReplacementMap.put("%indicatorsCount", getDashboardData(Dashboard.PSEUDO_INDICATORS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%indicatorsWithMethodsCount of %indicatorsCount Indicators have Methods."), tokenReplacementMap);

		createDataRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void createActivitiesAndTasksWithAssignmentsRow(TwoColumnPanel leftMainPanel)
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%activitiesAndTasksCount", getDashboardData(Dashboard.PSEUDO_ACTIVITIES_AND_TASKS_COUNT));
		tokenReplacementMap.put("%activitiesAndTasksWithAssignmentsCount", getDashboardData(Dashboard.PSEUDO_ACTIVITIES_AND_TASKS_WITH_ASSIGNMENTS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%activitiesAndTasksWithAssignmentsCount of %activitiesAndTasksCount Activities and tasks have assignments"), tokenReplacementMap);

		createDataRow(leftMainPanel, "", rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void createActivitiesCountRow(TwoColumnPanel leftMainPanel)
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%activitiesCount", getDashboardData(Dashboard.PSEUDO_ACTIVITIES_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%activitiesCount Total activities created"), tokenReplacementMap);

		createDataRow(leftMainPanel, "", rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void createDetailActivitiesTasksAndResponsiblitiesRow(TwoColumnPanel leftMainPanel)
	{
		String leftColumnTranslatedText = EAM.text("Detail Activities Tasks and Responsiblities");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%strategiesWithActivitiesCount", getDashboardData(Dashboard.PSEUDO_STRATEGIES_WITH_ACTIVITIES_COUNT));
		tokenReplacementMap.put("%strategiesCount", getDashboardData(Dashboard.PSEUDO_STRATEGY_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%strategiesWithActivitiesCount of %strategiesCount Strategies have at least 1 Activity. "), tokenReplacementMap);

		createDataRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
	}
	
	private SelectableRow createDataRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName)
	{
		return createDataRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, descriptionFileName, DiagramView.getViewName());
	}
	
	private void createSubHeaderRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightPanelHtmlFileName)
	{
		createSubHeaderRow(leftMainPanel, leftColumnTranslatedText, rightPanelHtmlFileName, DiagramView.getViewName());
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Implement Actions and Monitoring");
	}
	
	private static final String DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME = "dashboard/3A.html";
	private static final String DEVELOP_AND_REFINE_PROJECT_BUDGET_RIGHT_SIDE_FILENAME = "dashboard/3B.html";
	private static final String IMPLEMENT_PLANS_RIGHT_SIDE_FILENAME = "dashboard/3c.html";
}
