/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.diagram.renderers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;

public class EllipseWithRatingRenderer extends EllipseRenderer
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

		drawRatingBubble(g2, rect, rating.getColor(), rating.getLabel().substring(0,1));
	}
}
