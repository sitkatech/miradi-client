/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionHelpComingAttractions extends MainWindowAction
{
	public ActionHelpComingAttractions(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/coming attract16.png");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Coming Attractions");
	}

}
