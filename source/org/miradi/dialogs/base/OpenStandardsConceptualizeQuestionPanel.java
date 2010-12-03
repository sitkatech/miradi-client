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

import org.miradi.actions.AbstractJumpMenuAction;
import org.miradi.dialogs.dashboard.AbstractLongDescriptionProvider;
import org.miradi.main.EAM;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
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
	protected void addRow(ChoiceItem choiceItem, int indentCount) throws Exception
	{
		super.addRow(choiceItem, indentCount);
		
		String code = choiceItem.getCode();
		AbstractLongDescriptionProvider longDescriptionProvider = choiceItem.getLongDescriptionProvider();
		AbstractJumpMenuAction action = getMainWindow().getActions().getJumpMenuAction(code);
		String stepName = getMainWindow().getWizardManager().stripJumpPrefix(action.getClass());
		longDescriptionProvider.setWizardStepName(stepName);
		if (code.equals(OpenStandardsConceptualizeQuestion.SELECT_INTIAL_TEAM_MEMBERS_CODE))
			addTeamMembersRow(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsConceptualizeQuestion.AGREE_ON_ROLES_AND_RESPONSIBILITIES_CODE))
			addAgreeOnRolesAndResponsibilities();
		
		if (code.equals(OpenStandardsConceptualizeQuestion.DEFINE_PROJECT_SCOPE_CODE))
			addDefineProjectScope(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsConceptualizeQuestion.DEVELOP_MAP_OF_PROJECT_AREA_CODE))
			addDevelopMapOfProjectArea();
		
		if (code.equals(OpenStandardsConceptualizeQuestion.SELECT_CONSERVATION_TARGETS_CODE))
			addSelectConservationTargets(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsConceptualizeQuestion.ADD_HUMAN_WELFARE_TARGETS_IF_DESIRED_CODE))
			addAddHumanWelfareTargetsIfDesired(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsConceptualizeQuestion.DESCRIBE_STATUS_OF_TARGETS_CODE))
			addDescribeStatusOfTargets(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsConceptualizeQuestion.IDENTIFY_DIRECT_THREATS_CODE))
			addIdentifyDirectThreats(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsConceptualizeQuestion.RANK_DIRECT_THREATS_CODE))
			addRankDirectThreats(longDescriptionProvider, indentCount);
		
		if (code.equals(OpenStandardsConceptualizeQuestion.IDENTIFY_INDIRECT_THREATS_AND_OPPORTUNITIES_CODE))
			addIdentifyIndirectThreatsAndOpportunities();
		
		if (code.equals(OpenStandardsConceptualizeQuestion.ASSESS_STAKEHOLDERS_CODE))
			addAssessStakeholders();

		if (code.equals(OpenStandardsConceptualizeQuestion.CREATE_INITIAL_CONCEPTUAL_MODEL_CODE))
			addCreateInitialConceptualModel();
		
		if (code.equals(OpenStandardsConceptualizeQuestion.GROUND_THRUTH_AND_REVISE_MODEL_CODE))
			addGroundTruthAndReviseModel();
	}

	private void addTeamMembersRow(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRow(EAM.text("Team Members:"), getDashboardData(Dashboard.PSEUDO_TEAM_MEMBER_COUNT), longDescriptionProvider, indentCount);
	}

	private void addAgreeOnRolesAndResponsibilities()
	{
	}

	private void addDefineProjectScope(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("Created (%X chars)"), Dashboard.PSEUDO_PROJECT_SCOPE_WORD_COUNT);
	}

	private void addDevelopMapOfProjectArea()
	{
	}

	private void addSelectConservationTargets(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X created"), Dashboard.PSEUDO_TARGET_COUNT);
	}

	private void addAddHumanWelfareTargetsIfDesired(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X created"), Dashboard.PSEUDO_HUMAN_WELFARE_TARGET_COUNT);
	}

	private void addDescribeStatusOfTargets(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X targets have KEA"), Dashboard.PSEUDO_TARGET_WITH_KEA_COUNT);
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X targets have simple viablity information"), Dashboard.PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT);
	}

	private void addIdentifyDirectThreats(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(longDescriptionProvider, indentCount, EAM.text("%X Direct Threats created"), Dashboard.PSEUDO_THREAT_COUNT);
		addRowHelper(EAM.text("%Y of %X have taxonomy assignments"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_THREAT_WITH_TAXONOMY_COUNT, Dashboard.PSEUDO_THREAT_COUNT);
	}

	private void addRankDirectThreats(AbstractLongDescriptionProvider longDescriptionProvider, int indentCount) throws Exception
	{
		addRowHelper(EAM.text("%Y of %X threat/target links ranked"), longDescriptionProvider, indentCount, Dashboard.PSEUDO_THREAT_TARGET_LINK_COUNT, Dashboard.PSEUDO_THREAT_TARGET_LINK_WITH_RATING_COUNT);
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
