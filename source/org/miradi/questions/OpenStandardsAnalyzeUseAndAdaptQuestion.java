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

public class OpenStandardsAnalyzeUseAndAdaptQuestion extends DynamicChoiceWithRootChoiceItem
{
	@Override
	protected ChoiceItemWithChildren createHeaderChoiceItem() throws Exception
	{
		ChoiceItemWithChildren headerChoiceItem = new ChoiceItemWithChildren(ANALYZE_USE_AND_ADAPT_HEADER_CODE, getHeaderLabel(), new HtmlResourceLongDescriptionProvider(MAIN_DESCRIPTION_FILENAME));
		
		
		ChoiceItemWithChildren processStep4a = new ChoiceItemWithChildren(PROCESS_STEP_4A_CODE, getProcessStep4a(), EAM.text(""), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_4A_FILENAME));
		headerChoiceItem.addChild(processStep4a);
		processStep4a.addChild(new ChoiceItem("DevelopSystemsForRecordingStoringProcessingAndBackingUpProjectData", EAM.text("Develop systems for recording, storing, processing and backing up project data"), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_4A_FILENAME)));
		
		ChoiceItemWithChildren processStep4bChoiceItem = new ChoiceItemWithChildren(PROCESS_STEP_4B_CODE, getProcessStep4b(), EAM.text(""), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_4B_FILENAME));
		headerChoiceItem.addChild(processStep4bChoiceItem);
		processStep4bChoiceItem.addChild(new ChoiceItem("AnalyzeProjectResultsSndSssumptions", (EAM.text("Analyze project results and assumptions")), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_4B_FILENAME)));
		processStep4bChoiceItem.addChild(new ChoiceItem("AnalyzeOperationalAndFinancialData", (EAM.text("Analyze operational and financial data")), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_4B_FILENAME)));
		processStep4bChoiceItem.addChild(new ChoiceItem("DocumentDiscussionsAndDecisions", (EAM.text("Document discussions and decisions")), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_4B_FILENAME)));

		ChoiceItemWithChildren processStep4cChoiceItem = new ChoiceItemWithChildren(PROCESS_STEP_4C_CODE, getProcessStep4c(), EAM.text(""), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_4C_FILENAME));
		headerChoiceItem.addChild(processStep4cChoiceItem);
		processStep4cChoiceItem.addChild(new ChoiceItem("ReviseProjectPlanStrategicMonitoringOperationalAndWorkPlans", EAM.text("Revise project plan: strategic, monitoring, operational, and work plans"), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_4C_FILENAME)));
		
		return headerChoiceItem;
	}

	public static String getHeaderLabel()
	{
		return EAM.text("Menu|4. Analyze, Use, Adapt");
	}
	
	public static String getProcessStep4a()
	{
		return EAM.text("ProcessStep|4A. Prepare Data for Analysis");
	}
	
	public static String getProcessStep4b()
	{
		return EAM.text("ProcessStep|4B. Analyze Results");
	}
	
	public static String getProcessStep4c()
	{
		return EAM.text("ProcessStep|4C. Adapt Project Plan");
	}
	
	private static final String MAIN_DESCRIPTION_FILENAME =  "dashboard/4.html";
	private static final String PROCESS_STEP_4A_FILENAME = "dashboard/4A.html";
	private static final String PROCESS_STEP_4B_FILENAME = "dashboard/4B.html";
	private static final String PROCESS_STEP_4C_FILENAME = "dashboard/4C.html";


	public static final String ANALYZE_USE_AND_ADAPT_HEADER_CODE = "AnalyzeUseAndAdapt";
	public static final String PROCESS_STEP_4A_CODE = "ProcessStep4A";
	public static final String PROCESS_STEP_4B_CODE = "ProcessStep4B";
	public static final String PROCESS_STEP_4C_CODE = "ProcessStep4C";
}
