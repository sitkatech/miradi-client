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

import java.util.HashMap;

import org.miradi.dialogs.dashboard.HtmlResourceLongDescriptionProvider;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.views.diagram.DiagramView;
import org.miradi.wizard.WizardManager;

public class OpenStandardsImplementActionsAndMonitoringQuestion extends DynamicChoiceWithRootChoiceItem
{
	public OpenStandardsImplementActionsAndMonitoringQuestion(Project projectToUse, WizardManager wizardManagerToUse)
	{
		project = projectToUse;
		wizardManager = wizardManagerToUse;
	}

	@Override
	public ChoiceItemWithChildren createHeaderChoiceItem() throws Exception
	{
		ChoiceItemWithChildren headerChoiceItem = new ChoiceItemWithChildren("3Header", getHeaderLabel(), new HtmlResourceLongDescriptionProvider(getHeaderDescriptionFileName(), getDiagramOverviewStepName()));

		headerChoiceItem.addChild(createDevelopShortTermWorkPlanChoiceItem());
		headerChoiceItem.addChild(createDevelopAndRefineProjectBudgetRow());
		headerChoiceItem.addChild(createImplementPlansRow());		

		return headerChoiceItem;
	}

	public static String getHeaderLabel()
	{
		return EAM.text("3. Implement Actions and Monitoring");
	}

	public static String getHeaderDescriptionFileName()
	{
		return "dashboard/3.html";
	}

	private ChoiceItemWithChildren createImplementPlansRow() throws Exception
	{
		HtmlResourceLongDescriptionProvider providerToUse = new HtmlResourceLongDescriptionProvider(IMPLEMENT_PLANS_RIGHT_SIDE_FILENAME, getDiagramOverviewStepName());
		ChoiceItemWithChildren subHeaderChoiceItem = new ChoiceItemWithChildren("3B", EAM.text("3C. Implement Plans"), providerToUse);

		subHeaderChoiceItem.addChild(new ChoiceItemWithChildren("ImplementStrategicAndMonitoringPlans", EAM.text("Implement Strategic and Monitoring Plans"), "", providerToUse));
		subHeaderChoiceItem.addChild(createStrategiesAndActivitiesWithProgressReportsRow(providerToUse));
		subHeaderChoiceItem.addChild(createIndicatorsAndMethodsWithProgressReportsRow(providerToUse));
		subHeaderChoiceItem.addChild(new ChoiceItemWithChildren("ImplementWorkPlans", EAM.text("Implement Work Plans"), "", providerToUse));
		
		return subHeaderChoiceItem;
	}

