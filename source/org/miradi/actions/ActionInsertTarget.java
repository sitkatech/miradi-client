/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import javax.swing.Icon;

import org.miradi.icons.TargetIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionInsertTarget extends LocationAction
{
	public ActionInsertTarget(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new TargetIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Insert Target");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Target");
	}

	public Icon getDisabledIcon()
	{
		return TargetIcon.createDisabledIcon();
	}
}

