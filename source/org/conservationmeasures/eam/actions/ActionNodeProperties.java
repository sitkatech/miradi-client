/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionNodeProperties extends MainWindowAction
{
	public ActionNodeProperties(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/edit.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Properties...");
	}

}
