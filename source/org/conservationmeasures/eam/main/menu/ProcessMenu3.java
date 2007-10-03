/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAM;

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
