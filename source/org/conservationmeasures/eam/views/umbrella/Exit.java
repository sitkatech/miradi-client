/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.MainWindowDoer;

public class Exit extends MainWindowDoer
{
	public Exit(MainWindow mainWindow)
	{
		super(mainWindow);
	}
	
	public void doIt()
	{
		getMainWindow().exitNormally();
	}
	
	public boolean isAvailable()
	{
		return true;
	}
}
