/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionDeleteBendPoint extends LocationAction
{
	public ActionDeleteBendPoint(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/bendpt-no16.png");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Delete Bend Point");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Delete a Bend Point");
	}

}
