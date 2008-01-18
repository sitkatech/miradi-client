/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.GroupBoxIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionGroupBoxRemoveFactor extends LocationAction
{
	public ActionGroupBoxRemoveFactor(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new GroupBoxIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Remove|Remove Factor(s) from Group Box");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Remove Factor(s) from Group Box");
	}
}
