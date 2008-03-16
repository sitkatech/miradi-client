/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionEditIndicatorRelevancyList extends ObjectsAction
{
	public ActionEditIndicatorRelevancyList(MainWindow mainWindow)
	{
		super(mainWindow, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Choose...");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Choose which indicators are relevant to this objective");
	}
}
