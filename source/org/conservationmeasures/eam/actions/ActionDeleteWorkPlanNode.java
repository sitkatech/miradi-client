/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionDeleteWorkPlanNode extends ObjectsAction
{
	public ActionDeleteWorkPlanNode(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}
	
	private static String getLabel()
	{
		return EAM.text("Action|Delete");
	}

	public String getToolTipText()
	{
		//TODO come up with a better name instead of 'object'
		return EAM.text("TT|Delete the selected object");
	}
}
