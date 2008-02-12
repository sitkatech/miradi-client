/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.ProgressReportIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionCreateStrategyProgressReport extends ObjectsAction
{
	public ActionCreateStrategyProgressReport(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), new ProgressReportIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Create Progress Report");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Create a new Progress Report");
	}
}
