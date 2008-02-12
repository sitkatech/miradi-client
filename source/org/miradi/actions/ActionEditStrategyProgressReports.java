/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionEditStrategyProgressReports extends ObjectsAction
{
	public ActionEditStrategyProgressReports(MainWindow mainWindow)
	{
		super(mainWindow, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Edit Progress Reports...");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Edit the list of progress reports for this strategy");
	}

}
