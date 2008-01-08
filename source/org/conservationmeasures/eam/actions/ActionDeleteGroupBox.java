/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.GroupBoxIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionDeleteGroupBox extends LocationAction
{
	public ActionDeleteGroupBox(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), new GroupBoxIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Delet Group Box(s)");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Delete Group Box(s) but not children");
	}
}
