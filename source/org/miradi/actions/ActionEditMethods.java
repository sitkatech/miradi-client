/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionEditMethods extends ObjectsAction
{
	public ActionEditMethods(MainWindow mainWindow)
	{
		super(mainWindow, getLabel());
	}

	public static String getLabel()
	{
		return EAM.text("Action|Edit Methods...");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Edit the methods for this indicator");
	}
}
