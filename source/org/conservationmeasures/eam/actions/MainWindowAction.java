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

public abstract class MainWindowAction extends ProjectAction
{
	public MainWindowAction(MainWindow mainWindowToUse, String label)
	{
		super(mainWindowToUse.getProject(), label);
		mainWindow = mainWindowToUse;
	}
	public MainWindowAction(MainWindow mainWindowToUse, String label, String icon)
	{
		super(mainWindowToUse.getProject(), label, icon);
		mainWindow = mainWindowToUse;
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		UmbrellaView view = mainWindow.getCurrentView();
		doAction(view, event);
	}
	
	public void doAction(UmbrellaView view, ActionEvent event) throws CommandFailedException
	{
		EAM.logError("doAction(view, event) not implemented by: " + getClass().getName());
	}
	
	MainWindow mainWindow;
}
