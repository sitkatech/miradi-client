/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.IntermediateResultIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionInsertIntermediateResult extends LocationAction
{
	public ActionInsertIntermediateResult(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new IntermediateResultIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Insert Intermediate Result");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Intermediate Result");
	}
}
