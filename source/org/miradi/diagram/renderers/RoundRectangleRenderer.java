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
package org.miradi.diagram.renderers;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

public class RoundRectangleRenderer extends RectangleRenderer
{
	public RoundRectangleRenderer()
	{	
		minimumArcSize = 5;
	}
	
	public RoundRectangleRenderer(int minumArcSizeToUse)
	{
		minimumArcSize = minumArcSizeToUse;
	}
	
	@Override
	public Shape getShape(Rectangle rect)
	{
		return new RoundRectangle2D.Double(rect.x, rect.y, rect.width, rect.height, getArcWidth(rect), getArcHeight(rect));
	}

	int getArcWidth(Rectangle rect)
	{
		return getMinimumArcSize();
	}
	
	int getArcHeight(Rectangle rect)
	{
		return getMinimumArcSize();
	}

	public int getMinimumArcSize()
	{
		return minimumArcSize;
	}
	
	private int minimumArcSize;
}
