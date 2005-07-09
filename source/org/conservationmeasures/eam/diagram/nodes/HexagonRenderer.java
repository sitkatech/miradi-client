/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class HexagonRenderer extends MultilineNodeRenderer
{
	Dimension getInsetDimension()
	{
		return new Dimension(getSize().width / 5, 0);
	}
	
	void fillShape(Graphics g)
	{
		g.fillPolygon(buildHexagon());
	}

	void drawBorder(Graphics2D g2)
	{
		g2.drawPolygon(buildHexagon());
	}

	Polygon buildHexagon()
	{
		int left = borderWidth - 1;
		int top = borderWidth - 1;
		int totalWidth = getSize().width - borderWidth;
		int height = getSize().height - borderWidth;
		int right = left + totalWidth;
		int bottom = top + height;
		int endInset = getInsetDimension().width;
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

