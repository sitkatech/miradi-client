/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;


import java.awt.Container;

import javax.swing.JSplitPane;

public class ViewSplitPane extends JSplitPane
{
	
public ViewSplitPane(Container topPanel, Container BottomPanel, JSplitPane oldJSplitPane) {
	
	super(JSplitPane.VERTICAL_SPLIT);
	
	setOneTouchExpandable(true);
	setDividerSize(15);
	setResizeWeight(.5);
	setTopComponent(topPanel);
	setBottomComponent(BottomPanel);
	
	if (oldJSplitPane != null)
		setDividerLocation(oldJSplitPane.getDividerLocation());
}

}
