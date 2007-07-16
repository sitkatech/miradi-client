/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionAbout extends MainWindowAction
{
	public ActionAbout(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "images/miradi16.png");
	}

	private static String getLabel()
	{
		return EAM.text("Action|About Miradi");
	}

}
