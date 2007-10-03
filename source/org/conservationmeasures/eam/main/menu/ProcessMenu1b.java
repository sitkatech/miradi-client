/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardDefineTargetsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardProjectScopeStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardVisionStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpTargetViabilityMethodChoiceStep;

public class ProcessMenu1b extends MiradiMenu
{
	public ProcessMenu1b(Actions actionsToUse)
	{
		super(ProcessSteps.PROCESS_STEP_1B, actionsToUse);
		setMnemonic(KeyEvent.VK_D);
		
		addMenuItem(ActionJumpDiagramWizardProjectScopeStep.class, KeyEvent.VK_D);
		addMenuItem(ActionJumpDiagramWizardVisionStep.class, KeyEvent.VK_E);
		addMenuItem(ActionJumpDiagramWizardDefineTargetsStep.class, KeyEvent.VK_I);
		addMenuItem(ActionJumpTargetViabilityMethodChoiceStep.class, KeyEvent.VK_V);
	}
}
