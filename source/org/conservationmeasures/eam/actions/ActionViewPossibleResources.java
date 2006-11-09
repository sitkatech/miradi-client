/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionViewPossibleResources extends ObjectsAction
{
	public ActionViewPossibleResources(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}
	
	private static String getLabel()
	{
		return EAM.text("Action|Manage|Add Resources...");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Add resources to activity");
	}
}
