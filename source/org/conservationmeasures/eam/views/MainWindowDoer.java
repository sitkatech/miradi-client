/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import org.conservationmeasures.eam.main.MainWindow;

abstract public class MainWindowDoer extends Doer
{
	public MainWindowDoer(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}

	private MainWindow mainWindow;
}
