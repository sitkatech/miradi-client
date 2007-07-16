/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionCreateOrShowResultsChain extends ViewAction
{
	public ActionCreateOrShowResultsChain(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), getIconName());
	}
	
	//TODO vertify that this image is used for this action
	public static String getIconName()
	{
		return "icons/showResultsChain.png";
	}

	//TODO verify text for label and tooltip
	private static String getLabel()
	{
		return EAM.text("Action|Show/Create Results Chain");
	}
	
	public String getToolTipText()
	{
		return EAM.text("Action|Show/Create Results Chain");
	}
}
