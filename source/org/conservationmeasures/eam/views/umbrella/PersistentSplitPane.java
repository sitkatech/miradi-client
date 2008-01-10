/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;


import java.awt.Component;

import javax.swing.JSplitPane;

import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class PersistentSplitPane extends JSplitPane
{
	public PersistentSplitPane(Component componentSplitted, SplitterPositionSaverAndGetter splitPositionSaverToUse,  String splitterNameToUse) 
	{
		super(JSplitPane.VERTICAL_SPLIT);
		
		mainComponentSplitted = componentSplitted;
		splitPositionSaver = splitPositionSaverToUse;
		splitterName = splitterNameToUse;
		
		setOneTouchExpandable(true);
		setDividerSize(15);
		setResizeWeight(.5);
		setFocusable(false);
	
		restoreSavedLocation();
	}
	
	public void restoreSavedLocation()
	{
		int splitterLocation = getSplitterLocation(splitterName);
		setDividerLocationWithoutNotifications(splitterLocation);
	}
	
	public void  updateSplitterLocation(String name)
	{
		setDividerLocation(getSplitterLocation(name));
	}
	
	public void setDividerLocation(int location)
	{
		super.setDividerLocation(location);
		saveLocation(location);
	}

	public void setDividerLocationWithoutNotifications(int location)
	{
		super.setDividerLocation(location);
	}

	private int getMainHeight()
	{
		return mainComponentSplitted.getHeight();
	}
	
	public void saveCurrentLocation()
	{
		saveLocation(getDividerLocation());
	}

	private void saveLocation(int location)
	{
		if (getMainHeight()==0)
			return;
		
		double splitPercent = (double)location * 100 / getMainHeight();
		double splitPercentFromMiddle = splitPercent * 2 - 100;
		long roundedPercent = Math.round(splitPercentFromMiddle);
		splitPositionSaver.saveSplitterLocation(splitterName, (int)roundedPercent);
	}
	
	public int getSplitterLocation(String name)
	{
		int splitPercentFromMiddle = splitPositionSaver.getSplitterLocation(name);		
		int splitPercent = (splitPercentFromMiddle + 100) / 2;
		int location = getMainHeight() * splitPercent / 100;
		
		return location; 
	}
	
	public void setSplitterLocationToMiddle(String name)
	{
		splitPositionSaver.saveSplitterLocation(name, SPLITTER_MIDDLE_LOCATION);
		setDividerLocation(getSplitterLocation(name));
	}
	
	String splitterName;
	private SplitterPositionSaverAndGetter splitPositionSaver;
	private Component mainComponentSplitted;
	
	public final static int SPLITTER_MIDDLE_LOCATION = 0;
}
