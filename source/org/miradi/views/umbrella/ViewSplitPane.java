/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.umbrella;

import java.awt.Component;

import org.miradi.main.MainWindow;

public class ViewSplitPane extends PersistentVerticalSplitPane
{
	public ViewSplitPane(MainWindow mainWindow, Component topPanel, Component bottomPanel) 
	{
		super(mainWindow, mainWindow, MAIN_SPLITTER);

		setTopComponent(topPanel);
		setBottomComponent(bottomPanel);

		setOneTouchExpandable(true);
		setDividerSize(15);
		setResizeWeight(.5);
		setTopComponent(topPanel);
		setBottomComponent(bottomPanel);
		setFocusable(false);
		
		restoreSavedLocation();
	}
	
	static final String MAIN_SPLITTER = "MainSplitter";
}