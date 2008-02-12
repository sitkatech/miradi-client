/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions.views;

import org.miradi.actions.MainWindowAction;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionViewStrategicPlan extends MainWindowAction
{
	public ActionViewStrategicPlan(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Strategic Plan");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Switch to the Strategic Plan View");
	}
	
	public String toString()
	{
		return getLabel();
	}
}
