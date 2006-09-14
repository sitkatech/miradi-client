/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions.jump;

import org.conservationmeasures.eam.actions.JumpAction;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionJumpDetermineNeeds extends JumpAction
{
	public ActionJumpDetermineNeeds(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}
	
	static String getLabel()
	{
		return EAM.text("Determine what you need to know");
	}
	
}
