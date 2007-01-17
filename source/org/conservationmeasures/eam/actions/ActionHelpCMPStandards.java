/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionHelpCMPStandards extends MainWindowAction
{
	public ActionHelpCMPStandards(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/cmpstandards16.png");
	}

	private static String getLabel()
	{
		return EAM.text("Action|CMP Open Standards");
	}

}
