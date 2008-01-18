/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.Component;

import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class ManagementPanelSplitPane extends PersistentVerticalSplitPane
{
	public ManagementPanelSplitPane(Component componentSplitted, SplitterPositionSaverAndGetter splitPositionSaverToUse,  String splitterNameToUse, Component topPanel, Component bottomPanel)
	{
		super(componentSplitted, splitPositionSaverToUse, splitterNameToUse);

		setTopComponent(topPanel);
		setBottomComponent(bottomPanel);

		setOneTouchExpandable(false);
		setDividerSize(DIVIDER_SIZE);
		
		restoreSavedLocation();
	}
	
	public void setDividerLocation(int location)
	{
		location = getMinimumViewableLocation(location);
		super.setDividerLocation(location);
	}

	private int getMinimumViewableLocation(int location)
	{
		final int MINIMUM_SPLITTER_LOCATION = 45;
		if (location < MINIMUM_SPLITTER_LOCATION)
			return MINIMUM_SPLITTER_LOCATION;
		
		return location;
	}

	static final int DIVIDER_SIZE = 5;
}

