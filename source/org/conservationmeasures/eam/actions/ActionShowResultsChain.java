/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.ResultsChainIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;


public class ActionShowResultsChain extends ViewAction
{
	public ActionShowResultsChain(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new ResultsChainIcon());
	}
	
	private static String getLabel()
	{
		return EAM.text("Action|Show Results Chain");
	}
	
	public String getToolTipText()
	{
		return EAM.text("Action|Show Results Chain");
	}
}
