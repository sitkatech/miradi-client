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
import org.miradi.views.summary.SummaryView;
import org.miradi.wizard.WizardManager;

public class OpenStandardsAnalyzeAdaptAndUseQuestion extends DynamicChoiceWithRootChoiceItem
{
	public OpenStandardsAnalyzeAdaptAndUseQuestion(WizardManager wizardManagerToUse)
	{
		wizardManager = wizardManagerToUse;
	}
	
	@Override
	public ChoiceItemWithChildren createHeaderChoiceItem() throws Exception
	{
		ChoiceItemWithChildren headerChoiceItem = new ChoiceItemWithChildren("4Header", getHeaderLabel(), new HtmlResourceLongDescriptionProvider("dashboard/4.html", wizardManager.getOverviewStepName(SummaryView.getViewName())));

		headerChoiceItem.addChild(new ChoiceItemWithChildren("4A", EAM.text("4A. Prepare Data for Analysis"), EAM.text("Develop Systems for Recording, Storing, Processing and Backing Up Project Data"), new HtmlResourceLongDescriptionProvider(PREPARE_DATA_FOR_ANALYSIS_RIGTH_SIDE_FILENAME, wizardManager.getOverviewStepName(SummaryView.getViewName()))));
		headerChoiceItem.addChild(new ChoiceItemWithChildren("4B", EAM.text("4B. Analyze Results"), EAM.text("Analyze Project Results and Assumptions Analyze Operational and Financial Data Document Discussions and Decisions"), new HtmlResourceLongDescriptionProvider(ANALYZE_RESULTS_RIGTH_SIDE_FILENAME, wizardManager.getOverviewStepName(SummaryView.getViewName()))));
		headerChoiceItem.addChild(new ChoiceItemWithChildren("4C", EAM.text("4C. Adapt Project Plan"), EAM.text("Revise Project Plan: Strategic Monitoring Operational Work Plan"), new HtmlResourceLongDescriptionProvider(ADAPT_PROJECT_PLAN_RIGTH_SIDE_FILENAME, wizardManager.getOverviewStepName(SummaryView.getViewName()))));

		return headerChoiceItem;
	}

	public static String getHeaderLabel()
	{
		return EAM.text("4. Analyze, Use and Adapt");
	}
	
	private static final String PREPARE_DATA_FOR_ANALYSIS_RIGTH_SIDE_FILENAME = "dashboard/4A.html";
	private static final String ANALYZE_RESULTS_RIGTH_SIDE_FILENAME = "dashboard/4B.html";
	private static final String ADAPT_PROJECT_PLAN_RIGTH_SIDE_FILENAME = "dashboard/4C.html";
	private WizardManager wizardManager;
}
