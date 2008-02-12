/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import java.awt.Component;
import java.awt.event.MouseWheelEvent;

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

	public void processMouseWheelEvent(MouseWheelEvent e)
	{
		//note: overriding to change visibility
		super.processMouseWheelEvent(e);
	}

	public static final int SCROLL_UNIT_INCREMENT = 15;
}
