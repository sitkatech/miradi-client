/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAssessResources;
import org.conservationmeasures.eam.actions.jump.ActionJumpAssessRisks;
import org.conservationmeasures.eam.actions.jump.ActionJumpPlanProjectLifespan;

public class ProcessMenu2c extends MiradiMenu
{
	public ProcessMenu2c(Actions actions)
	{
		super(ProcessSteps.PROCESS_STEP_2C, actions);
		setMnemonic(KeyEvent.VK_O);
		
		addMenuItem(ActionJumpAssessResources.class, KeyEvent.VK_A);
		addMenuItem(ActionJumpAssessRisks.class, KeyEvent.VK_R);
		addMenuItem(ActionJumpPlanProjectLifespan.class, KeyEvent.VK_P);
	}
}
