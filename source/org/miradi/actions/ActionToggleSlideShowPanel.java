/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.SlideShowIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionToggleSlideShowPanel extends ViewAction
{
	public ActionToggleSlideShowPanel(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new SlideShowIcon());
	}

	public static String getLabel()
	{
		return EAM.text("Action|Edit Slide Show");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Hides or shows the Slide Show editor");
	}

}
