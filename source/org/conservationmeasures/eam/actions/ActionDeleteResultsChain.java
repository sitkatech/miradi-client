/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionDeleteResultsChain extends MainWindowAction
{
	public ActionDeleteResultsChain(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/delete.gif");
	}
	
	private static String getLabel()
	{
		return EAM.text("Action|Delete Results Chain");
	}
}
