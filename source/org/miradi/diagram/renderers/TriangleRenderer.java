/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
