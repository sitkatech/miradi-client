/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionCopy extends ViewAction
{
	public ActionCopy(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/copy.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Copy");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Copy the selection to the clipboard");
	}

}
