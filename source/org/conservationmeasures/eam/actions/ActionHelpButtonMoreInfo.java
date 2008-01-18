/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionHelpButtonMoreInfo extends MainWindowAction
{
	public ActionHelpButtonMoreInfo(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(),"images/info.png");
	}

	private static String getLabel()
	{
		return EAM.text("Action|More Info");
	}

}