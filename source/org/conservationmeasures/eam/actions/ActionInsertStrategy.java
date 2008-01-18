/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import javax.swing.Icon;

import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertStrategy extends LocationAction
{
	public ActionInsertStrategy(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new StrategyIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Insert Strategy");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Strategy");
	}
	
	public Icon getDisabledIcon()
	{
		return StrategyIcon.createDisabledIcon();
	}
}

