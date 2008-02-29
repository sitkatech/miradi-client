/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.PlanningIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionCreatePlanningViewConfiguration extends MainWindowAction
{
	public ActionCreatePlanningViewConfiguration(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), new PlanningIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Create Custom");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Create a new Custom Planning View");
	}
}
