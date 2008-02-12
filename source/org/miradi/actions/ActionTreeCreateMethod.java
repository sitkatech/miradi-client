/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.MethodIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionTreeCreateMethod extends ObjectsAction
{
	public ActionTreeCreateMethod(MainWindow mainWindowToUse)
	{
		this(mainWindowToUse, getLabel());
	}

	public ActionTreeCreateMethod(MainWindow mainWindowToUse, String label)
	{
		super(mainWindowToUse, label, new MethodIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Create Method");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Create a Method for the selected Indicator");
	}
}
