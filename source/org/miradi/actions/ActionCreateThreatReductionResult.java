/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import javax.swing.Icon;

import org.miradi.icons.ThreatReductionResultIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionCreateThreatReductionResult extends LocationAction
{
	public ActionCreateThreatReductionResult(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new ThreatReductionResultIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Insert Threat Reduction Result");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Threat Reduction Result");
	}

	public Icon getDisabledIcon()
	{
		return ThreatReductionResultIcon.createDisabledIcon();
	}

}
