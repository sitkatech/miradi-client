/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;


import java.awt.Container;

import javax.swing.JSplitPane;

import org.conservationmeasures.eam.utils.SplitterPositionSaver;

public class ViewSplitPane extends JSplitPane
{
	public ViewSplitPane(SplitterPositionSaver splitPositionSaverToUse,  String splitterNameToUse, Container topPanel, Container bottomPanel) 
	{
		super(JSplitPane.VERTICAL_SPLIT);
		splitPositionSaver = splitPositionSaverToUse;
		splitterName = splitterNameToUse;
		
		setOneTouchExpandable(true);
		setDividerSize(15);
		setResizeWeight(.5);
		setTopComponent(topPanel);
		setBottomComponent(bottomPanel);
		setFocusable(false);
		
		setDividerLocationWithoutNotifications(splitPositionSaver.getSplitterLocation(splitterName));
	}

	public void setDividerLocation(int location)
	{
		setDividerLocationWithoutNotifications(location);
		splitPositionSaver.setSplitterLocation(splitterName, location);
	}

	private void setDividerLocationWithoutNotifications(int location)
	{
		super.setDividerLocation(location);
	}

	String splitterName;
	private SplitterPositionSaver splitPositionSaver;
}
