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

public class OpenStandardsImplementActionsAndMonitoringQuestion extends DynamicChoiceWithRootChoiceItem
{
	@Override
	protected ChoiceItemWithChildren createHeaderChoiceItem() throws Exception
	{
		ChoiceItemWithChildren headerChoiceItem = new ChoiceItemWithChildren(IMPLEMENT_ACTIONS_AND_MONITORING_HEADER_CODE, getHeaderLabel(), new HtmlResourceLongDescriptionProvider(MAIN_DESCRIPTION_RIGHT_PANEL_FILE_NAME));
		ChoiceItemWithChildren processStep3a = new ChoiceItemWithChildren(PROCESS_STEP_3A_CODE, getProcessStep3a(), EAM.text(""), new HtmlResourceLongDescriptionProvider(DESCRIPTION_3A_FILE_NAME));
		headerChoiceItem.addChild(processStep3a);
		processStep3a.addChild(new ChoiceItem(DETAIL_ACTIVITIES_TASKS_AND_RESPONSIBILITIES_CODE, getDetailActivitiesTasksAndResponsibilitiesLabel(), new HtmlResourceLongDescriptionProvider(DESCRIPTION_3A_FILE_NAME)));
		processStep3a.addChild(new ChoiceItem(DETAIL_METHODS_TASKS_AND_RESPONSIBILITIES_CODE, getDetailMethodsTasksAndResponsibilitiesLabel(), new HtmlResourceLongDescriptionProvider(DESCRIPTION_3A_FILE_NAME)));
		processStep3a.addChild(new ChoiceItem(DEVELOP_PROJECT_TIMELINE_OR_CALENDAR_CODE, getDevelopProjectTimelineOrCalendarLabel(), new HtmlResourceLongDescriptionProvider(DESCRIPTION_3A_FILE_NAME)));
		
		ChoiceItemWithChildren processStep3bChoiceItem = new ChoiceItemWithChildren(PROCESS_STEP_3B_CODE, getProcessStep3b(), EAM.text(""), new HtmlResourceLongDescriptionProvider(DESCRIPTION_3B_FILE_NAME));
		headerChoiceItem.addChild(processStep3bChoiceItem);
		processStep3bChoiceItem.addChild(new ChoiceItem(ESTIMATE_COSTS_FOR_ACTIVITIES_AND_MONITORING_CODE, getEstimateCostsForActivitiesAndMonitoringLabel(), new HtmlResourceLongDescriptionProvider(DESCRIPTION_3B_FILE_NAME)));
		processStep3bChoiceItem.addChild(new ChoiceItem(DEVELOP_AND_SUBMIT_FUNDING_PROPOSALS_CODE, getDevelopAndSubmitFundingProposalsLabel(), new HtmlResourceLongDescriptionProvider(DESCRIPTION_3B_FILE_NAME)));
		
		ChoiceItemWithChildren processStep3cChoiceItem = new ChoiceItemWithChildren(PROCESS_STEP_3C_CODE, getProcessStep3c(), EAM.text(""), new HtmlResourceLongDescriptionProvider(DESCRIPTION_3C_FILE_NAME));
		headerChoiceItem.addChild(processStep3cChoiceItem);
		processStep3cChoiceItem.addChild(new ChoiceItem(IMPLEMENT_STRATEGIC_AND_MONITORING_PLANS, getImplementStrategicAndMonitoringPlansLabel(), new HtmlResourceLongDescriptionProvider(DESCRIPTION_3C_FILE_NAME)));
		processStep3cChoiceItem.addChild(new ChoiceItem(IMPLEMENT_WORK_PLAN_CODE, getImplementWorkPlanLabel(), new HtmlResourceLongDescriptionProvider(DESCRIPTION_3C_FILE_NAME)));
		
		return headerChoiceItem;
	}
	
	public static String getHeaderLabel()
	{
		return EAM.text("Menu|3. Implement Actions &  Monitoring");
	}
	
	public static String getProcessStep3a()
	{
		return EAM.text("ProcessStep|3A. Develop Short-Term Work Plan");
	}
	
	public static String getProcessStep3b()
	{
		return EAM.text("ProcessStep|3B. Develop and Refine Project Budget");
	}

	public static String getProcessStep3c()
	{
		return EAM.text("ProcessStep|3C. Implement Plans");
	}

	public static String getDetailActivitiesTasksAndResponsibilitiesLabel()
	{
		return EAM.text("Detail activities, tasks, and responsibilities");
	}
	
	public static String getDetailMethodsTasksAndResponsibilitiesLabel()
	{
		return EAM.text("Detail methods, tasks, and responsibilities");
	}
	
	public static String getDevelopProjectTimelineOrCalendarLabel()
	{
		return EAM.text("Develop project timeline or calendar");
	}
	
	public static String getEstimateCostsForActivitiesAndMonitoringLabel()
	{
		return EAM.text("Estimate costs for activities and monitoring");
	}
	
	public static String getDevelopAndSubmitFundingProposalsLabel()
	{
		return EAM.text("Develop and submit funding proposals");
	}

	public static String getObtainFinancialResourcesLabel()
	{
		return EAM.text("Obtain financial resources");
	}
	
	public static String getImplementStrategicAndMonitoringPlansLabel()
	{
		return EAM.text("Implement strategic and monitoring plans");
	}
	
	public static String getImplementWorkPlanLabel()
	{
		return EAM.text("Implement work plan");
	}

	public static final String IMPLEMENT_ACTIONS_AND_MONITORING_HEADER_CODE = "ImplementActionsAndMonitoring";
	public static final String PROCESS_STEP_3A_CODE = "ProcessStep3A";
	public static final String PROCESS_STEP_3B_CODE = "ProcessStep3B";
	public static final String PROCESS_STEP_3C_CODE = "ProcessStep3C";
	public static final String DETAIL_ACTIVITIES_TASKS_AND_RESPONSIBILITIES_CODE = "DetailActivitiesTasksAndResponsibilities";
	public static final String DETAIL_METHODS_TASKS_AND_RESPONSIBILITIES_CODE = "DetailMethodsTasksAndResponsibilities";
	public static final String DEVELOP_PROJECT_TIMELINE_OR_CALENDAR_CODE = "DevelopProjectTimelineOrCalendar";
	public static final String ESTIMATE_COSTS_FOR_ACTIVITIES_AND_MONITORING_CODE = "EstimateCostsForActivitiesAndMonitoring";
	public static final String DEVELOP_AND_SUBMIT_FUNDING_PROPOSALS_CODE = "DevelopAndSubmitFundingProposals";
	public static final String OBTAIN_FINANCIAL_RESOURCES_CODE = "ObtainFinancialResources";
	public static final String IMPLEMENT_STRATEGIC_AND_MONITORING_PLANS = "ImplementStrategicAndMonitoringPlans";
	public static final String IMPLEMENT_WORK_PLAN_CODE = "ImplementWorkPlan";
	
	private static final String MAIN_DESCRIPTION_RIGHT_PANEL_FILE_NAME = "dashboard/1.html";
	private static final String DESCRIPTION_3A_FILE_NAME = "dashboard/3A.html";
	private static final String DESCRIPTION_3B_FILE_NAME = "dashboard/3B.html";
	private static final String DESCRIPTION_3C_FILE_NAME = "dashboard/3C.html";
}
