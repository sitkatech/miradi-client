/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAM;

public class ProcessMenu1 extends MiradiMenu
{
	public ProcessMenu1(Actions actions)
	{
		super(EAM.text("1. Conceptualize Project"), actions);
		setMnemonic(KeyEvent.VK_C);
		
		add(new ProcessMenu1a(actions));
		add(new ProcessMenu1b(actions));
		add(new ProcessMenu1c(actions));
		add(new ProcessMenu1d(actions));
	}
}
