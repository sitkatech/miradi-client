/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionEditIndicatorProgressReports extends ObjectsAction
{
	public ActionEditIndicatorProgressReports(MainWindow mainWindow)
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
