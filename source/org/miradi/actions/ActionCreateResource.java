/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.ProjectResourceIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionCreateResource extends ObjectsAction
{
	public ActionCreateResource(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), new ProjectResourceIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Create Resource");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Create a Resource (person, team, etc)");
	}

}
