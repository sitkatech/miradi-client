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

import org.miradi.dialogs.dashboard.StaticLongDescriptionProvider;
import org.miradi.main.EAM;

public class OpenStandardsImplementActionsAndMonitoringQuestion extends DynamicChoiceWithRootChoiceItem
{
	@Override
	protected ChoiceItemWithChildren createHeaderChoiceItem() throws Exception
	{
		StaticLongDescriptionProvider provider = new StaticLongDescriptionProvider();
		ChoiceItemWithChildren headerChoiceItem = new ChoiceItemWithChildren("ImplementActionsAndMonitoring", EAM.text("Menu|3. Implement Actions &  Monitoring"), provider);
		
		ChoiceItemWithChildren processStep3a = new ChoiceItemWithChildren("ProcessStep3A", EAM.text("ProcessStep|3A. Develop Short-Term Work Plan"), EAM.text(""), provider);
		headerChoiceItem.addChild(processStep3a);
		processStep3a.addChild(new ChoiceItem(DETAIL_ACTIVITIES_TASKS_AND_RESPONSIBILITIES_CODE, getDetailActivitiesTasksAndResponsibilitiesLabel()));
		processStep3a.addChild(new ChoiceItem(DETAIL_METHODS_TASKS_AND_RESPONSIBILITIES_CODE, getDetailMethodsTasksAndResponsibilitiesLabel()));
		processStep3a.addChild(new ChoiceItem(DEVELOP_PROJECT_TIMELINE_OR_CALENDAR_CODE, getDevelopProjectTimelineOrCalendarLabel()));
		
		ChoiceItemWithChildren processStep3bChoiceItem = new ChoiceItemWithChildren("ProcessStep3B", EAM.text("ProcessStep|3B. Develop and Refine Project Budget"), EAM.text(""), provider);
		headerChoiceItem.addChild(processStep3bChoiceItem);
		processStep3bChoiceItem.addChild(new ChoiceItem(ESTIMATE_COSTS_FOR_ACTIVITIES_AND_MONITORING_CODE, getEstimateCostsForActivitiesAndMonitoringLabel()));
		processStep3bChoiceItem.addChild(new ChoiceItem(DEVELOP_AND_SUBMIT_FUNDING_PROPOSALS_CODE, getDevelopAndSubmitFundingProposalsLabel()));
		
		ChoiceItemWithChildren processStep3cChoiceItem = new ChoiceItemWithChildren("ProcessStep3C", EAM.text("ProcessStep|3C. Implement Plans"), EAM.text(""), provider);
		headerChoiceItem.addChild(processStep3cChoiceItem);
		processStep3cChoiceItem.addChild(new ChoiceItem(IMPLEMENT_STRATEGIC_AND_MONITORING_PLANS, getImplementStrategicAndMonitoringPlansLabel()));
		processStep3cChoiceItem.addChild(new ChoiceItem(IMPLEMENT_WORK_PLAN_CODE, getImplementWorkPlanLabel()));
		
		return headerChoiceItem;
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

	public static final String DETAIL_ACTIVITIES_TASKS_AND_RESPONSIBILITIES_CODE = "DetailActivitiesTasksAndResponsibilities";
	public static final String DETAIL_METHODS_TASKS_AND_RESPONSIBILITIES_CODE = "DetailMethodsTasksAndResponsibilities";
	public static final String DEVELOP_PROJECT_TIMELINE_OR_CALENDAR_CODE = "DevelopProjectTimelineOrCalendar";
	public static final String ESTIMATE_COSTS_FOR_ACTIVITIES_AND_MONITORING_CODE = "EstimateCostsForActivitiesAndMonitoring";
	public static final String DEVELOP_AND_SUBMIT_FUNDING_PROPOSALS_CODE = "DevelopAndSubmitFundingProposals";
	public static final String OBTAIN_FINANCIAL_RESOURCES_CODE = "ObtainFinancialResources";
	public static final String IMPLEMENT_STRATEGIC_AND_MONITORING_PLANS = "ImplementStrategicAndMonitoringPlans";
	public static final String IMPLEMENT_WORK_PLAN_CODE = "ImplementWorkPlan";
}
