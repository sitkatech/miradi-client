/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.DeleteIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionDeleteGroupBox extends LocationAction
{
	public ActionDeleteGroupBox(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), new DeleteIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Delet Group Box(s)");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Delete Group Box(s) but not children");
	}
}
