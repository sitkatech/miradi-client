/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAM;

public class ProcessMenu4 extends MiradiMenu
{
	public ProcessMenu4(Actions actions)
	{
		super(EAM.text("4. Analyze, Use, Adapt"), actions);
		setMnemonic(KeyEvent.VK_I);
		
		add(new ProcessMenu4a(actions));
		add(new ProcessMenu4b(actions));
		add(new ProcessMenu4c(actions));
//		addMenuItem(ActionJumpImplementWorkPlan.class, KeyEvent.VK_I);
	//	addMenuItem(ActionJumpRefinePlans.class, KeyEvent.VK_R);

	}
}
