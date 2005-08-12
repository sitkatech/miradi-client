/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.icons.InsertInterventionIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class ActionInsertIntervention extends LocationAction
{
	public ActionInsertIntervention(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new InsertInterventionIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Intervention");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert an Intervention node");
	}

	public void doAction(UmbrellaView view, ActionEvent event) throws CommandFailedException
	{
		view.getInsertInterventionDoer(createAt).doIt();
	}

	public boolean shouldBeEnabled(UmbrellaView view)
	{
		return view.getInsertInterventionDoer(createAt).isAvailable();
	}
}

