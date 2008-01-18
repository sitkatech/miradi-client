/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.CreateResultsChainIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionCreateResultsChain extends MainWindowAction
{
	public ActionCreateResultsChain(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new CreateResultsChainIcon());
	}
	
	private static String getLabel()
	{
		return EAM.text("Action|Create Results Chain");
	}
}
