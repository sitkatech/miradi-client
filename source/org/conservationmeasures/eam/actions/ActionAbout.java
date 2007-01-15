/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionAbout extends MainWindowAction
{
	public ActionAbout(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "images/miradi16.png");
	}

	private static String getLabel()
	{
		return EAM.text("Action|About Miradi");
	}

}
