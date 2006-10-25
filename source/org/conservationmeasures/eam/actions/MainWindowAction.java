/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.Doer;
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
	
	public Project getProject()
	{
		return mainWindow.getProject();
	}

	public void actionPerformed(ActionEvent event)
	{
		try
		{
			doAction(event);
		}
		catch (CommandFailedException e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("An internal error prevented this operation"));
		}
		
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		doAction();
	}

	public void doAction() throws CommandFailedException
	{
		getDoer().doIt();
		getMainWindow().getActions().updateActionStates();
	}
	
	public boolean shouldBeEnabled()
	{
		Doer doer = getDoer();
		if(doer == null)
			return false;
		return doer.isAvailable();
	}
	
	Doer getDoer()
	{
		UmbrellaView currentView = mainWindow.getCurrentView();
		if(currentView == null)
			return null;
		
		Doer doer = currentView.getDoer(getClass());
		doer.setMainWindow(mainWindow);
		doer.setProject(mainWindow.getProject());
		return doer;
	}
	
	MainWindow mainWindow;
}
