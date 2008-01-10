/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.Component;

import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class PersistentHorizontalSplitPane extends PersistentSplitPane
{
	public PersistentHorizontalSplitPane(Component containerToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, String splitterNameToUse)
	{
		super(containerToUse, splitPositionSaverToUse, splitterNameToUse);
		setOrientation(HORIZONTAL_SPLIT);
	}

	int getContainerHeightOrWidth()
	{
		return getContainer().getWidth();
	}

}
