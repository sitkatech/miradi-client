/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.InsertThreatIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertThreat extends LocationAction
{
	public ActionInsertThreat(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new InsertThreatIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Threat");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Threat node");
	}

}

