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

package org.miradi.main.menu;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import org.miradi.actions.jump.ActionJumpAssessStakeholders;
import org.miradi.actions.jump.ActionJumpDevelopMap;
import org.miradi.actions.jump.ActionJumpDiagramWizardCreateInitialModelStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardDefineTargetsStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardHumanWelfareTargetsStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardIdentifyDirectThreatStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardIdentifyIndirectThreatStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardReviewModelAndAdjustStep;
import org.miradi.actions.jump.ActionJumpSummaryWizardDefineProjecScope;
import org.miradi.actions.jump.ActionJumpSummaryWizardDefineTeamMembers;
import org.miradi.actions.jump.ActionJumpSummaryWizardRolesAndResponsibilities;
import org.miradi.actions.jump.ActionJumpTargetViabilityMethodChoiceStep;
import org.miradi.actions.jump.ActionJumpThreatMatrixOverviewStep;
import org.miradi.questions.OpenStandardsConceptualizeQuestion;

public class OpenStandardsMenuMap extends HashMap<String, MenuItemDetailsProvider>
{
	public OpenStandardsMenuMap()
	{
		setup();
	}

	private void setup()
	{
		put(OpenStandardsConceptualizeQuestion.SELECT_INTIAL_TEAM_MEMBERS_CODE, new MenuItemDetailsProvider(ActionJumpSummaryWizardDefineTeamMembers.class, KeyEvent.VK_S));
		put(OpenStandardsConceptualizeQuestion.AGREE_ON_ROLES_AND_RESPONSIBILITIES_CODE, new MenuItemDetailsProvider(ActionJumpSummaryWizardRolesAndResponsibilities.class, KeyEvent.VK_R));
		
		put(OpenStandardsConceptualizeQuestion.DEFINE_PROJECT_SCOPE_CODE, new MenuItemDetailsProvider(ActionJumpSummaryWizardDefineProjecScope.class, KeyEvent.VK_D));
		put(OpenStandardsConceptualizeQuestion.DEVELOP_MAP_OF_PROJECT_AREA_CODE, new MenuItemDetailsProvider(ActionJumpDevelopMap.class, KeyEvent.VK_M));
		put(OpenStandardsConceptualizeQuestion.SELECT_CONSERVATION_TARGETS_CODE, new MenuItemDetailsProvider(ActionJumpDiagramWizardDefineTargetsStep.class, KeyEvent.VK_I));
		put(OpenStandardsConceptualizeQuestion.ADD_HUMAN_WELFARE_TARGETS_IF_DESIRED_CODE, new MenuItemDetailsProvider(ActionJumpDiagramWizardHumanWelfareTargetsStep.class, KeyEvent.VK_A));
		put(OpenStandardsConceptualizeQuestion.DESCRIBE_STATUS_OF_TARGETS_CODE, new MenuItemDetailsProvider(ActionJumpTargetViabilityMethodChoiceStep.class, KeyEvent.VK_V));
		
		put(OpenStandardsConceptualizeQuestion.IDENTIFY_DIRECT_THREATS_CODE, new MenuItemDetailsProvider(ActionJumpDiagramWizardIdentifyDirectThreatStep.class, KeyEvent.VK_I));
		put(OpenStandardsConceptualizeQuestion.RANK_DIRECT_THREATS_CODE, new MenuItemDetailsProvider(ActionJumpThreatMatrixOverviewStep.class, KeyEvent.VK_R));
		
		put(OpenStandardsConceptualizeQuestion.IDENTIFY_INDIRECT_THREATS_AND_OPPORTUNITIES_CODE, new MenuItemDetailsProvider(ActionJumpDiagramWizardIdentifyIndirectThreatStep.class, KeyEvent.VK_I));
		put(OpenStandardsConceptualizeQuestion.ASSESS_STAKEHOLDERS_CODE, new MenuItemDetailsProvider(ActionJumpAssessStakeholders.class, KeyEvent.VK_S));
		put(OpenStandardsConceptualizeQuestion.CREATE_INITIAL_CONCEPTUAL_MODEL_CODE, new MenuItemDetailsProvider(ActionJumpDiagramWizardCreateInitialModelStep.class, KeyEvent.VK_C));
		put(OpenStandardsConceptualizeQuestion.GROUND_THRUTH_AND_REVISE_MODEL_CODE, new MenuItemDetailsProvider(ActionJumpDiagramWizardReviewModelAndAdjustStep.class, KeyEvent.VK_R));
	}
}
