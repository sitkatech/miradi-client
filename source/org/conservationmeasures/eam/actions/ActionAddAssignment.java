/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionAddAssignment extends ObjectsAction
{
	public ActionAddAssignment(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}
	
	public static String getLabel()
	{
		return EAM.text("Action|Add Item");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Add item to list");
	}
}
