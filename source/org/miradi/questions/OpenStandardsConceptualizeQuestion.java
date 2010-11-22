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

public class OpenStandardsConceptualizeQuestion extends DynamicChoiceWithRootChoiceItem
{
	@Override
	protected ChoiceItemWithChildren createHeaderChoiceItem() throws Exception
	{
		StaticLongDescriptionProvider provider = new StaticLongDescriptionProvider();
		ChoiceItemWithChildren headerChoiceItem = new ChoiceItemWithChildren("Conceptualize", EAM.text("Menu|1. Conceptualize Project"), provider);
		
		ChoiceItemWithChildren processStep1A = new ChoiceItemWithChildren("ProcessStep1A", EAM.text("ProcessStep|1A. Define Initial Project Team"), EAM.text(""), provider);
		headerChoiceItem.addChild(processStep1A);
		processStep1A.addChild(new ChoiceItem("SelectInitialTeamMembers", EAM.text("Select initial team members")));
		processStep1A.addChild(new ChoiceItem("AgreeOnRolesAndResponsibilities", EAM.text("Agree on roles and responsibilities")));
		
		ChoiceItemWithChildren processStep1BChoiceItem = new ChoiceItemWithChildren("ProcessStep1B", EAM.text("ProcessStep|1B. Define Scope, Vision, and Targets"), EAM.text(""), provider);
		headerChoiceItem.addChild(processStep1BChoiceItem);
		processStep1BChoiceItem.addChild(new ChoiceItem("DefineProjectScope", EAM.text("Define project scope")));
		processStep1BChoiceItem.addChild(new ChoiceItem("DevelopMapOfProjectArea", EAM.text("Develop map of project area")));
		processStep1BChoiceItem.addChild(new ChoiceItem("SelectConservationTargets", EAM.text("Select conservation targets")));
		processStep1BChoiceItem.addChild(new ChoiceItem("AddHumanWelfareTargetsIfDesired", EAM.text("Add Human Welfare Targets If Desired")));
		processStep1BChoiceItem.addChild(new ChoiceItem("DescribeStatusOfTargets", EAM.text("Describe status of targets")));
		
		ChoiceItemWithChildren processStep1CChoiceItem = new ChoiceItemWithChildren("ProcessStep1C", EAM.text("ProcessStep|1C. Identify Critical Threats"), EAM.text(""), provider);
		headerChoiceItem.addChild(processStep1CChoiceItem);
		processStep1CChoiceItem.addChild(new ChoiceItem("IdentifyDirectThreats", EAM.text("Identify direct threats")));
		processStep1CChoiceItem.addChild(new ChoiceItem("RankDirectThreats", EAM.text("Rank direct threats")));
		
		ChoiceItemWithChildren processStep1DChoiceItem = new ChoiceItemWithChildren("ProcessStep1D", EAM.text("ProcessStep|1D. Complete Situation Analysis"), EAM.text(""), provider);
		headerChoiceItem.addChild(processStep1DChoiceItem);
		processStep1DChoiceItem.addChild(new ChoiceItem("IdentifyIndirectThreatsAndOpportunities", EAM.text("Identify indirect threats and opportunities")));
		processStep1DChoiceItem.addChild(new ChoiceItem("AssessStakeholders", EAM.text("Assess Stakeholders")));
		processStep1DChoiceItem.addChild(new ChoiceItem("CreateInitialConceptualModel", EAM.text("Create initial conceptual model")));
		processStep1DChoiceItem.addChild(new ChoiceItem("Ground-truthAndReviseModel", EAM.text("Ground-truth and revise model")));
		
		return headerChoiceItem;
	}

}
