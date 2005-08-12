/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.UmbrellaView;

public class ActionExit extends MainWindowAction
{
	public ActionExit(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/blankicon.png");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Exit");
	}

	public void doAction(UmbrellaView view, ActionEvent event) throws CommandFailedException
	{
		view.doExit(mainWindow);
	}

	public boolean shouldBeEnabled()
	{
		return true;
	}
}
