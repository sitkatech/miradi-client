/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpCreate;
import org.conservationmeasures.eam.actions.jump.ActionJumpDocument;
import org.conservationmeasures.eam.actions.jump.ActionJumpShare;
import org.conservationmeasures.eam.main.EAM;

public class ProcessMenu7 extends MiradiMenu
{
	public ProcessMenu7(Actions actions)
	{
		super(EAM.text("7. Capture and Share Learning"), actions);
		setMnemonic(KeyEvent.VK_C);
		
		addMenuItem(ActionJumpDocument.class, KeyEvent.VK_D);
		addMenuItem(ActionJumpShare.class, KeyEvent.VK_S);
		addMenuItem(ActionJumpCreate.class, KeyEvent.VK_C);
	}
}
