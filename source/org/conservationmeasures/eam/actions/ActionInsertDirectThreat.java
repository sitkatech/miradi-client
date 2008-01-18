/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import javax.swing.Icon;

import org.conservationmeasures.eam.icons.DirectThreatIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertDirectThreat extends LocationAction
{
	public ActionInsertDirectThreat(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new DirectThreatIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Insert Direct Threat");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Direct Threat");
	}

	public Icon getDisabledIcon()
	{
		return DirectThreatIcon.createDisabledIcon();
	}
}

