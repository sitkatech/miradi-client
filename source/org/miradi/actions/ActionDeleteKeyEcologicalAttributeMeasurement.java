/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.actions;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionDeleteKeyEcologicalAttributeMeasurement extends ObjectsAction
{
	public ActionDeleteKeyEcologicalAttributeMeasurement(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/delete.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Delete Measurement");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Delete a Measurement");
	}
}
