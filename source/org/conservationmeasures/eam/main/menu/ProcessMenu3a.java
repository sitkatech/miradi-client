/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineAudiences;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringWizardFocusStep;

public class ProcessMenu3a extends MiradiMenu
{
	public ProcessMenu3a(Actions actionsToUse)
	{
		super(ProcessSteps.PROCESS_STEP_3A, actionsToUse);
		setMnemonic(KeyEvent.VK_F);
		
		addMenuItem(ActionJumpMonitoringWizardFocusStep.class, KeyEvent.VK_M);
		addMenuItem(ActionJumpDefineAudiences.class, KeyEvent.VK_A);
	}

}
