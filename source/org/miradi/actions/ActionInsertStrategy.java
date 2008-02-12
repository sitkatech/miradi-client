/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import javax.swing.Icon;

import org.miradi.icons.StrategyIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

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

