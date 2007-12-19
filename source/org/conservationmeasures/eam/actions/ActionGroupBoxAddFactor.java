/* 
 * Copyright 2005-2007, Wildlife Conservation Society, 
 * Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
 * Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
 */ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.GroupBoxIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionGroupBoxAddFactor extends LocationAction
{
	public ActionGroupBoxAddFactor(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new GroupBoxIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Add Factor(s) in Group Box");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Add Factor(s) into Group Box");
	}
}
