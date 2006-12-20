/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;


import java.awt.Container;

import javax.swing.JSplitPane;

import org.conservationmeasures.eam.main.MainWindow;

public class ViewSplitPane extends JSplitPane
{
		
	public ViewSplitPane(MainWindow mainWindowToUse, String splitterNameToUse, Container topPanel, Container bottomPanel) 
	{
		super(JSplitPane.VERTICAL_SPLIT);
		mainWindow = mainWindowToUse;
		splitterName = splitterNameToUse;
		
		setOneTouchExpandable(true);
		setDividerSize(15);
		setResizeWeight(.5);
		setTopComponent(topPanel);
		setBottomComponent(bottomPanel);
		setFocusable(false);
		
		setDividerLocationWithoutNotifications(mainWindow.getSplitterLocation(splitterName));
	}

	public void setDividerLocation(int location)
	{
		setDividerLocationWithoutNotifications(location);
		mainWindow.setSplitterLocation(splitterName, location);
	}

	private void setDividerLocationWithoutNotifications(int location)
	{
		super.setDividerLocation(location);
	}

	MainWindow mainWindow;
	String splitterName;
}
