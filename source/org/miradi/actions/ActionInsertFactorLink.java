/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.FactorLinkIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionInsertFactorLink extends ViewAction
{
	public ActionInsertFactorLink(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new FactorLinkIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Insert Link...");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Add a link between two factors");
	}

}

