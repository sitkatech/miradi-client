/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import java.awt.Component;

import org.miradi.utils.SplitterPositionSaverAndGetter;

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