	private ChoiceItemWithChildren createIndicatorsAndMethodsWithProgressReportsRow(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%indicatorsAndMethodsWithProgressReportsCount", getDashboardData(Dashboard.PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_COUNT));
		tokenReplacementMap.put("%indicatorsAndMethodsWithProgressReportsPercent", getDashboardData(Dashboard.PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_PERCENT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%indicatorsAndMethodsWithProgressReportsCount Indicators/methods (% %indicatorsAndMethodsWithProgressReportsPercent) have progress reports"), tokenReplacementMap);

		return new ChoiceItemWithChildren("ObtainFinancialResources", "", rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createStrategiesAndActivitiesWithProgressReportsRow(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%strategiesAndActivitiesWithProgressReportsCount", getDashboardData(Dashboard.PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_COUNT));
		tokenReplacementMap.put("%strategiesAndActivitiesWithProgressReportsPercent", getDashboardData(Dashboard.PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_PERCENT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%strategiesAndActivitiesWithProgressReportsCount Strategies/activities (% %strategiesAndActivitiesWithProgressReportsPercent) have progress reports"), tokenReplacementMap);

		return new ChoiceItemWithChildren("ObtainFinancialResources", "", rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createDevelopAndRefineProjectBudgetRow() throws Exception
	{
		HtmlResourceLongDescriptionProvider providerToUse = new HtmlResourceLongDescriptionProvider(DEVELOP_AND_REFINE_PROJECT_BUDGET_RIGHT_SIDE_FILENAME, getDiagramOverviewStepName());
		ChoiceItemWithChildren subHeaderChoiceItem = new ChoiceItemWithChildren("3B", EAM.text("3B. Develop and Refine Project Budget"), providerToUse);

		subHeaderChoiceItem.addChild(createWorkCostsChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createExpensesChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createProjectBudgetChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(new ChoiceItemWithChildren("DevelopAndSubmitFundingProposals", EAM.text("Develop and Submit Funding Proposals"), "", providerToUse));
		subHeaderChoiceItem.addChild(createFinacialResourcesRow(providerToUse));
		
		return subHeaderChoiceItem;
	}

	private ChoiceItemWithChildren createFinacialResourcesRow(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%budgetSecuredPercent", getDashboardData(Dashboard.PSEUDO_BUDGET_SECURED_PERCENT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("Total Budget for Funding: %  %budgetSecuredPercent Budget Secured"), tokenReplacementMap);

		return new ChoiceItemWithChildren("ObtainFinancialResources", EAM.text("Obtain Finacial Resources"), rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createWorkCostsChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%workCosts", getDashboardData(Dashboard.PSEUDO_TOTAL_PROJECT_RESOURCES_COSTS));
		tokenReplacementMap.put("%currencySymbol", getEncodedCurrencySymbol());
		String rightColumnTranslatedText = EAM.substitute(EAM.text("Work Costs: %currencySymbol %workCosts"), tokenReplacementMap);

		return new ChoiceItemWithChildren("EstimateCostsForActivitiesAndMonitoring", EAM.text("Estimate Costs for Activities and Monitoring"), rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createExpensesChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%expenses", getDashboardData(Dashboard.PSEUDO_TOTAL_PROJECT_EXPENSES));
		tokenReplacementMap.put("%currencySymbol", getEncodedCurrencySymbol());
		String rightColumnTranslatedText = EAM.substitute(EAM.text("Expenses: %currencySymbol %expenses"), tokenReplacementMap);

		return new ChoiceItemWithChildren("Expenses", "", rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createProjectBudgetChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%projectBudget", getDashboardData(Dashboard.PSEUDO_PROJECT_BUDGET));
		tokenReplacementMap.put("%currencySymbol", getEncodedCurrencySymbol());
		String rightColumnTranslatedText = EAM.substitute(EAM.text("Project Budget: %currencySymbol %projectBudget"), tokenReplacementMap);

		return new ChoiceItemWithChildren("ProjectBudget", "", rightColumnTranslatedText, providerToUse);
	}

	private String getEncodedCurrencySymbol()
	{
		return "\\" + getDashboardData(Dashboard.PSEUDO_CURRENCY_SYMBOL);
	}
	
	private ChoiceItemWithChildren createDevelopShortTermWorkPlanChoiceItem() throws Exception
	{
		HtmlResourceLongDescriptionProvider providerToUse = new HtmlResourceLongDescriptionProvider(DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME, getDiagramOverviewStepName());
		ChoiceItemWithChildren subHeaderChoiceItem = new ChoiceItemWithChildren("3A", EAM.text("3A. Develop Short Term Work Plan"), providerToUse);

		subHeaderChoiceItem.addChild(createDetailActivitiesTasksAndResponsiblitiesChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createActivitiesCountChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createActivitiesAndTasksWithAssignmentsChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createDetailMethodsTasksAndResponsibilitiesChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createMethodsCountChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createMethodsAndTasksWithAssignmentsChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createProjectPlanningStartEndDateRow(providerToUse));
		
		return subHeaderChoiceItem;
	}
 
	private ChoiceItemWithChildren createProjectPlanningStartEndDateRow(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%workPlanStartDate", getDashboardData(Dashboard.PSEUDO_WORK_PLAN_START_DATE));
		tokenReplacementMap.put("%workPlanEndDate", getDashboardData(Dashboard.PSEUDO_WORK_PLAN_END_DATE));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%workPlanStartDate - %workPlanEndDate"), tokenReplacementMap);

		return new ChoiceItemWithChildren("DevelopProjectTimelineOrCalendar", EAM.text("Develop Project Timeline or Calendar"), rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createMethodsAndTasksWithAssignmentsChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%methodsAndTasksWithAssignmentsCount", getDashboardData(Dashboard.PSEUDO_METHODS_AND_TASKS_WITH_ASSIGNMENT_COUNT));
		tokenReplacementMap.put("%methodsAndTasksCount", getDashboardData(Dashboard.PSEUDO_METHODS_AND_TASKS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%methodsAndTasksWithAssignmentsCount of %methodsAndTasksCount Methods and Tasks have assignments"), tokenReplacementMap);

		return new ChoiceItemWithChildren("MethodsAndTasksWithAssignments", "", rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createMethodsCountChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%methodsCount", getDashboardData(Dashboard.PSEUDO_METHODS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%methodsCount Total methods created"), tokenReplacementMap);

		return new ChoiceItemWithChildren("TotalMethodsCreated", "", rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createDetailMethodsTasksAndResponsibilitiesChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Detail Methods, Tasks and Responsibilities");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%indicatorsWithMethodsCount", getDashboardData(Dashboard.PSEUDO_INDICATORS_WITH_METHODS_COUNT));
		tokenReplacementMap.put("%indicatorsCount", getDashboardData(Dashboard.PSEUDO_INDICATORS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%indicatorsWithMethodsCount of %indicatorsCount Indicators have Methods."), tokenReplacementMap);

		return new ChoiceItemWithChildren("DetailMethodsTasksAndResponsibilities", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createActivitiesAndTasksWithAssignmentsChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%activitiesAndTasksCount", getDashboardData(Dashboard.PSEUDO_ACTIVITIES_AND_TASKS_COUNT));
		tokenReplacementMap.put("%activitiesAndTasksWithAssignmentsCount", getDashboardData(Dashboard.PSEUDO_ACTIVITIES_AND_TASKS_WITH_ASSIGNMENTS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%activitiesAndTasksWithAssignmentsCount of %activitiesAndTasksCount Activities and tasks have assignments"), tokenReplacementMap);

		return new ChoiceItemWithChildren("TasksWithAssignmentsCount", "", rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createActivitiesCountChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%activitiesCount", getDashboardData(Dashboard.PSEUDO_ACTIVITIES_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%activitiesCount Total activities created"), tokenReplacementMap);

		return new ChoiceItemWithChildren("TotalActivitiesCreated", "", rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createDetailActivitiesTasksAndResponsiblitiesChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Detail Activities Tasks and Responsiblities");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%strategiesWithActivitiesCount", getDashboardData(Dashboard.PSEUDO_STRATEGIES_WITH_ACTIVITIES_COUNT));
		tokenReplacementMap.put("%strategiesCount", getDashboardData(Dashboard.PSEUDO_STRATEGY_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%strategiesWithActivitiesCount of %strategiesCount Strategies have at least 1 Activity. "), tokenReplacementMap);
		
		return new ChoiceItemWithChildren("DetailActivitiesTasksAndResponsiblities", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}

	protected String getDashboardData(String tag)
	{
		return getDashboard().getData(tag);
	}

	private Dashboard getDashboard()
	{
		ORef dashboardRef = getProject().getSingletonObjectRef(Dashboard.getObjectType());
		return Dashboard.find(getProject(), dashboardRef);
	}

	private String getDiagramOverviewStepName()
	{
		return wizardManager.getOverviewStepName(DiagramView.getViewName());
	}

	private Project getProject()
	{
		return project;
	}

	private Project project;
	private WizardManager wizardManager;
	private static final String DEVELOP_SHORT_TERM_WORK_PLAN_RIGHT_SIDE_FILENAME = "dashboard/3A.html";
	private static final String DEVELOP_AND_REFINE_PROJECT_BUDGET_RIGHT_SIDE_FILENAME = "dashboard/3B.html";
	private static final String IMPLEMENT_PLANS_RIGHT_SIDE_FILENAME = "dashboard/3C.html";
}
