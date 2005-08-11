/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;

public class HexagonRenderer extends MultilineNodeRenderer
{
	Dimension getInsetDimension()
	{
		return getInsetDimension(getSize().width);
	}
	
	static Dimension getInsetDimension(int totalWidth)
	{
		return new Dimension(totalWidth / 5, 0);
	}
	
	public void fillShape(Graphics g, Rectangle rect, Color color)
	{
		Graphics2D g2 = (Graphics2D)g;
		Paint oldPaint = g2.getPaint();
		setPaint(g2, rect, color);
		g.fillPolygon(buildHexagon(rect));
		g2.setPaint(oldPaint);
	}

	public void drawBorder(Graphics2D g2, Rectangle rect, Color color)
	{
		setPaint(g2, rect, color);
		g2.drawPolygon(buildHexagon(rect));
	}

	static Polygon buildHexagon(Rectangle rect)
	{
		int left = rect.x;
		int top = rect.y;
		int totalWidth = rect.width;
		int height = rect.height;
		int right = left + totalWidth;
		int bottom = top + height;
		int endInset = getInsetDimension(totalWidth).width;
		int verticalMiddle = top + (height / 2);
		
		Polygon hex = new Polygon();
		hex.addPoint(left, verticalMiddle);
		hex.addPoint(left + endInset, top);
		hex.addPoint(right - endInset, top);
		hex.addPoint(right, verticalMiddle);
		hex.addPoint(right - endInset, bottom);
		hex.addPoint(left + endInset, bottom);
		hex.addPoint(left, verticalMiddle);
		return hex;
	}

}

