/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.main.menu;

import java.awt.event.KeyEvent;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpDiagramWizardResultsChainStep;
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
		addMenuItem(ActionJumpDiagramWizardResultsChainStep.class, KeyEvent.VK_R);
		addMenuItem(ActionJumpStrategicPlanDevelopObjectivesStep.class, KeyEvent.VK_O);
		
		// NOTE: Finalize CM is still in the Open Standards, but we are leaving it out,
		// because it doesn't fit well here, and we think the standards will agree at some point
		//addMenuItem(ActionJumpFinalizeConceptualModel.class, KeyEvent.VK_F);
		
		addMenuItem(ActionJumpPlanningWizardFinalizeStrategicPlanStep.class, KeyEvent.VK_E);
		//addMenuItem(ActionJumpAnalyzeResourcesFeasibilityAndRisk.class, KeyEvent.VK_A);
	}
}
