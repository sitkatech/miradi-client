/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.MainWindow;

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

	MainWindow mainWindow;
}
