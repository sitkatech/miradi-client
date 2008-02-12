/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.TaskIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionTreeCreateTask extends ObjectsAction
{
	public ActionTreeCreateTask(MainWindow mainWindowToUse)
	{
		this(mainWindowToUse, getLabel());
	}

	public ActionTreeCreateTask(MainWindow mainWindowToUse, String labelToUse)
	{
		super(mainWindowToUse, labelToUse, new TaskIcon());
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
