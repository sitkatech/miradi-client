/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.icons.InsertGoalIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class ActionInsertGoal extends LocationAction
{
	public ActionInsertGoal(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new InsertGoalIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Goal");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Goal node");
	}

	public void doAction(UmbrellaView view, ActionEvent event) throws CommandFailedException
	{
		view.getInsertGoalDoer(createAt).doIt();
	}

	public boolean shouldBeEnabled(UmbrellaView view)
	{
		return view.getInsertGoalDoer(createAt).isAvailable();
	}
}

