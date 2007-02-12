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
		
		int mainComponentHeight = mainComponentSplitted.getHeight();
		int splitterLocation = getSplitterLocation(mainComponentHeight, splitterName);
		setDividerLocationWithoutNotifications(splitterLocation);
	}
	
	public void setDividerLocation(int location)
	{
		setDividerLocationWithoutNotifications(location);
		setSplitterLocation(mainComponentSplitted.getHeight(), splitterName, location);
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

	public void setSplitterLocation(int containerHeight, String name, int location)
	{
		int splitPercent = location * 100 / containerHeight;
		int splitPercentFromMiddle = splitPercent * 2 - 100;
		
		splitPositionSaver.saveSplitterLocation(name, splitPercentFromMiddle);
	}
	
	public int getSplitterLocation(int containerHeight, String name)
	{
		int splitPercentFromMiddle = splitPositionSaver.getSplitterLocation(name);
		int splitPercent = (splitPercentFromMiddle + 100) / 2;
		int location = containerHeight * splitPercent / 100;
		
		return location; 
	}
	
	String splitterName;
	private SplitterPositionSaverAndGetter splitPositionSaver;
	private Component mainComponentSplitted;
	
	public final static int SPLITTER_MIDDLE_LOCATION = 0;
}
