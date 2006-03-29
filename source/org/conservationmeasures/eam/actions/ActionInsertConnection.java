/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.ConnectionIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertConnection extends MainWindowAction
{
	public ActionInsertConnection(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new ConnectionIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Connection...");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Add a relationship between two factors");
	}

}

