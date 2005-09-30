/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.InsertFactorIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertFactor extends LocationAction
{
	public ActionInsertFactor(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new InsertFactorIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Factor");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Factor node");
	}

}

