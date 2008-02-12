/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionShowCellRatings extends MainWindowAction
{
	public ActionShowCellRatings(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/showRatings.png");
	}
	
	public static String getLabel()
	{
		return EAM.text("Action|Show Ratings in Cell");
	}
	
	public String getToolTipText()
	{
		return getLabel();
	}
}
