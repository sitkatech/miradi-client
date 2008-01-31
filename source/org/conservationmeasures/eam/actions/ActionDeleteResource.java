/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionDeleteResource extends ObjectsAction
{
	public ActionDeleteResource(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/delete.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Delete Resource");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Delete the selected Resource (person, team, etc)");
	}

}
