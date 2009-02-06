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
		setVerticalMargin(margin);
	}
	
	public void setVerticalMargin(int margin)
	{
		vMargin = margin;
	}
	
	public void setGaps(int gap)
	{
		hGap = gap;
		vGap = gap;
	}
	
	public void setAlignmentRight()
	{
		setAlignment(Alignment.EAST);
	}
}
