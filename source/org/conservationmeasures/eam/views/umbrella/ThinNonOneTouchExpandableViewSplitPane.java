/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.Component;

import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class ThinNonOneTouchExpandableViewSplitPane extends ViewSplitPane
{
	public ThinNonOneTouchExpandableViewSplitPane(Component componentSplitted, SplitterPositionSaverAndGetter splitPositionSaverToUse,  String splitterNameToUse, Component topPanel, Component bottomPanel)
	{
		super(componentSplitted, splitPositionSaverToUse, splitterNameToUse, topPanel, bottomPanel);
		setOneTouchExpandable(false);
		setDividerSize(DIVIDER_SIZE);
	}
	
	static final int DIVIDER_SIZE = 5;
}

