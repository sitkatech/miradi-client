/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.renderers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;

public class RectangleWithPriorityRenderer extends RectangleRenderer
{
	public void fillShape(Graphics g, Rectangle rect, Color color)
	{
		super.fillShape(g, rect, color);
		
		Graphics2D g2 = (Graphics2D)g;
		Paint oldPaint = g2.getPaint();
		drawPriority(g, rect, g2);
		g2.setPaint(oldPaint);
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
		
		drawRatingLetter(g, g2, smallRect);
	}

	//FIXME: need to calculate font position using bounding rectangle of the font
	private void drawRatingLetter(Graphics g, Graphics2D g2, Rectangle smallRect)
	{
		String letter =  priority.getLabel().substring(0,1);
		g2.setFont(new Font("", Font.BOLD, 12));
		g.drawString(letter, smallRect.x+5, smallRect.y+15);
	}

	private static final int PRIORITY_WIDTH = 20;
	private static final int PRIORITY_HEIGHT = 20;
}
