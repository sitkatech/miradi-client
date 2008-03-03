/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.CreateResultsChainIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionCreateOrShowResultsChain extends ViewAction
{
	public ActionCreateOrShowResultsChain(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new CreateResultsChainIcon());
	}
	
	private static String getLabel()
	{
		return EAM.text("Action|Show/Create Results Chain");
	}
	
	public String getToolTipText()
	{
		return EAM.text("Action|Show/Create Results Chain");
	}
}
