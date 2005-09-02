/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionDelete extends MainWindowAction
{
	public ActionDelete(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/delete.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Delete");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Delete the selection");
	}

}
