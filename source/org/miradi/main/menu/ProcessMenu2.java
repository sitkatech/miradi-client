/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.main.menu;

import java.awt.event.KeyEvent;

import org.miradi.actions.Actions;
import org.miradi.main.EAM;

public class ProcessMenu2 extends MiradiMenu
{
	public ProcessMenu2(Actions actionsToUse)
	{
		super(EAM.text("2. Plan Actions and Monitoring"), actionsToUse);
		setMnemonic(KeyEvent.VK_A);
		
		add(new ProcessMenu2a(actions));
		add(new ProcessMenu2b(actions));
		add(new ProcessMenu2c(actions));
	}

}
