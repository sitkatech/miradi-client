/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionTreeCreateTask extends ObjectsAction
{
	public ActionTreeCreateTask(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Create Task");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Create a Task or Subtask for the selected Item");
	}
}
