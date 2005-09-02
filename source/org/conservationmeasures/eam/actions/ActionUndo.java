/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionUndo extends MainWindowAction
{
	public ActionUndo(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/undo.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Undo");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Undo last action");
	}

}
