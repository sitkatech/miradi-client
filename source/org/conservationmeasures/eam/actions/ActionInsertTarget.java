/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import javax.swing.Icon;

import org.conservationmeasures.eam.icons.TargetIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

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

