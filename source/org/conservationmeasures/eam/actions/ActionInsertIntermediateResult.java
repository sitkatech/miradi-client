/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.IntermediateResultIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

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
