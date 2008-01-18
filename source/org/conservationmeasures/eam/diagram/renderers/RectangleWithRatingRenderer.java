/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.renderers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;

public class RectangleWithRatingRenderer extends RectangleRenderer
{
	public void fillShape(Graphics g, Rectangle rect, Color color)
	{
		super.fillShape(g, rect, color);
		
		Graphics2D g2 = (Graphics2D)g;
		Paint oldPaint = g2.getPaint();
		drawPriority(g, rect, g2);
		g2.setPaint(oldPaint);
	}
	
	Dimension getInsetDimension()
	{
		return new Dimension(0, PRIORITY_HEIGHT);
	}

	void drawPriority(Graphics g, Rectangle rect, Graphics2D g2) 
	{		
		if (priority == null)
			return;
		
		if (priority.getCode().length() == 0)
			return;
		
		if (priority.getCode().equals("0"))
			return;

		drawRatingBubble(g2, rect, priority.getColor(), priority.getLabel().substring(0,1));
	}
}
