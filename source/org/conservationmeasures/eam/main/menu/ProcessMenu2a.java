/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardResultsChainStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpFinalizeConceptualModel;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDraftStrategiesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpPlanningWizardFinalizeStrategicPlanStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectChainStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanDevelopGoalStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanDevelopObjectivesStep;

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
		addMenuItem(ActionJumpFinalizeConceptualModel.class, KeyEvent.VK_F);
		addMenuItem(ActionJumpPlanningWizardFinalizeStrategicPlanStep.class, KeyEvent.VK_E);
		//addMenuItem(ActionJumpAnalyzeResourcesFeasibilityAndRisk.class, KeyEvent.VK_A);
	}
}
