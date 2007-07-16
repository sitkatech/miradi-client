/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionHelpAgileSoftware extends MainWindowAction
{
	public ActionHelpAgileSoftware(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/agile16.png");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Agile Software");
	}

}
