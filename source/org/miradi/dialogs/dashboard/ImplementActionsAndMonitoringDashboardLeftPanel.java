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

public class ImplementActionsAndMonitoringDashboardLeftPanel extends LeftSidePanelWithSelectableRows
{
	public ImplementActionsAndMonitoringDashboardLeftPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse);
		
		createHeaderRow(EAM.text("3. Implement Actions and Monitoring"), "", getMainDescriptionFileName(), getDiagramOverviewStepName());
		createSubHeaderRow(EAM.text("3A. Develop Short Term Work Plan"), DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
		
		createDetailActivitiesTasksAndResponsiblitiesRow();
		createActivitiesCountRow();
		createActivitiesAndTasksWithAssignmentsRow();
		createDetailMethodsTasksAndResponsibilitiesRow();
		createMethodsCountRow();
		createMethodsAndTasksWithAssignmentsRow();
		createProjectPlanningStartEndDateRow();
		createDevelopAndRefineProjectBudgetRow();
		createImplementPlansRow();		
	}	

	protected String getMainDescriptionFileName()
	{
		return "dashboard/3.html";
	}
	
	private void createImplementPlansRow() throws Exception
	{
		createSubHeaderRow(EAM.text("3C. Implement Plans"), IMPLEMENT_PLANS_RIGHT_SIDE_FILENAME);
		createImplementStrategicAndMonitoringPlansRow();
	}

	private void createImplementStrategicAndMonitoringPlansRow() throws Exception
	{
		createDataRow(EAM.text("Implement Strategic and Monitoring Plans"), "", IMPLEMENT_PLANS_RIGHT_SIDE_FILENAME);
		createStrategiesAndActivitiesWithProgressReportsRow();
		createIndicatorsAndMethodsWithProgressReportsRow();
		createDataRow(EAM.text("Implement Work Plans"), "", IMPLEMENT_PLANS_RIGHT_SIDE_FILENAME);
	}

	private void createIndicatorsAndMethodsWithProgressReportsRow() throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%indicatorsAndMethodsWithProgressReportsCount", getDashboardData(Dashboard.PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_COUNT));
		tokenReplacementMap.put("%indicatorsAndMethodsWithProgressReportsPercent", getDashboardData(Dashboard.PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_PERCENT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%indicatorsAndMethodsWithProgressReportsCount Indicators/methods (% %indicatorsAndMethodsWithProgressReportsPercent) have progress reports"), tokenReplacementMap);

		createDataRow("", rightColumnTranslatedText, IMPLEMENT_PLANS_RIGHT_SIDE_FILENAME);
	}

	private void createStrategiesAndActivitiesWithProgressReportsRow() throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%strategiesAndActivitiesWithProgressReportsCount", getDashboardData(Dashboard.PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_COUNT));
		tokenReplacementMap.put("%strategiesAndActivitiesWithProgressReportsPercent", getDashboardData(Dashboard.PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_PERCENT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%strategiesAndActivitiesWithProgressReportsCount Strategies/activities (% %strategiesAndActivitiesWithProgressReportsPercent) have progress reports"), tokenReplacementMap);

		createDataRow("", rightColumnTranslatedText, IMPLEMENT_PLANS_RIGHT_SIDE_FILENAME);
	}

	private void createDevelopAndRefineProjectBudgetRow() throws Exception
	{
		createSubHeaderRow(EAM.text("3B. Develop and Refine Project Budget"), DEVELOP_AND_REFINE_PROJECT_BUDGET_RIGHT_SIDE_FILENAME);
		createWorkCostsRow();
		createExpensesRow();
		createProjectBudgetRow();
		createDataRow(EAM.text("Develop and Submit Funding Proposals"), "", DEVELOP_AND_REFINE_PROJECT_BUDGET_RIGHT_SIDE_FILENAME);
		createFinacialResourcesRow();
	}

	private void createFinacialResourcesRow() throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%budgetSecuredPercent", getDashboardData(Dashboard.PSEUDO_BUDGET_SECURED_PERCENT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("Total Budget for Funding: %  %budgetSecuredPercent Budget Secured"), tokenReplacementMap);

		createDataRow(EAM.text("Obtain Finacial Resources"), rightColumnTranslatedText, DEVELOP_AND_REFINE_PROJECT_BUDGET_RIGHT_SIDE_FILENAME);		
	}

	private void createWorkCostsRow() throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%workCosts", getDashboardData(Dashboard.PSEUDO_TOTAL_PROJECT_RESOURCES_COSTS));
		tokenReplacementMap.put("%currencySymbol", getEncodedCurrencySymbol());
		String rightColumnTranslatedText = EAM.substitute(EAM.text("Work Costs: %currencySymbol %workCosts"), tokenReplacementMap);

		createDataRow(EAM.text("Estimate Costs for Activities and Monitoring"), rightColumnTranslatedText, DEVELOP_AND_REFINE_PROJECT_BUDGET_RIGHT_SIDE_FILENAME);
	}
	
	private void createExpensesRow() throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%expenses", getDashboardData(Dashboard.PSEUDO_TOTAL_PROJECT_EXPENSES));
		tokenReplacementMap.put("%currencySymbol", getEncodedCurrencySymbol());
		String rightColumnTranslatedText = EAM.substitute(EAM.text("Expenses: %currencySymbol %expenses"), tokenReplacementMap);

		createDataRow("", rightColumnTranslatedText, DEVELOP_AND_REFINE_PROJECT_BUDGET_RIGHT_SIDE_FILENAME);
	}
	
	private void createProjectBudgetRow() throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%projectBudget", getDashboardData(Dashboard.PSEUDO_PROJECT_BUDGET));
		tokenReplacementMap.put("%currencySymbol", getEncodedCurrencySymbol());
		String rightColumnTranslatedText = EAM.substitute(EAM.text("Project Budget: %currencySymbol %projectBudget"), tokenReplacementMap);

		createDataRow("", rightColumnTranslatedText, DEVELOP_AND_REFINE_PROJECT_BUDGET_RIGHT_SIDE_FILENAME);
	}

	private String getEncodedCurrencySymbol()
	{
		return "\\" + getDashboardData(Dashboard.PSEUDO_CURRENCY_SYMBOL);
	}

	private void createProjectPlanningStartEndDateRow() throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%workPlanStartDate", getDashboardData(Dashboard.PSEUDO_WORK_PLAN_START_DATE));
		tokenReplacementMap.put("%workPlanEndDate", getDashboardData(Dashboard.PSEUDO_WORK_PLAN_END_DATE));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%workPlanStartDate - %workPlanEndDate"), tokenReplacementMap);

		createDataRow(EAM.text("Develop Project Timeline or Calendar"), rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void createMethodsAndTasksWithAssignmentsRow() throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%methodsAndTasksWithAssignmentsCount", getDashboardData(Dashboard.PSEUDO_METHODS_AND_TASKS_WITH_ASSIGNMENT_COUNT));
		tokenReplacementMap.put("%methodsAndTasksCount", getDashboardData(Dashboard.PSEUDO_METHODS_AND_TASKS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%methodsAndTasksWithAssignmentsCount of %methodsAndTasksCount Methods and Tasks have assignments"), tokenReplacementMap);

		createDataRow("", rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void createMethodsCountRow() throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%methodsCount", getDashboardData(Dashboard.PSEUDO_METHODS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%methodsCount Total methods created"), tokenReplacementMap);

		createDataRow("", rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);		
	}

	private void createDetailMethodsTasksAndResponsibilitiesRow() throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Detail Methods, Tasks and Responsibilities");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%indicatorsWithMethodsCount", getDashboardData(Dashboard.PSEUDO_INDICATORS_WITH_METHODS_COUNT));
		tokenReplacementMap.put("%indicatorsCount", getDashboardData(Dashboard.PSEUDO_INDICATORS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%indicatorsWithMethodsCount of %indicatorsCount Indicators have Methods."), tokenReplacementMap);

		createDataRow(leftColumnTranslatedText, rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void createActivitiesAndTasksWithAssignmentsRow() throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%activitiesAndTasksCount", getDashboardData(Dashboard.PSEUDO_ACTIVITIES_AND_TASKS_COUNT));
		tokenReplacementMap.put("%activitiesAndTasksWithAssignmentsCount", getDashboardData(Dashboard.PSEUDO_ACTIVITIES_AND_TASKS_WITH_ASSIGNMENTS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%activitiesAndTasksWithAssignmentsCount of %activitiesAndTasksCount Activities and tasks have assignments"), tokenReplacementMap);

		createDataRow("", rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void createActivitiesCountRow() throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%activitiesCount", getDashboardData(Dashboard.PSEUDO_ACTIVITIES_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%activitiesCount Total activities created"), tokenReplacementMap);

		createDataRow("", rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
	}

	private void createDetailActivitiesTasksAndResponsiblitiesRow() throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Detail Activities Tasks and Responsiblities");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%strategiesWithActivitiesCount", getDashboardData(Dashboard.PSEUDO_STRATEGIES_WITH_ACTIVITIES_COUNT));
		tokenReplacementMap.put("%strategiesCount", getDashboardData(Dashboard.PSEUDO_STRATEGY_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%strategiesWithActivitiesCount of %strategiesCount Strategies have at least 1 Activity. "), tokenReplacementMap);

		createDataRow(leftColumnTranslatedText, rightColumnTranslatedText, DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME);
	}
	
	private SelectableRow createDataRow(String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName) throws Exception
	{
		return createDataRow(leftColumnTranslatedText, rightColumnTranslatedText, descriptionFileName, getDiagramOverviewStepName());
	}
	
	private void createSubHeaderRow(String leftColumnTranslatedText, String rightPanelHtmlFileName) throws Exception
	{
		createSubHeaderRow(leftColumnTranslatedText, rightPanelHtmlFileName, getDiagramOverviewStepName());
	}
	
	private static final String DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME = "dashboard/3A.html";
	private static final String DEVELOP_AND_REFINE_PROJECT_BUDGET_RIGHT_SIDE_FILENAME = "dashboard/3B.html";
	private static final String IMPLEMENT_PLANS_RIGHT_SIDE_FILENAME = "dashboard/3c.html";
}
