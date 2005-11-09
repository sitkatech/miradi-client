/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionZoomIn extends MainWindowAction
{
	public ActionZoomIn(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/zoomin.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Zoom In");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Increase magnification");
	}
}
