/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionModifyResource extends ObjectsAction
{
	public ActionModifyResource(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Modify Member ...");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Modify the selected member");
	}
	

}
