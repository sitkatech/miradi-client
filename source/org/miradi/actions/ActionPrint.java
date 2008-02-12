/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionPrint extends MainWindowAction 
{
	public ActionPrint(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/print.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Print");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Print the current view to a printer");
	}
	
}
