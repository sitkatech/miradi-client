/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.Component;

import org.conservationmeasures.eam.main.MainWindow;

public class ViewSplitPane extends PersistentSplitPane
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
	}
	
	static final String MAIN_SPLITTER = "MainSplitter";
}