/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.TextBoxIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

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
