/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.TextBoxIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertTextBox extends LocationAction
{
	public ActionInsertTextBox(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new TextBoxIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Insert Text Box");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Text Box");
	}
}
