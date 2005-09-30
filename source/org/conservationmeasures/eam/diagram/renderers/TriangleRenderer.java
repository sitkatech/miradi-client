/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.renderers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.JLabel;

public class TriangleRenderer extends MultilineNodeRenderer {

	Dimension getInsetDimension() 
	{
		return new Dimension(0, 0);
	}

	public void fillShape(Graphics g, Rectangle rect, Color color) 
	{
		Graphics2D g2 = (Graphics2D)g;
		Paint oldPaint = g2.getPaint();
		setPaint(g2, rect, color);
		g.fillPolygon(buildTriangle(rect));
		g2.setPaint(oldPaint);
	}

	public void drawBorder(Graphics2D g2, Rectangle rect, Color color)
	{
		setPaint(g2, rect, color);
		g2.drawPolygon(buildTriangle(rect));
	}

	static Polygon buildTriangle(Rectangle rect)
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

	int getVirticalAlignmentOfText()
	{
		return JLabel.CENTER;
	}
}
