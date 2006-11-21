/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionNormalDiagramMode extends MainWindowAction
{
	public ActionNormalDiagramMode(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), getIconName());
	}
	
	private static String getIconName()
	{
		return "icons/fullmodel.gif";
	}

	private static String getLabel()
	{
		return EAM.text("Action|Normal Mode");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Return to normal mode");
	}
}
