/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.DirectThreatIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertDirectThreat extends LocationAction
{
	public ActionInsertDirectThreat(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new DirectThreatIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Direct Threat");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Direct Threat");
	}

}

