/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionDeletePlanningViewTreeNode extends ObjectsAction
{
	public ActionDeletePlanningViewTreeNode(MainWindow mainWindow)
	{
		super(mainWindow, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Tree|Delete Item");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Delete the selected item");
	}

}
