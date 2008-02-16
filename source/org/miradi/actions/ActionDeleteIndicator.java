/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.DeleteIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionDeleteIndicator extends ObjectsAction
{
	public ActionDeleteIndicator(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(),  new DeleteIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Delete Indicator");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Delete the selected Indicator");
	}

}
