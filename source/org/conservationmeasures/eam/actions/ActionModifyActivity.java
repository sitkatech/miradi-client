/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionModifyActivity extends ObjectsAction
{
	public ActionModifyActivity(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Edit Activity");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Edit the selected activity");
	}
	

}
