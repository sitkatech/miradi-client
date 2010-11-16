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

public class OpenStandardsCaptureAndShareLearningQuestion extends DynamicChoiceWithRootChoiceItem
{
	public OpenStandardsCaptureAndShareLearningQuestion(WizardManager wizardManagerToUse)
	{
		wizardManager = wizardManagerToUse;
	}
	
	@Override
	public ChoiceItemWithChildren createHeaderChoiceItem() throws Exception
	{
		ChoiceItemWithChildren headerChoiceItem = new ChoiceItemWithChildren("5Header", EAM.text("5. Capture and Share Learning"), new HtmlResourceLongDescriptionProvider("dashboard/5.html", wizardManager.getOverviewStepName(SummaryView.getViewName())));

		headerChoiceItem.addChild(new ChoiceItemWithChildren("5A", EAM.text("5A. Document Learning"), EAM.text("Document Key Results and Lessons"), new HtmlResourceLongDescriptionProvider(DOCUMENT_LEARNING_RIGHT_SIDE_FILENAME, wizardManager.getOverviewStepName(SummaryView.getViewName()))));
		headerChoiceItem.addChild(new ChoiceItemWithChildren("5B", EAM.text("5B. Share Learning"), EAM.text("Identify Key Audiences Develop Communications Strategy Report to Project Team and Stakeholders Develop and Share Communication Products Use Other's Communication Products"), new HtmlResourceLongDescriptionProvider(SHARE_LEARNING_RIGHT_SIDE_FILENAME, wizardManager.getOverviewStepName(SummaryView.getViewName()))));
		headerChoiceItem.addChild(new ChoiceItemWithChildren("5C", EAM.text("5C. Create a Learning Enviornment"), EAM.text("Share Feedback Formally or Informally Conduct Evaluations... Demonstrate Commitment... Provide Safe Environment... Share Successes and Failures..."), new HtmlResourceLongDescriptionProvider(CREATE_LEARNING_ENVIRONMENT_RIGHT_SIDE_FILENAME, wizardManager.getOverviewStepName(SummaryView.getViewName()))));

		return headerChoiceItem;
	}
	
	private WizardManager wizardManager;
	private static final String DOCUMENT_LEARNING_RIGHT_SIDE_FILENAME = "dashboard/5A.html";
	private static final String SHARE_LEARNING_RIGHT_SIDE_FILENAME = "dashboard/5B.html";
	private static final String CREATE_LEARNING_ENVIRONMENT_RIGHT_SIDE_FILENAME = "dashboard/5C.html";
}
