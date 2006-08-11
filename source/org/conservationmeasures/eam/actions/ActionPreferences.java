/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionPreferences extends MainWindowAction
{

	public ActionPreferences(MainWindow mainWindow)
	{
		super(mainWindow, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Preferences");
	}

}
