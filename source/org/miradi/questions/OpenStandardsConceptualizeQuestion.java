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
		
		processStep1A.addChild(new ChoiceItem(SELECT_INTIAL_TEAM_MEMBERS_CODE, getSelectIntialTeamMembersLabel()));
		processStep1A.addChild(new ChoiceItem(AGREE_ON_ROLES_AND_RESPONSIBILITIES_CODE, getAgreeOnRolesAndResponsibilitiesLabel()));
		
		ChoiceItemWithChildren processStep1BChoiceItem = new ChoiceItemWithChildren("ProcessStep1B", EAM.text("ProcessStep|1B. Define Scope, Vision, and Targets"), EAM.text(""), provider);
		headerChoiceItem.addChild(processStep1BChoiceItem);
		processStep1BChoiceItem.addChild(new ChoiceItem(DEFINE_PROJECT_SCOPE_CODE, getDefineProjectScopeLabel()));
		processStep1BChoiceItem.addChild(new ChoiceItem(DEVELOP_MAP_OF_PROJECT_AREA_CODE, getDevelopMapOfProjectAreaLabel()));
		processStep1BChoiceItem.addChild(new ChoiceItem(SELECT_CONSERVATION_TARGETS_CODE, getSelectConservationTargetsLabel()));
		processStep1BChoiceItem.addChild(new ChoiceItem(ADD_HUMAN_WELFARE_TARGETS_IF_DESIRED_CODE, getAddHumanWelfareTargetsIfDesiredLabel()));
		processStep1BChoiceItem.addChild(new ChoiceItem(DESCRIBE_STATUS_OF_TARGETS_CODE, getDescribeStatusOfTargetsLabel()));
		
		ChoiceItemWithChildren processStep1CChoiceItem = new ChoiceItemWithChildren("ProcessStep1C", EAM.text("ProcessStep|1C. Identify Critical Threats"), EAM.text(""), provider);
		headerChoiceItem.addChild(processStep1CChoiceItem);
		processStep1CChoiceItem.addChild(new ChoiceItem(IDENTIFY_DIRECT_THREATS_CODE, getIdentifyDirectThreatsLabel()));
		processStep1CChoiceItem.addChild(new ChoiceItem(RANK_DIRECT_THREATS_CODE, getRankDirectThreatsLabel()));
		
		ChoiceItemWithChildren processStep1DChoiceItem = new ChoiceItemWithChildren("ProcessStep1D", EAM.text("ProcessStep|1D. Complete Situation Analysis"), EAM.text(""), provider);
		headerChoiceItem.addChild(processStep1DChoiceItem);
		processStep1DChoiceItem.addChild(new ChoiceItem(IDENTIFY_INDIRECT_THREATS_AND_OPPORTUNITIES_CODE, getIdentifyIndirectThreatsAndOpportunitiesLabel()));
		processStep1DChoiceItem.addChild(new ChoiceItem(ASSESS_STAKEHOLDERS_CODE, getAssessStakeholdersLabel()));
		processStep1DChoiceItem.addChild(new ChoiceItem(CREATE_INITIAL_CONCEPTUAL_MODEL_CODE, getCreateInitialConceptualModelLabel()));
		processStep1DChoiceItem.addChild(new ChoiceItem(GROUND_THRUTH_AND_REVISE_MODEL_CODE, getGroundTruthAndReviseModelLabel()));
		
		return headerChoiceItem;
	}
	
	public static String getSelectIntialTeamMembersLabel()
	{
		return EAM.text("Select initial team members");
	}
	
	public static String getAgreeOnRolesAndResponsibilitiesLabel()
	{
		return EAM.text("Agree on roles and responsibilities"); 
	}
	
	public static String getDefineProjectScopeLabel()
	{
		return EAM.text("Define project scope");
	}
	
	public static String getDevelopMapOfProjectAreaLabel()
	{
		return EAM.text("Develop map of project area");
	}
	
	public static String getSelectConservationTargetsLabel()
	{
		return EAM.text("Select conservation targets");
	}
	
	public static String getAddHumanWelfareTargetsIfDesiredLabel()
	{
		return EAM.text("Add Human Welfare Targets If Desired");
	}
	
	public static String getDescribeStatusOfTargetsLabel()
	{
		return EAM.text("Describe status of targets"); 
	}
	
	public static String getIdentifyDirectThreatsLabel()
	{
		return EAM.text("Identify direct threats");
	}

	public static String getRankDirectThreatsLabel()
	{
		return EAM.text("Rank direct threats");
	}

	public static String getIdentifyIndirectThreatsAndOpportunitiesLabel()
	{
		return EAM.text("Identify indirect threats and opportunities");
	}
	
	public static String getAssessStakeholdersLabel()
	{
		return EAM.text("Assess Stakeholders");
	}

	public static String getCreateInitialConceptualModelLabel()
	{
		return EAM.text("Create initial conceptual model");
	}
	
	public static String getGroundTruthAndReviseModelLabel()
	{
		return EAM.text("Ground-truth and revise model");
	}

	public static final String SELECT_INTIAL_TEAM_MEMBERS_CODE = "SelectInitialTeamMembers";
	public static final String AGREE_ON_ROLES_AND_RESPONSIBILITIES_CODE = "AgreeOnRolesAndResponsibilities";
	public static final String DEFINE_PROJECT_SCOPE_CODE = "DefineProjectScope";
	public static final String DEVELOP_MAP_OF_PROJECT_AREA_CODE = "DevelopMapOfProjectArea";
	public static final String SELECT_CONSERVATION_TARGETS_CODE = "SelectConservationTargets";
	public static final String ADD_HUMAN_WELFARE_TARGETS_IF_DESIRED_CODE = "AddHumanWelfareTargetsIfDesired";
	public static final String DESCRIBE_STATUS_OF_TARGETS_CODE = "DescribeStatusOfTargets";
	public static final String IDENTIFY_DIRECT_THREATS_CODE = "IdentifyDirectThreats";
	public static final String RANK_DIRECT_THREATS_CODE = "RankDirectThreats";
	public static final String IDENTIFY_INDIRECT_THREATS_AND_OPPORTUNITIES_CODE = "IdentifyIndirectThreatsAndOpportunities";
	public static final String ASSESS_STAKEHOLDERS_CODE = "AssessStakeholders";
	public static final String CREATE_INITIAL_CONCEPTUAL_MODEL_CODE = "CreateInitialConceptualModel";
	public static final String GROUND_THRUTH_AND_REVISE_MODEL_CODE = "Ground-truthAndReviseModel";
}
