/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;


import javax.swing.JSplitPane;

import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

abstract public class PersistentSplitPane extends JSplitPane
{
	public PersistentSplitPane(SplitterPositionSaverAndGetter splitPositionSaverToUse,  String splitterNameToUse) 
	{
		super(JSplitPane.VERTICAL_SPLIT);
		
		splitPositionSaver = splitPositionSaverToUse;
		splitterName = splitterNameToUse;
		
		setOneTouchExpandable(true);
		setDividerSize(15);
		setResizeWeight(.5);
		setFocusable(false);
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

	abstract int getMainHeight();
	
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
	
	private String splitterName;
	private SplitterPositionSaverAndGetter splitPositionSaver;
	
	public final static int SPLITTER_MIDDLE_LOCATION = 0;
}
