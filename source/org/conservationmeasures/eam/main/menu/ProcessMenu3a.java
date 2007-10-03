/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopSchedule;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanDevelopActivitiesAndTasksStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanDevelopMethodsAndTasksStep;

public class ProcessMenu3a extends MiradiMenu
{
	public ProcessMenu3a(Actions actionsToUse)
	{
		super(ProcessSteps.PROCESS_STEP_3A, actionsToUse);
		setMnemonic(KeyEvent.VK_F);
		
		addMenuItem(ActionJumpWorkPlanDevelopActivitiesAndTasksStep.class, KeyEvent.VK_A);
		addMenuItem(ActionJumpWorkPlanDevelopMethodsAndTasksStep.class, KeyEvent.VK_M);
		addMenuItem(ActionJumpDevelopSchedule.class, KeyEvent.VK_D);
	}

}
