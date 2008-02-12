/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import javax.swing.Icon;

import org.miradi.icons.ContributingFactorIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionInsertContributingFactor extends LocationAction
{
	public ActionInsertContributingFactor(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new ContributingFactorIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Insert Contributing Factor");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Contributing Factor");
	}
	
	public Icon getDisabledIcon()
	{
		return ContributingFactorIcon.createDisabledIcon();
	}

}

