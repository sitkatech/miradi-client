/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.IndirectFactorIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertContributingFactor extends LocationAction
{
	public ActionInsertContributingFactor(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new IndirectFactorIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Contributing Factor");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Contributing Factor");
	}

}

