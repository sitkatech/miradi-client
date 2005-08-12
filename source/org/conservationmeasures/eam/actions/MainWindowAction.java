/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public abstract class MainWindowAction extends EAMAction
{
	public MainWindowAction(MainWindow mainWindowToUse, String label)
	{
		super(label, "icons/blankicon.png");
		mainWindow = mainWindowToUse;
	}
	
	public MainWindowAction(MainWindow mainWindowToUse, String label, String icon)
	{
		super(label, icon);
		mainWindow = mainWindowToUse;
	}
	
	public MainWindowAction(MainWindow mainWindowToUse, String label, Icon icon)
	{
		super(label, icon);
		mainWindow = mainWindowToUse;
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public BaseProject getProject()
	{
		return mainWindow.getProject();
	}

	public boolean shouldBeEnabled()
	{
		return shouldBeEnabled(mainWindow.getCurrentView());
	}

	public boolean shouldBeEnabled(UmbrellaView view)
	{
		return false;
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		doAction(mainWindow.getCurrentView(), event);
	}
	
	public void doAction(UmbrellaView view, ActionEvent event) throws CommandFailedException
	{
		EAM.logError("doAction(view, event) not implemented by: " + getClass().getName());
	}
	
	MainWindow mainWindow;
}
