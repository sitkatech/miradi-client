/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.InsertIndirectFactorIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertIndirectFactor extends LocationAction
{
	public ActionInsertIndirectFactor(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new InsertIndirectFactorIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Indirect Factor");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Indirect Factor node");
	}

}

