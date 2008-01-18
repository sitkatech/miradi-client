/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.SlideShowIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

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
