/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import com.jhlabs.awt.GridLayoutPlus;

public class MiradiGridLayoutPlus extends GridLayoutPlus
{
	public MiradiGridLayoutPlus(int rows, int cols)
	{
		super(rows, cols);
	}

	public MiradiGridLayoutPlus(int rows, int cols, int hGap, int vGap, int hMargin, int vMargin)
	{
		super(rows, cols, hGap, vGap, hMargin, vMargin);
	}
	
	public void growToFillColumn(int column)
	{
		setColWeight(column, GROW_TO_FILL);
	}
	
	public void doNotGrowColumn(int column)
	{
		setColWeight(column, DO_NOT_GROW);	
	}
	
	public void setGaps(int newHorizontalGap, int newVerticalGap)
	{
		hGap = newHorizontalGap;
		vGap = newVerticalGap;
	}
	
	
	private static final int DO_NOT_GROW = 0;
	private static final int GROW_TO_FILL = 1;
}
