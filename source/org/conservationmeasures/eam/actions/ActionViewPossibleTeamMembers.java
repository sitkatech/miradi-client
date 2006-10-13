/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionViewPossibleTeamMembers extends ViewAction
{
	public ActionViewPossibleTeamMembers(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Add Member");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Add a member to the team");
	}
	
}
