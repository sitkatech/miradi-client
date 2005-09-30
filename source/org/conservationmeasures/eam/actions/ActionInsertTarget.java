/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.InsertTargetIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertTarget extends LocationAction
{
	public ActionInsertTarget(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new InsertTargetIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Target");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Target node");
	}

}

