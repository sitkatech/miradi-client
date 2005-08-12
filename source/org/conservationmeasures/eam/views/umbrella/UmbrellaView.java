/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.MainWindow;

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
	
	public About getAboutDoer()
	{
		return new About();
	}
	
	public NewProject getNewProjectDoer()
	{
		return new NewProject(getMainWindow());
	}
	
	public OpenProject getOpenProjectDoer()
	{
		return new OpenProject(getMainWindow());
	}
	
	public Exit getExitDoer()
	{
		return new Exit(getMainWindow());
	}
	
	private MainWindow mainWindow;
}
