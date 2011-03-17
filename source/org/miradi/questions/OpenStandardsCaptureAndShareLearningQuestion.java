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

public class OpenStandardsCaptureAndShareLearningQuestion extends DynamicChoiceWithRootChoiceItem
{
	@Override
	protected ChoiceItemWithChildren createHeaderChoiceItem() throws Exception
	{
		ChoiceItemWithChildren headerChoiceItem = new ChoiceItemWithChildren(HEADER_CODE, getHeaderLabel(), new HtmlResourceLongDescriptionProvider(MAIN_DESCRIPTION_FILENAME));
		
		ChoiceItemWithChildren processStep5a = new ChoiceItemWithChildren(PROCESS_STEP_5A_CODE, EAM.text(getProcessStep5aLabel()), EAM.emptyText(), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_5A_FILENAME));
		headerChoiceItem.addChild(processStep5a);
		processStep5a.addChild(new ChoiceItem("DocumentKeyResultsAndLessons", EAM.text("Document key results and lessons"), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_5A_FILENAME)));
		
		ChoiceItemWithChildren processStep5bChoiceItem = new ChoiceItemWithChildren(PROCESS_STEP_5B_CODE, getProcessStep5bLabel(), EAM.text(EAM.emptyText()), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_5B_FILENAME));
		headerChoiceItem.addChild(processStep5bChoiceItem);
		processStep5bChoiceItem.addChild(new ChoiceItem("IdentifyKeyAudiences", EAM.text("Identify key audiences"), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_5B_FILENAME)));
		processStep5bChoiceItem.addChild(new ChoiceItem("DevelopCommunicationsStrategy", EAM.text("Develop communications strategy"), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_5B_FILENAME)));
		processStep5bChoiceItem.addChild(new ChoiceItem("ReportToProjectTeamAndStakeholders", EAM.text("Report to project team and stakeholders"), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_5B_FILENAME)));
		processStep5bChoiceItem.addChild(new ChoiceItem("DevelopAndShareCommunicationProducts", EAM.text("Develop and share communication products"), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_5B_FILENAME)));
		processStep5bChoiceItem.addChild(new ChoiceItem("UseOthersCommunicationProducts", EAM.text("Use other's communication products"), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_5B_FILENAME)));
		
		ChoiceItemWithChildren processStep5cChoiceItem = new ChoiceItemWithChildren(PROCESS_STEP_5C_CODE, getProcessStep5cLabel(), EAM.emptyText(), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_5C_FILENAME));
		headerChoiceItem.addChild(processStep5cChoiceItem);
		processStep5cChoiceItem.addChild(new ChoiceItem("ShareFeedbackFormallyOrInformally", EAM.text("Share feedback formally or informally"), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_5C_FILENAME)));
		processStep5cChoiceItem.addChild(new ChoiceItem("ConductEvaluationsAndOrAuditsAtAppropriateTimesDuringTheProjectCycle", EAM.text("Conduct evaluations and/or audits"), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_5C_FILENAME)));
		processStep5cChoiceItem.addChild(new ChoiceItem("DemonstrateCommitmentFromLeadersToLearningAndInnovation", EAM.text("Demonstrate commitment to learning"), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_5C_FILENAME)));
		processStep5cChoiceItem.addChild(new ChoiceItem("ProvideASafeEnvironmentForEncouragingExperimentation", EAM.text("Provide a safe environment for experimentation"), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_5C_FILENAME)));
		processStep5cChoiceItem.addChild(new ChoiceItem("Share success & failures with practitioners around the world", EAM.text("Share success & failures with other teams"), new HtmlResourceLongDescriptionProvider(PROCESS_STEP_5C_FILENAME)));

		return headerChoiceItem;
	}

	public static String getHeaderLabel()
	{
		return EAM.text("Menu|5. Capture and Share Learning");
	}

	public static String getProcessStep5aLabel()
	{
		return "ProcessStep|5A. Document Learning";
	}
	
	public static String getProcessStep5bLabel()
	{
		return EAM.text("ProcessStep|5B. Share Learning");
	}

	public static String getProcessStep5cLabel()
	{
		return EAM.text("ProcessStep|5C. Create a Learning Environment");
	}
	
	public static final String MAIN_DESCRIPTION_FILENAME =  "dashboard/5.html";
	private static final String PROCESS_STEP_5A_FILENAME = "dashboard/5A.html";
	private static final String PROCESS_STEP_5B_FILENAME = "dashboard/5B.html";
	private static final String PROCESS_STEP_5C_FILENAME = "dashboard/5C.html";

	public static final String HEADER_CODE = "CaptureAndShareLearning";
	public static final String PROCESS_STEP_5A_CODE = "ProcessStep5A";
	public static final String PROCESS_STEP_5B_CODE = "ProcessStep5B";
	public static final String PROCESS_STEP_5C_CODE = "ProcessStep5C";
}
