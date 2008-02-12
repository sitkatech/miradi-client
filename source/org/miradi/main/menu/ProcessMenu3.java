/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.main.menu;

import java.awt.event.KeyEvent;

import org.miradi.actions.Actions;
import org.miradi.main.EAM;

public class ProcessMenu3 extends MiradiMenu
{
	public ProcessMenu3(Actions actionsToUse)
	{
		super(EAM.text("3. Implement Actions &  Monitoring"), actionsToUse);
		setMnemonic(KeyEvent.VK_M);
		
		add(new ProcessMenu3a(actions));
		add(new ProcessMenu3b(actions));
		add(new ProcessMenu3c(actions));
	}

}
