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

package org.miradi.dialogs.base;

import java.util.HashMap;

import org.miradi.main.EAM;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.questions.OpenStandardsConceptualizeQuestion;

public class OpenStandardsConceptualizeQuestionPanel extends AbstractOpenStandardsQuestionPanel
{
	public OpenStandardsConceptualizeQuestionPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, new OpenStandardsConceptualizeQuestion());
	}

	@Override
	public String getPanelDescription()
	{
		return "OpenStandardsConceptualizeQuestionPanel";
	}
	
	@Override
	protected void addFourthLevelRow(String code) throws Exception
	{
		if (code.equals(OpenStandardsConceptualizeQuestion.SELECT_INTIAL_TEAM_MEMBERS_CODE))
			addTeamMembersRow();
		
		if (code.equals(OpenStandardsConceptualizeQuestion.AGREE_ON_ROLES_AND_RESPONSIBILITIES_CODE))
			addAgreeOnRolesAndResponsibilities();
		
		if (code.equals(OpenStandardsConceptualizeQuestion.DEFINE_PROJECT_SCOPE_CODE))
			addDefineProjectScope();
		
		if (code.equals(OpenStandardsConceptualizeQuestion.DEVELOP_MAP_OF_PROJECT_AREA_CODE))
			addDevelopMapOfProjectArea();
		
		if (code.equals(OpenStandardsConceptualizeQuestion.SELECT_CONSERVATION_TARGETS_CODE))
			addSelectConservationTargets();
		
		if (code.equals(OpenStandardsConceptualizeQuestion.ADD_HUMAN_WELFARE_TARGETS_IF_DESIRED_CODE))
			addAddHumanWelfareTargetsIfDesired();
		
		if (code.equals(OpenStandardsConceptualizeQuestion.DESCRIBE_STATUS_OF_TARGETS_CODE))
			addDescribeStatusOfTargets();
		
		if (code.equals(OpenStandardsConceptualizeQuestion.IDENTIFY_DIRECT_THREATS_CODE))
			addIdentifyDirectThreats();
		
		if (code.equals(OpenStandardsConceptualizeQuestion.RANK_DIRECT_THREATS_CODE))
			addRankDirectThreats();
		
		if (code.equals(OpenStandardsConceptualizeQuestion.IDENTIFY_INDIRECT_THREATS_AND_OPPORTUNITIES_CODE))
			addIdentifyIndirectThreatsAndOpportunities();
		
		if (code.equals(OpenStandardsConceptualizeQuestion.ASSESS_STAKEHOLDERS_CODE))
			addAssessStakeholders();

		if (code.equals(OpenStandardsConceptualizeQuestion.CREATE_INITIAL_CONCEPTUAL_MODEL_CODE))
			addCreateInitialConceptualModel();
		
		if (code.equals(OpenStandardsConceptualizeQuestion.GROUND_THRUTH_AND_REVISE_MODEL_CODE))
			addGroundTruthAndReviseModel();
	}

	private void addTeamMembersRow() throws Exception
	{
		addFourthLevelRow(EAM.text("Team Members:"), getDashboardData(Dashboard.PSEUDO_TEAM_MEMBER_COUNT));
	}

	private void addAgreeOnRolesAndResponsibilities()
	{
	}

	private void addDefineProjectScope() throws Exception
	{
		String scopeVisionCount = getDashboardData(Dashboard.PSEUDO_PROJECT_SCOPE_WORD_COUNT);
		String rightColumnTranslatedText = EAM.substitute(EAM.text("Created (%s chars)"), scopeVisionCount);
		addFourthLevelRow("", rightColumnTranslatedText);
	}

	private void addDevelopMapOfProjectArea()
	{
	}

	private void addSelectConservationTargets() throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%targetCount", getDashboardData(Dashboard.PSEUDO_TARGET_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%targetCount created"), tokenReplacementMap);
		
		addFourthLevelRow("", rightColumnTranslatedText);
	}

	private void addAddHumanWelfareTargetsIfDesired() throws Exception
	{
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%s created"), getDashboardData(Dashboard.PSEUDO_HUMAN_WELFARE_TARGET_COUNT));
		addFourthLevelRow("", rightColumnTranslatedText);
	}

	private void addDescribeStatusOfTargets() throws Exception
	{
		HashMap<String, String> statusDescriptionTokenReplacementMap1 = new HashMap<String, String>();
		statusDescriptionTokenReplacementMap1.put("%targetWithKeaCount", getDashboardData(Dashboard.PSEUDO_TARGET_WITH_KEA_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%targetWithKeaCount targets have KEA"), statusDescriptionTokenReplacementMap1);
		addFourthLevelRow("", rightColumnTranslatedText);
		
		HashMap<String, String> statusDescriptionTokenReplacementMap2 = new HashMap<String, String>();
		statusDescriptionTokenReplacementMap2.put("%targetWithSimpleViabilityCount", getDashboardData(Dashboard.PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT));
		String leftColumnTranslatedText = EAM.substitute(EAM.text("%targetWithSimpleViabilityCount targets have simple viablity information"), statusDescriptionTokenReplacementMap2);
		addFourthLevelRow("", leftColumnTranslatedText);
	}

	private void addIdentifyDirectThreats() throws Exception
	{
		HashMap<String, String> threatsTokenReplacementMap = new HashMap<String, String>();
		threatsTokenReplacementMap.put("%threatCount", getDashboardData(Dashboard.PSEUDO_THREAT_COUNT));
		threatsTokenReplacementMap.put("%threatWithTaxonomyCount", getDashboardData(Dashboard.PSEUDO_THREAT_WITH_TAXONOMY_COUNT));

		String threatCountRightColumn = EAM.substitute(EAM.text("%threatCount Direct Threats created"), threatsTokenReplacementMap);
		addFourthLevelRow("", threatCountRightColumn);
		
		String taxonomyCountRightColumn = EAM.substitute(EAM.text("%threatWithTaxonomyCount of %threatCount have taxonomy assignments"), threatsTokenReplacementMap);
		addFourthLevelRow("", taxonomyCountRightColumn);
	}

	private void addRankDirectThreats() throws Exception
	{
		HashMap<String, String> threatTargetLinksTokenReplacementMap = new HashMap<String, String>();
		threatTargetLinksTokenReplacementMap.put("%threatTargetLinkCount", getDashboardData(Dashboard.PSEUDO_THREAT_TARGET_LINK_COUNT));
		threatTargetLinksTokenReplacementMap.put("%threatTargetWithRatingCount", getDashboardData(Dashboard.PSEUDO_THREAT_TARGET_LINK_WITH_RATING_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%threatTargetWithRatingCount of %threatTargetLinkCount threat/target links ranked"), threatTargetLinksTokenReplacementMap);

		addFourthLevelRow("", rightColumnTranslatedText);
	}

	private void addIdentifyIndirectThreatsAndOpportunities()
	{
	}

	private void addAssessStakeholders()
	{
	}

	private void addCreateInitialConceptualModel()
	{
	}

	private void addGroundTruthAndReviseModel()
	{
	}
}
