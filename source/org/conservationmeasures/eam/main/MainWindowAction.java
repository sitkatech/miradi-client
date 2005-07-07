/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import javax.swing.AbstractAction;

public abstract class MainWindowAction extends AbstractAction
{
	public MainWindowAction(MainWindow mainWindowToUse, String label)
	{
		super(label);
		mainWindow = mainWindowToUse;
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	MainWindow mainWindow;
}