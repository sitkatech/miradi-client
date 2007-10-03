/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeData;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpCommunicateResults;
import org.conservationmeasures.eam.main.EAM;

public class ProcessMenu5 extends MiradiMenu
{
	public ProcessMenu5(Actions actions)
	{
		super(EAM.text("5. Analyze"), actions);
		setMnemonic(KeyEvent.VK_A);
		
		addMenuItem(ActionJumpAnalyzeData.class, KeyEvent.VK_A);
		addMenuItem(ActionJumpAnalyzeStrategies.class, KeyEvent.VK_S);
		addMenuItem(ActionJumpCommunicateResults.class, KeyEvent.VK_C);
	}
}
