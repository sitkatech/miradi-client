/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import java.awt.Dimension;

public class HideableScrollBar extends FastScrollBar
{
	public HideableScrollBar()
	{
		visible = true;
	}
	
	public Dimension getPreferredSize()
	{
		Dimension dimension = super.getPreferredSize();
		if(!visible)
			dimension.width = 0;
		return dimension;
	}

	public boolean visible;
}
