/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.Component;

import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class PersistentVerticalSplitPane extends PersistentSplitPane
{
	public PersistentVerticalSplitPane(Component containerToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, String splitterNameToUse)
	{
		super(containerToUse, splitPositionSaverToUse, splitterNameToUse);
		setOrientation(VERTICAL_SPLIT);
	}

	int getContainerHeightOrWidth()
	{
		return getContainer().getHeight();
	}
	

}
