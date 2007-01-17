/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions.jump;

import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionJumpDefineIndicators extends MainWindowAction
{
	public ActionJumpDefineIndicators(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}
	
	private static String getLabel()
	{
		return EAM.text("Define Indicators"); 
	}

}
