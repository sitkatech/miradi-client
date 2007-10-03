/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringWizardDefineIndicatorsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpPlanDataStorage;

public class ProcessMenu3b extends MiradiMenu
{
	public ProcessMenu3b(Actions actions)
	{
		super(ProcessSteps.PROCESS_STEP_3B, actions);
		setMnemonic(KeyEvent.VK_D);
		
	}
}
