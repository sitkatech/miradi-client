/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions.jump;

import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionJumpWorkPlanOverview extends MainWindowAction
{
	public ActionJumpWorkPlanOverview(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}
	
	static String getLabel()
	{
		return EAM.text("Work plan overview");
	}
}
