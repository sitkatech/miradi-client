/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;


public class ActionShowResultsChain extends ViewAction
{
	public ActionShowResultsChain(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), getIconName());
	}
	
	public static String getIconName()
	{
		return "icons/showResultsChain.png";
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
