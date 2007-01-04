/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions.jump;

import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionJumpEditIndicators extends MainWindowAction
{
	public ActionJumpEditIndicators(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	static String getLabel()
	{
		return EAM.text("Edit Indicators");
	}
	
}
