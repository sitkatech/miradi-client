/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.renderers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;

public class HexagonWithRatingRenderer extends HexagonRenderer
{
	public void fillShape(Graphics g, Rectangle rect, Color color)
	{
		super.fillShape(g, rect, color);
		
		Graphics2D g2 = (Graphics2D)g;
		Paint oldPaint = g2.getPaint();
		drawRating(g, rect, g2);
		g2.setPaint(oldPaint);
	}
	
	void drawRating(Graphics g, Rectangle rect, Graphics2D g2) 
	{
		if(rating == null || rating.getCode().length() == 0)
			return;

		Rectangle smallRect = new Rectangle();
		smallRect.x = rect.x;
		smallRect.y = rect.y;
		smallRect.width = PRIORITY_WIDTH;
		smallRect.height = PRIORITY_HEIGHT;
		setPaint(g2, smallRect, rating.getColor());
		Polygon smallHex = buildHexagon(smallRect);
		g.fillPolygon(smallHex);
		g2.setColor(Color.BLACK);
		g.drawPolygon(smallHex);
	}
	

	private static final int PRIORITY_WIDTH = 20;
	private static final int PRIORITY_HEIGHT = 10;

}
