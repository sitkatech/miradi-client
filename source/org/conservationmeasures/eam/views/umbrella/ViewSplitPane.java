/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;


import java.awt.Component;

import javax.swing.JSplitPane;

import org.conservationmeasures.eam.utils.SplitterPositionSaver;

public class ViewSplitPane extends JSplitPane
{
	public ViewSplitPane(Component componentSplitted, SplitterPositionSaver splitPositionSaverToUse,  String splitterNameToUse, Component topPanel, Component bottomPanel) 
	{
		super(JSplitPane.VERTICAL_SPLIT);
		
		mainComponentSplitted = componentSplitted;
		splitPositionSaver = splitPositionSaverToUse;
		splitterName = splitterNameToUse;
		
		setOneTouchExpandable(true);
		setDividerSize(15);
		setResizeWeight(.5);
		setTopComponent(topPanel);
		setBottomComponent(bottomPanel);
		setFocusable(false);
		
		int mainComponentHeight = mainComponentSplitted.getHeight();
		int splitterLocation = splitPositionSaver.getSplitterLocation(mainComponentHeight, splitterName);
		setDividerLocationWithoutNotifications(splitterLocation);
	}
	
	public void setSplitterLocationToMiddle(String name)
	{
		splitPositionSaver.setSplitterLocationToMiddle(name);
		setDividerLocation(0.5);
	}
	
	public void setDividerLocation(int location)
	{
		setDividerLocationWithoutNotifications(location);
		splitPositionSaver.setSplitterLocation(mainComponentSplitted.getHeight(), splitterName, location);
	}

	private void setDividerLocationWithoutNotifications(int location)
	{
		super.setDividerLocation(location);
	}
	
	String splitterName;
	private SplitterPositionSaver splitPositionSaver;
	private Component mainComponentSplitted;
}
