/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.main.menu;

import java.awt.event.KeyEvent;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpAssessStakeholders;
import org.miradi.actions.jump.ActionJumpDiagramWizardConstructChainsStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardIdentifyIndirectThreatStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardReviewModelAndAdjustStep;

public class ProcessMenu1d extends MiradiMenu
{

	public ProcessMenu1d(Actions actionsToUse)
	{
		super(ProcessSteps.PROCESS_STEP_1D, actionsToUse);
		setMnemonic(KeyEvent.VK_M);
		
		addMenuItem(ActionJumpDiagramWizardIdentifyIndirectThreatStep.class, KeyEvent.VK_I);
		addMenuItem(ActionJumpAssessStakeholders.class, KeyEvent.VK_S);
		addMenuItem(ActionJumpDiagramWizardConstructChainsStep.class, KeyEvent.VK_C); 
//		addMenuItem(ActionJumpAnalyzeProjectCapacity.class, KeyEvent.VK_C);
		
//		addMenuItem(ActionJumpArticulateCoreAssumptions.class, KeyEvent.VK_A);
		addMenuItem(ActionJumpDiagramWizardReviewModelAndAdjustStep.class, KeyEvent.VK_R);
//		addMenuItem(ActionJumpGroundTruthRevise.class, KeyEvent.VK_G);
	}

}
