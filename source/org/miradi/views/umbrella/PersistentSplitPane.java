/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.umbrella;


import java.awt.Component;

import javax.swing.JSplitPane;

import org.miradi.utils.SplitterPositionSaverAndGetter;

abstract public class PersistentSplitPane extends JSplitPane
{
	public PersistentSplitPane(Component containerToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse,  String splitterNameToUse) 
	{
		container = containerToUse;
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

	abstract int getContainerHeightOrWidth();
	
	public void saveCurrentLocation()
	{
		saveLocation(getDividerLocation());
	}

	public boolean isSavedLocationDefault()
	{
		int splitPercentFromMiddle = splitPositionSaver.getSplitterLocation(splitterName);		
		return (splitPercentFromMiddle == 0);
	}
	
	private void saveLocation(int location)
	{
		if (getContainerHeightOrWidth()==0)
			return;
		
		int roundedPercent = computePercentFromLocation(location);
		splitPositionSaver.saveSplitterLocation(splitterName, roundedPercent);
	}

	protected int computePercentFromLocation(int location)
	{
		double splitPercent = Math.round((double)location * 100 / getContainerHeightOrWidth());
		double splitPercentFromMiddle = splitPercent * 2 - 100;
		int roundedPercent = (int)splitPercentFromMiddle;
		return roundedPercent;
	}
	
	public int getSplitterLocation(String name)
	{
		int splitPercentFromMiddle = splitPositionSaver.getSplitterLocation(name);		
		int location = computeLocationFromPercent(splitPercentFromMiddle);
		
		return location; 
	}

	protected int computeLocationFromPercent(int splitPercentFromMiddle)
	{
		double splitPercent = (splitPercentFromMiddle + 100) / 2 + .5;
		int location = (int) (getContainerHeightOrWidth() * splitPercent / 100);
		return location;
	}
	
	public void setSplitterLocationToMiddle(String name)
	{
		splitPositionSaver.saveSplitterLocation(name, SPLITTER_MIDDLE_LOCATION);
		int location = getSplitterLocation(name);
		setDividerLocation(location);
	}
	
	Component getContainer()
	{
		return container;
	}
	
	private Component container;
	private String splitterName;
	private SplitterPositionSaverAndGetter splitPositionSaver;
	
	public final static int SPLITTER_MIDDLE_LOCATION = 0;
}
