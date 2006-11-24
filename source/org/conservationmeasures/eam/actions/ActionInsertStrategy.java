/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.InterventionIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertStrategy extends LocationAction
{
	public ActionInsertStrategy(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new InterventionIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Strategy");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Strategy");
	}

}

