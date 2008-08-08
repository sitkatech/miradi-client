/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.utils;

import org.miradi.main.MainWindow;

public class TableRowHeightController
{
	public TableRowHeightController(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;

		isAutomaticRowHeightsEnabled = true;
	}

	public void disable()
	{
		isAutomaticRowHeightsEnabled = false;
	}

	public MainWindow getMainWindow()
	{
		return mainWindow;
	}

	protected boolean isAutomaticRowHeightsEnabled()
	{
		if(!isAutomaticRowHeightsEnabled)
			return false;
		if(!getMainWindow().isRowHeightModeAutomatic())
			return false;
	
		return true;
	}

	private MainWindow mainWindow;
	private boolean isAutomaticRowHeightsEnabled;
}
