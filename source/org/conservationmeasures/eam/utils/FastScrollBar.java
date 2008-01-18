/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import javax.swing.JScrollBar;

public class FastScrollBar extends JScrollBar
{
	public FastScrollBar()
	{
		setUnitIncrement(FastScrollPane.SCROLL_UNIT_INCREMENT);
	}
}
