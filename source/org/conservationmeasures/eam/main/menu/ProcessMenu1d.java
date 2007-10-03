/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeProjectCapacity;
import org.conservationmeasures.eam.actions.jump.ActionJumpArticulateCoreAssumptions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAssessStakeholders;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardIdentifyIndirectThreatStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardReviewModelAndAdjustStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpGroundTruthRevise;

public class ProcessMenu1d extends MiradiMenu
{

	public ProcessMenu1d(Actions actionsToUse)
	{
		super(ProcessSteps.PROCESS_STEP_1D, actionsToUse);
		setMnemonic(KeyEvent.VK_M);
		
		addMenuItem(ActionJumpDiagramWizardIdentifyIndirectThreatStep.class, KeyEvent.VK_I);
		addMenuItem(ActionJumpAssessStakeholders.class, KeyEvent.VK_S);
		addMenuItem(ActionJumpAnalyzeProjectCapacity.class, KeyEvent.VK_C);
		
		addMenuItem(ActionJumpArticulateCoreAssumptions.class, KeyEvent.VK_A);
		addMenuItem(ActionJumpDiagramWizardReviewModelAndAdjustStep.class, KeyEvent.VK_R);
		addMenuItem(ActionJumpGroundTruthRevise.class, KeyEvent.VK_G);
	}

}
