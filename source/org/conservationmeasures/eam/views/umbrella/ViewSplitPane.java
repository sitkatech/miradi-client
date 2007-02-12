/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;


import java.awt.Component;

import javax.swing.JSplitPane;

import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class ViewSplitPane extends JSplitPane
{
	public ViewSplitPane(Component componentSplitted, SplitterPositionSaverAndGetter splitPositionSaverToUse,  String splitterNameToUse, Component topPanel, Component bottomPanel) 
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
		
		int splitterLocation = getSplitterLocation(splitterName);
		setDividerLocationWithoutNotifications(splitterLocation);
	}
	
	public void setDividerLocation(int location)
	{
		setDividerLocationWithoutNotifications(location);

		int splitPercent = location * 100 / mainComponentSplitted.getHeight();
		int splitPercentFromMiddle = splitPercent * 2 - 100;
		
		splitPositionSaver.saveSplitterLocation(splitterName, splitPercentFromMiddle);
	}

	private void setDividerLocationWithoutNotifications(int location)
	{
		super.setDividerLocation(location);
	}
	
	public void setSplitterLocationToMiddle(String name)
	{
		splitPositionSaver.saveSplitterLocation(name, SPLITTER_MIDDLE_LOCATION);
		setDividerLocation(0.5);
	}

	public int getSplitterLocation(String name)
	{
		int splitPercentFromMiddle = splitPositionSaver.getSplitterLocation(name);
		int splitPercent = (splitPercentFromMiddle + 100) / 2;
		int location = mainComponentSplitted.getHeight() * splitPercent / 100;
		
		return location; 
	}
	
	String splitterName;
	private SplitterPositionSaverAndGetter splitPositionSaver;
	private Component mainComponentSplitted;
	
	public final static int SPLITTER_MIDDLE_LOCATION = 0;
}
