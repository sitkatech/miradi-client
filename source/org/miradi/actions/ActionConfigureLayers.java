/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionConfigureLayers extends MainWindowAction
{
	public ActionConfigureLayers(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "images/layers.png");
	}

	private static String getLabel()
	{
		return EAM.text("Action|View|Layer...");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Hide or show portions of the diagram");
	}
}
