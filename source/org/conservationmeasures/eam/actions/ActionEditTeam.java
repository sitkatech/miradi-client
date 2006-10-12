/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionEditTeam extends ViewAction
{
	public ActionEditTeam(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Edit Team");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Add, Remove, or Modify Team Members");
	}

}
