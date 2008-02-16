/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.ActivityIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionCreateActivity extends MainWindowAction
{
	public ActionCreateActivity(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), new ActivityIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Create Activity");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Create a new Activity");
	}
}
