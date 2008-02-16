/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.GoalIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionCloneGoal extends ObjectsAction
{
	public ActionCloneGoal(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), new GoalIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Clone Goal...");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Clone a Goal");
	}

}
