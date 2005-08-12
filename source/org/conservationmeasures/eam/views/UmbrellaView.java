/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.About;
import org.conservationmeasures.eam.views.umbrella.Exit;
import org.conservationmeasures.eam.views.umbrella.NewProject;
import org.conservationmeasures.eam.views.umbrella.OpenProject;

abstract public class UmbrellaView extends JPanel
{
	public UmbrellaView(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
	}
	
	abstract public String cardName();
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public BaseProject getProject()
	{
		return getMainWindow().getProject();
	}
	
	public Actions getActions()
	{
		return getMainWindow().getActions();
	}
	
	public void doAbout() throws CommandFailedException
	{
		About.doAbout();
	}
	
	public void doNewProject() throws CommandFailedException
	{
		NewProject.doNewProject(getMainWindow());
	}
	
	public void doOpenProject() throws CommandFailedException
	{
		OpenProject.doOpenProject(getMainWindow());
	}
	
	public void doExit() throws CommandFailedException
	{
		Exit.doExit(getMainWindow());
	}
	
	private MainWindow mainWindow;
}
