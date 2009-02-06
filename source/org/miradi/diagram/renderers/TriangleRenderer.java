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

import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JLabel;

public class TriangleRenderer extends FactorRenderer {

	Dimension getInsetDimension() 
	{
		return new Dimension(0, 0);
	}
	
	@Override
	public Shape getShape(Rectangle rect)
	{
		return buildTriangle(rect);
	}

	public static Polygon buildTriangle(Rectangle rect)
	{
		int left = rect.x;
		int top = rect.y;
		int width = rect.width;
		int height = rect.height;
		int right = left + width;
		int bottom = top + height;
		int horizontalMiddle = left + width / 2;

		Polygon triangle = new Polygon();
		triangle.addPoint(horizontalMiddle , top);
		triangle.addPoint(left, bottom);
		triangle.addPoint(right, bottom);
		return triangle;
	}

	int getVerticalAlignmentOfText()
	{
		return JLabel.BOTTOM;
	}
}
