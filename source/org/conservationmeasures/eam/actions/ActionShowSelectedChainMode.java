/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionShowSelectedChainMode extends ViewAction
{
	public ActionShowSelectedChainMode(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), getIconName());
	}

	private static String getIconName()
	{
		return "icons/chain.gif";
	}
	
	private static String getLabel()
	{
		return EAM.text("Action|Brainstorm Mode");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Isolate the selected factor chain in Brainstorm Mode");
	}

}
