/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAdaptAndMonitorPlans;
import org.conservationmeasures.eam.main.EAM;

public class ProcessMenu6 extends MiradiMenu
{
	public ProcessMenu6(Actions actions)
	{
		super(EAM.text("6. Use/Adapt"), actions);
		setMnemonic(KeyEvent.VK_U);
		
		addMenuItem(ActionJumpAdaptAndMonitorPlans.class, KeyEvent.VK_A);
	}
}
