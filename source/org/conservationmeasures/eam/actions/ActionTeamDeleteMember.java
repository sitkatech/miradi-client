/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionTeamDeleteMember extends ObjectsAction
{
	public ActionTeamDeleteMember(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Delete Member");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Delete a member from the team");
	}

}
