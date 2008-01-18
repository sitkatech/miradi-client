/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import javax.swing.Icon;

import org.conservationmeasures.eam.icons.ContributingFactorIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

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

