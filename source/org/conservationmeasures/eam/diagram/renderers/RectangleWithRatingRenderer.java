/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.renderers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;

import org.conservationmeasures.eam.utils.Utility;

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
		if(priority == null)
			return;

		Rectangle smallRect = new Rectangle();
		smallRect.x = rect.x;
		smallRect.y = rect.y;
		smallRect.width = PRIORITY_WIDTH;
		smallRect.height = PRIORITY_HEIGHT;
		setPaint(g2, smallRect, priority.getColor());
		g.fillRect(smallRect.x, smallRect.y, smallRect.width, smallRect.height);
		g2.setColor(Color.BLACK);
		g.drawRect(smallRect.x, smallRect.y, smallRect.width, smallRect.height);
		
		setRatingBubbleFont(g2);
		String letter = priority.getLabel().substring(0,1);
		Utility.drawStringCentered(g2, letter, smallRect);
	}
	
	private static final int PRIORITY_WIDTH = 20;
	private static final int PRIORITY_HEIGHT = 10;
}
