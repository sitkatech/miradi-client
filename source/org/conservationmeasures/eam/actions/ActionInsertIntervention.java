/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.InterventionIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertIntervention extends LocationAction
{
	public ActionInsertIntervention(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new InterventionIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Intervention");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert an Intervention");
	}

}

