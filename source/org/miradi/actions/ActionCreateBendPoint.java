/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionCreateBendPoint extends LocationAction
{
	public ActionCreateBendPoint(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/bendpt16.png");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Create Bend Point");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Create a Bend Point");
	}

}
