/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanDevelopGoalStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanDevelopObjectivesStep;

public class ProcessMenu2a extends MiradiMenu
{
	public ProcessMenu2a(Actions actions)
	{
		super(ProcessSteps.PROCESS_STEP_2A, actions);
		setMnemonic(KeyEvent.VK_D);
		
		addMenuItem(ActionJumpStrategicPlanDevelopGoalStep.class, KeyEvent.VK_G);
		addMenuItem(ActionJumpStrategicPlanDevelopObjectivesStep.class, KeyEvent.VK_O);
	}
}
