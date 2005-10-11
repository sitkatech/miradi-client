/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.InsertStressIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertStress extends LocationAction
{
	public ActionInsertStress(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new InsertStressIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Stress");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Stress node");
	}

}

