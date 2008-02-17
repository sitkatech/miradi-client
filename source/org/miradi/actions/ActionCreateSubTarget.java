/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.SubTargetIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionCreateSubTarget extends ObjectsAction
{
	public ActionCreateSubTarget(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), new SubTargetIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Create Nested Target");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Create a Nested Target");
	}
}
