/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.ConnectionIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertFactorLink extends MainWindowAction
{
	public ActionInsertFactorLink(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new ConnectionIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Link...");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Add a link between two factors");
	}

}

