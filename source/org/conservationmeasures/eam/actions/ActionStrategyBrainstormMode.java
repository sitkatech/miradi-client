/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionStrategyBrainstormMode extends MainWindowAction
{
	public ActionStrategyBrainstormMode(MainWindow mainWindow)
	{
		super(mainWindow, getLabel());

	}

	private static String getLabel()
	{
		return EAM.text("Action|Strategy Brainstorm Mode");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Enter the mode that allows brainstorming of strategies");
	}

}
