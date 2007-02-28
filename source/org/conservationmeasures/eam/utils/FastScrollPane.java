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
	public FastScrollPane()
	{
		super();
		setUnitIncrements();
	}
	
	public FastScrollPane(Component view)
	{
		super(view);
		setUnitIncrements();
	}

	public FastScrollPane(int vsbPolicy, int hsbPolicy)
	{
		super(vsbPolicy, hsbPolicy);
		setUnitIncrements();
	}

	public FastScrollPane(Component view, int vsbPolicy, int hsbPolicy)
	{
		super(view, vsbPolicy, hsbPolicy);
		setUnitIncrements();
	}
	
	private void setUnitIncrements()
	{
		getHorizontalScrollBar().setUnitIncrement(SCROLL_UNIT_INCREMENT);
		getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INCREMENT);
	}
	
	int SCROLL_UNIT_INCREMENT = 15;
}
