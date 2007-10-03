/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpImplementWorkPlan;
import org.conservationmeasures.eam.actions.jump.ActionJumpRefinePlans;
import org.conservationmeasures.eam.main.EAM;

public class ProcessMenu4 extends MiradiMenu
{
	public ProcessMenu4(Actions actions)
	{
		super(EAM.text("4. Implement Actions and Monitoring"), actions);
		setMnemonic(KeyEvent.VK_I);
		
		add(new ProcessMenu4a(actions));
		addMenuItem(ActionJumpImplementWorkPlan.class, KeyEvent.VK_I);
		addMenuItem(ActionJumpRefinePlans.class, KeyEvent.VK_R);

	}
}
