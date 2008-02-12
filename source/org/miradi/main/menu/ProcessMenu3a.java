/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.main.menu;

import java.awt.event.KeyEvent;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpDevelopSchedule;
import org.miradi.actions.jump.ActionJumpWorkPlanDevelopActivitiesAndTasksStep;
import org.miradi.actions.jump.ActionJumpWorkPlanDevelopMethodsAndTasksStep;

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
