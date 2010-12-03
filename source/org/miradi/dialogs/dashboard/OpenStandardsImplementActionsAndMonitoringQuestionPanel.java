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

import org.miradi.actions.AbstractJumpMenuAction;
import org.miradi.dialogs.base.AbstractOpenStandardsQuestionPanel;
import org.miradi.main.EAM;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.OpenStandardsImplementActionsAndMonitoringQuestion;

public class OpenStandardsImplementActionsAndMonitoringQuestionPanel extends AbstractOpenStandardsQuestionPanel
{
	public OpenStandardsImplementActionsAndMonitoringQuestionPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, new OpenStandardsImplementActionsAndMonitoringQuestion());
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
		
		if (code.equals(OpenStandardsImplementActionsAndMonitoringQuestion.DETAIL_ACTIVITIES_TASKS_AND_RESPONSIBILITIES_CODE))
			addDetailActivitiesTasksAndResponsibilitiesRow(longDescriptionProvider, indentCount);
	
		if (code.equals(OpenStandardsImplementActionsAndMonitoringQuestion.DETAIL_METHODS_TASKS_AND_RESPONSIBILITIES_CODE))
			addDetailMethodsTasksAndResponsibilitiesRow(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsImplementActionsAndMonitoringQuestion.DEVELOP_PROJECT_TIMELINE_OR_CALENDAR_CODE))
			addDevelopProjectTimelineOrCalendarRow(longDescriptionProvider, indentCount);
		
		
		if (code.equals(OpenStandardsImplementActionsAndMonitoringQuestion.ESTIMATE_COSTS_FOR_ACTIVITIES_AND_MONITORING_CODE))
			addEstimateCostsForActivitiesAndMonitoringrow(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsImplementActionsAndMonitoringQuestion.DEVELOP_AND_SUBMIT_FUNDING_PROPOSALS_CODE))
			addDevelopAndSubmitFundingProposalsrow(longDescriptionProvider, indentCount);
		
		
		if (code.equals(OpenStandardsImplementActionsAndMonitoringQuestion.IMPLEMENT_STRATEGIC_AND_MONITORING_PLANS))
			addImplementStrategicAndMonitoringPlansrow(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsImplementActionsAndMonitoringQuestion.IMPLEMENT_WORK_PLAN_CODE))
			addImplementWorkPlanrow(longDescriptionProvider, indentCount);
	}
	
	private void addDetailActivitiesTasksAndResponsibilitiesRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X of %Y Strategies have at least 1 Activity."), Dashboard.PSEUDO_STRATEGIES_WITH_ACTIVITIES_COUNT, Dashboard.PSEUDO_STRATEGY_COUNT);
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X Total activities created"), Dashboard.PSEUDO_ACTIVITIES_COUNT);
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%Y of %X Activities and tasks have assignments"), Dashboard.PSEUDO_ACTIVITIES_AND_TASKS_COUNT, Dashboard.PSEUDO_ACTIVITIES_AND_TASKS_WITH_ASSIGNMENTS_COUNT);
	}

	private void addDetailMethodsTasksAndResponsibilitiesRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X of %Y Indicators have Methods."), Dashboard.PSEUDO_INDICATORS_WITH_METHODS_COUNT, Dashboard.PSEUDO_INDICATORS_COUNT);
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X Total methods created"), Dashboard.PSEUDO_METHODS_COUNT);
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X of %Y Methods and Tasks have assignments"), Dashboard.PSEUDO_METHODS_AND_TASKS_WITH_ASSIGNMENT_COUNT, Dashboard.PSEUDO_METHODS_AND_TASKS_COUNT);
	}

	private void addDevelopProjectTimelineOrCalendarRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X - %Y"), Dashboard.PSEUDO_WORK_PLAN_START_DATE, Dashboard.PSEUDO_WORK_PLAN_END_DATE);
	}

	private void addEstimateCostsForActivitiesAndMonitoringrow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		HashMap<String, String> tokenReplacementMap1 = new HashMap<String, String>();
		tokenReplacementMap1.put("%workCosts", getDashboardData(Dashboard.PSEUDO_TOTAL_PROJECT_RESOURCES_COSTS));
		tokenReplacementMap1.put("%currencySymbol", getEncodedCurrencySymbol());
		addRow(tokenReplacementMap1, EAM.text("Work Costs: %currencySymbol %workCosts"), longDescriptionProvider, indentCount);

		HashMap<String, String> tokenReplacementMap2 = new HashMap<String, String>();
		tokenReplacementMap2.put("%expenses", getDashboardData(Dashboard.PSEUDO_TOTAL_PROJECT_EXPENSES));
		tokenReplacementMap2.put("%currencySymbol", getEncodedCurrencySymbol());
		addRow(tokenReplacementMap2, EAM.text("Expenses: %currencySymbol %expenses"), longDescriptionProvider, indentCount);
		
		HashMap<String, String> tokenReplacementMap3 = new HashMap<String, String>();
		tokenReplacementMap3.put("%projectBudget", getDashboardData(Dashboard.PSEUDO_PROJECT_BUDGET));
		tokenReplacementMap3.put("%currencySymbol", getEncodedCurrencySymbol());
		addRow(tokenReplacementMap3, EAM.text("Project Budget: %currencySymbol %projectBudget"), longDescriptionProvider, indentCount);
	}

	private void addDevelopAndSubmitFundingProposalsrow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("Total Budget for Funding: %  %X Budget Secured"), Dashboard.PSEUDO_BUDGET_SECURED_PERCENT);
	}

	private void addImplementStrategicAndMonitoringPlansrow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X Strategies/activities (% %Y) have progress reports"), Dashboard.PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_COUNT, Dashboard.PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_PERCENT);
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X Indicators/methods (% %Y) have progress reports"), Dashboard.PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_COUNT, Dashboard.PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_PERCENT);
	}

	private void addImplementWorkPlanrow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount)
	{
	}
	
	private String getEncodedCurrencySymbol()
	{
		return "\\" + getDashboardData(Dashboard.PSEUDO_CURRENCY_SYMBOL);
	}

	@Override
	public String getPanelDescription()
	{
		return "OpenStandardsImplementActionsAndMonitoringQuestionPanel";
	}
}
