/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionTeamAddMember extends ObjectsAction
{
	public ActionTeamAddMember(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Add to Team");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Add the selected person to the team");
	}
	
}
