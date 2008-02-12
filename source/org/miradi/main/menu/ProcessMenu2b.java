/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.main.menu;

import java.awt.event.KeyEvent;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpMonitoringWizardDefineIndicatorsStep;
import org.miradi.actions.jump.ActionJumpMonitoringWizardFocusStep;
import org.miradi.actions.jump.ActionJumpPlanningWizardFinalizeMonitoringPlanStep;

public class ProcessMenu2b extends MiradiMenu
{
	public ProcessMenu2b(Actions actions)
	{
		super(ProcessSteps.PROCESS_STEP_2B, actions);
		setMnemonic(KeyEvent.VK_S);
		
		addMenuItem(ActionJumpMonitoringWizardFocusStep.class, KeyEvent.VK_M);
		//addMenuItem(ActionJumpDefineAudiences.class, KeyEvent.VK_A);
		addMenuItem(ActionJumpMonitoringWizardDefineIndicatorsStep.class, KeyEvent.VK_I);
		//addMenuItem(ActionJumpPlanDataStorage.class, KeyEvent.VK_D);
		addMenuItem(ActionJumpPlanningWizardFinalizeMonitoringPlanStep.class, KeyEvent.VK_F);
	}
}
