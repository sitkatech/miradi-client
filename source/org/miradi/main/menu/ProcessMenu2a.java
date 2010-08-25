/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpDiagramWizardResultsChainSelectStrategyStep;
import org.miradi.actions.jump.ActionJumpPlanningWizardFinalizeStrategicPlanStep;
import org.miradi.actions.jump.ActionJumpRankDraftStrategiesStep;
import org.miradi.actions.jump.ActionJumpSelectChainStep;
import org.miradi.actions.jump.ActionJumpStrategicPlanDevelopGoalStep;
import org.miradi.actions.jump.ActionJumpStrategicPlanDevelopObjectivesStep;

public class ProcessMenu2a extends MiradiMenu
{
	public ProcessMenu2a(Actions actions)
	{
		super(ProcessSteps.PROCESS_STEP_2A, actions);
		setMnemonic(KeyEvent.VK_D);
		
		addMenuItem(ActionJumpStrategicPlanDevelopGoalStep.class, KeyEvent.VK_G);
		addMenuItem(ActionJumpSelectChainStep.class, KeyEvent.VK_I);
		addMenuItem(ActionJumpRankDraftStrategiesStep.class, KeyEvent.VK_S);
		addMenuItem(ActionJumpDiagramWizardResultsChainSelectStrategyStep.class, KeyEvent.VK_R);
		addMenuItem(ActionJumpStrategicPlanDevelopObjectivesStep.class, KeyEvent.VK_O);
		
		// NOTE: Finalize CM is still in the Open Standards, but we are leaving it out,
		// because it doesn't fit well here, and we think the standards will agree at some point
		//addMenuItem(ActionJumpFinalizeConceptualModel.class, KeyEvent.VK_F);
		
		addMenuItem(ActionJumpPlanningWizardFinalizeStrategicPlanStep.class, KeyEvent.VK_E);
		//addMenuItem(ActionJumpAnalyzeResourcesFeasibilityAndRisk.class, KeyEvent.VK_A);
	}
}
