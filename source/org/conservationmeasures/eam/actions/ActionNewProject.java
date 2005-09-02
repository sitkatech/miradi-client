/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionNewProject extends MainWindowAction
{
	public ActionNewProject(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/new.gif");
	}

	public static String getLabel()
	{
		return EAM.text("Action|New Project");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Create a new project");
	}

}
