/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionZoomOut extends MainWindowAction
{
	public ActionZoomOut(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/zoomout.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Zoom Out");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Decrease magnification");
	}
}
