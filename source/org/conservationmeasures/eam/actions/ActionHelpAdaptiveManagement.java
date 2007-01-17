/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionHelpAdaptiveManagement extends MainWindowAction
{
	public ActionHelpAdaptiveManagement(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/AM16.png");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Adaptive Management");
	}

}
