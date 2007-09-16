/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.Component;

import org.martus.swing.UiScrollPane;

public class FastScrollPane extends UiScrollPane
{
	
	public FastScrollPane(Component view)
	{
		super(view);
		setUnitIncrements();
	}
	
	private void setUnitIncrements()
	{
		getHorizontalScrollBar().setUnitIncrement(SCROLL_UNIT_INCREMENT);
		getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INCREMENT);
	}
	
	public static final int SCROLL_UNIT_INCREMENT = 15;
}
