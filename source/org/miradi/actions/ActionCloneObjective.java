/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.ObjectiveIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionCloneObjective extends ObjectsAction
{
	public ActionCloneObjective(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), new ObjectiveIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Clone Objective...");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Clone a Objective");
	}

}
