/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionShowFullModelMode extends ViewAction
{
	public ActionShowFullModelMode(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), getIconName());
	}
	
	private static String getIconName()
	{
		return "icons/fullmodel.gif";
	}

	private static String getLabel()
	{
		return EAM.text("Action|Show Full Diagram");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Show the full conceptual model page");
	}
}
