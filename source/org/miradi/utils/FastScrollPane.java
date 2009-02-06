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
