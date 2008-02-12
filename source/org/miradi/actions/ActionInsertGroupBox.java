/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.GroupBoxIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionInsertGroupBox extends LocationAction
{
	public ActionInsertGroupBox(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new GroupBoxIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Create Group Box");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Create a Group Box");
	}
}
