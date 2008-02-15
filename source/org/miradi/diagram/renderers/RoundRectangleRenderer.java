/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.diagram.renderers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;

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
	
	public void fillShape(Graphics g, Rectangle rect, Color color)
	{
		Graphics2D g2 = (Graphics2D)g;
		Paint oldPaint = g2.getPaint();
		setPaint(g2, rect, color);
		g.fillRoundRect(rect.x, rect.y, rect.width, rect.height, getArcWidth(rect), getArcHeight(rect));
	
		g2.setPaint(oldPaint);
	}

	public void drawBorder(Graphics2D g2, Rectangle rect, Color color)
	{
		Color oldColor = g2.getColor();
		g2.setColor(color);
		g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, getArcWidth(rect), getArcHeight(rect));
		g2.setColor(oldColor);
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
	
	@Override
	Dimension getInsetDimension()
	{
		Dimension insetDimension = new Dimension(minimumArcSize/2, 0);
		return insetDimension;
	}
	
	private int minimumArcSize;
}
