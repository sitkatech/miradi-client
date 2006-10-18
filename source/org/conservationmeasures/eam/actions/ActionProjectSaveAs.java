/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionProjectSaveAs extends MainWindowAction
{
	public ActionProjectSaveAs(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	public static String getLabel()
	{
		return EAM.text("Action|Save Project As...");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Save project as");
	}

}
