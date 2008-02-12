/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.layout;

import com.jhlabs.awt.Alignment;
import com.jhlabs.awt.GridLayoutPlus;

public class OneRowGridLayout extends GridLayoutPlus
{
	public OneRowGridLayout()
	{
		super(1, 0, 0, 0, 0, 0);
		setFill(Alignment.FILL_NONE);
		setAlignment(Alignment.NORTH);
	}
	
	public void setMargins(int margin)
	{
		hMargin = margin;
		vMargin = margin;
	}
	
	public void setAlignmentRight()
	{
		setAlignment(Alignment.EAST);
	}
}
