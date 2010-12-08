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
}
