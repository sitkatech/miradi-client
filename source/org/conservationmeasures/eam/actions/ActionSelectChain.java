/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionSelectChain extends MainWindowAction
{
	public ActionSelectChain(MainWindow mainWindow)
	{
		super(mainWindow, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Select Chain");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Select the entire chain related to this Direct Threat");
	}

}
