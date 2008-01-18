/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.ProgressReportIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

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
