/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionTreeNodeDown extends ViewAction
{
	public ActionTreeNodeDown(MainWindow mainWindow)
	{
		super(mainWindow, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Tree|Move Down");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Move the selected item down");
	}
}
