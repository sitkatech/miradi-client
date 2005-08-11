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

public class ActionExit extends ProjectAction
{
	public ActionExit(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse.getProject(), getLabel());
		mainWindow = mainWindowToUse;
	}

	private static String getLabel()
	{
		return EAM.text("Action|Exit");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		mainWindow.exitNormally();
	}

	public boolean shouldBeEnabled()
	{
		return true;
	}
	
	MainWindow mainWindow;
}
