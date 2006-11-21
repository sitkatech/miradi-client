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
	public ViewSplitPane(MainWindow mainWindowToUse, Container topPanel, Container BottomPanel, JSplitPane oldJSplitPane)
	{
		this(topPanel, BottomPanel, oldJSplitPane);
		
		if (oldJSplitPane == null)
			setDividerLocation((int)mainWindowToUse.getSize().getHeight() / 2);
	}
	
	public ViewSplitPane(Container topPanel, Container bottomPanel, JSplitPane oldJSplitPane) 
	{
		super(JSplitPane.VERTICAL_SPLIT);
		
		setOneTouchExpandable(true);
		setDividerSize(15);
		setResizeWeight(.5);
		setTopComponent(topPanel);
		setBottomComponent(bottomPanel);
		
		if (oldJSplitPane != null)
			setDividerLocation(oldJSplitPane.getDividerLocation());
	}

}
