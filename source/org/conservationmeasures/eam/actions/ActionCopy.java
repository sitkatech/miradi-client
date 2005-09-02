/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionCopy extends MainWindowAction
{
	public ActionCopy(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/copy.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Copy");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Copy the selection to the clipboard");
	}

}
