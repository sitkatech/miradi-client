/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.InsertGoalIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertGoal extends LocationAction
{
	public ActionInsertGoal(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new InsertGoalIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Goal");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Goal node");
	}

}

