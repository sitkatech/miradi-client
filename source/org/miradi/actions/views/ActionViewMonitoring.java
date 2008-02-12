/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions.views;

import org.miradi.actions.MainWindowAction;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionViewMonitoring extends MainWindowAction
{
	public ActionViewMonitoring(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Monitoring Plan");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Switch to the Monitoring Plan View");
	}
	
	public String toString()
	{
		return getLabel();
	}
}
