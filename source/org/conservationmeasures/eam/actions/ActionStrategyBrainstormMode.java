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
		super(mainWindow, getLabel(), getIconName());
	}

	private static String getIconName()
	{
		return "icons/chain.gif";
	}
	
	private static String getLabel()
	{
		return EAM.text("Action|Show Chain");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Enter the mode that shows chain of strategies");
	}

}
