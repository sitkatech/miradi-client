/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpImplementStrategicAndMonitoringPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpImplementWorkPlan;

public class ProcessMenu3c extends MiradiMenu
{
	public ProcessMenu3c(Actions actions)
	{
		super(ProcessSteps.PROCESS_STEP_3C, actions);
		setMnemonic(KeyEvent.VK_I);
		
		addMenuItem(ActionJumpImplementStrategicAndMonitoringPlans.class, KeyEvent.VK_S);
		addMenuItem(ActionJumpImplementWorkPlan.class, KeyEvent.VK_W);
	}
}
