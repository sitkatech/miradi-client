/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionPrint extends MainWindowAction 
{
	public ActionPrint(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/print.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Print");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Print the current view to a printer");
	}
	
}
