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

public class OpenStandardsCaptureAndShareLearningQuestion extends DynamicChoiceWithRootChoiceItem
{
	@Override
	protected ChoiceItemWithChildren createHeaderChoiceItem() throws Exception
	{
		StaticLongDescriptionProvider provider = new StaticLongDescriptionProvider();
		ChoiceItemWithChildren headerChoiceItem = new ChoiceItemWithChildren(HEADER_CODE, getHeaderLabel(), provider);
		
		ChoiceItemWithChildren processStep5a = new ChoiceItemWithChildren(PROCESS_STEP_5A_CODE, EAM.text(getProcessStep5aLabel()), EAM.text(""), provider);
		headerChoiceItem.addChild(processStep5a);
		processStep5a.addChild(new ChoiceItem("DocumentKeyResultsAndLessons", EAM.text("Document key results and lessons")));
		
		ChoiceItemWithChildren processStep5bChoiceItem = new ChoiceItemWithChildren(PROCESS_STEP_5B_CODE, getProcessStep5bLabel(), EAM.text(""), provider);
		headerChoiceItem.addChild(processStep5bChoiceItem);
		processStep5bChoiceItem.addChild(new ChoiceItem("IdentifyKeyAudiences", EAM.text("Identify key audiences")));
		processStep5bChoiceItem.addChild(new ChoiceItem("DevelopCommunicationsStrategy", EAM.text("Develop communications strategy")));
		processStep5bChoiceItem.addChild(new ChoiceItem("ReportToProjectTeamAndStakeholders", EAM.text("Report to project team and stakeholders")));
		processStep5bChoiceItem.addChild(new ChoiceItem("DevelopAndShareCommunicationProducts", EAM.text("Develop and share communication products")));
		processStep5bChoiceItem.addChild(new ChoiceItem("UseOthersCommunicationProducts", EAM.text("Use other's communication products")));
		
		ChoiceItemWithChildren processStep5cChoiceItem = new ChoiceItemWithChildren(PROCESS_STEP_5C_CODE, getProcessStep5cLabel(), EAM.text(""), provider);
		headerChoiceItem.addChild(processStep5cChoiceItem);
		processStep5cChoiceItem.addChild(new ChoiceItem("ShareFeedbackFormallyOrInformally", EAM.text("Share feedback formally or informally")));
		processStep5cChoiceItem.addChild(new ChoiceItem("ConductEvaluationsAndOrAuditsAtAppropriateTimesDuringTheProjectCycle", EAM.text("Conduct evaluations and/or audits at appropriate times during the project cycle")));
		processStep5cChoiceItem.addChild(new ChoiceItem("DemonstrateCommitmentFromLeadersToLearningAndInnovation", EAM.text("Demonstrate commitment from leaders to learning and innovation")));
		processStep5cChoiceItem.addChild(new ChoiceItem("ProvideASafeEnvironmentForEncouragingExperimentation", EAM.text("Provide a safe environment for encouraging experimentation")));
		processStep5cChoiceItem.addChild(new ChoiceItem("Share success & failures with practitioners around the world", EAM.text("Share success & failures with practitioners around the world")));

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

	public static final String HEADER_CODE = "CaptureAndShareLearning";
	public static final String PROCESS_STEP_5A_CODE = "ProcessStep5A";
	public static final String PROCESS_STEP_5B_CODE = "ProcessStep5B";
	public static final String PROCESS_STEP_5C_CODE = "ProcessStep5C";
}
