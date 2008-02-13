/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import java.awt.Component;

import org.miradi.utils.SplitterPositionSaverAndGetter;

public class PersistentNonPercentageHorizontalSplitPane extends PersistentHorizontalSplitPane
{
	public PersistentNonPercentageHorizontalSplitPane(Component containerToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, String splitterNameToUse)
	{
		super(containerToUse, splitPositionSaverToUse, splitterNameToUse);
		setResizeWeight(0);

	}

	@Override
	protected int computeLocationFromPercent(int splitPercentFromMiddle)
	{
		if(splitPercentFromMiddle <= 0)
			splitPercentFromMiddle = getDefaultSplitterLocation();
		return splitPercentFromMiddle - 1;
	}
	
	@Override
	protected double computePercentFromLocation(int location)
	{
		return location + 1;
	}

	private int getDefaultSplitterLocation()
	{
		return 200;
	}

}
