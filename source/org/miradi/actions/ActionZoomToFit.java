/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionZoomToFit extends ViewAction
{
	public ActionZoomToFit(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/zoomToFit.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Zoom To Fit");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Zoom in/out to fit everything on page");
	}
}
