/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;

abstract public class MainWindowDoer extends Doer
{
	public void setMainWindow(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public Project getProject()
	{
		return getMainWindow().getProject();
	}

	private MainWindow mainWindow;
}
