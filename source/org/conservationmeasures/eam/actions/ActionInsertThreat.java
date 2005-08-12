/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.icons.InsertThreatIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class ActionInsertThreat extends LocationAction
{
	public ActionInsertThreat(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new InsertThreatIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Threat");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Threat node");
	}

	public void doAction(UmbrellaView view, ActionEvent event) throws CommandFailedException
	{
		view.getInsertThreatDoer(createAt).doIt();
	}

	public boolean shouldBeEnabled(UmbrellaView view)
	{
		return view.getInsertThreatDoer(createAt).isAvailable();
	}
}

