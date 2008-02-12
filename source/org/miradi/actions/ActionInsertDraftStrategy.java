/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.DraftStrategyIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionInsertDraftStrategy extends LocationAction
{
	public ActionInsertDraftStrategy(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new DraftStrategyIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Insert Draft Strategy");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a draft Strategy");
	}


}
