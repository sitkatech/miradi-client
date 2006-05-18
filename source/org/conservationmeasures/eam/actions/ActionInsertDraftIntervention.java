/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.DraftInterventionIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertDraftIntervention extends LocationAction
{
	public ActionInsertDraftIntervention(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new DraftInterventionIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Draft Intervention");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a draft Intervention");
	}


}
