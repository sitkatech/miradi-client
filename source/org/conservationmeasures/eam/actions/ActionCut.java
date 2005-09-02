/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionCut extends MainWindowAction
{
	public ActionCut(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/cut.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Cut");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Cut the selection to the clipboard");
	}
	
}
