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
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import org.martus.swing.Utilities;

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
		
		drawRatingLetter(g, g2, smallRect);
	}
	
	private void drawRatingLetter(Graphics g, Graphics2D g2, Rectangle smallRect)
	{
		String letter =  rating.getLabel().substring(0,1);
		g2.setFont(new Font("", Font.BOLD, 8));
		Rectangle2D p = calcalateCenteredAndCushioned(g2, smallRect, letter);
		g.drawString(letter, p.getBounds().x,  p.getBounds().y + p.getBounds().height);
	}

	private Rectangle calcalateCenteredAndCushioned(Graphics2D g2, Rectangle2D graphBounds, String text)
	{
		TextLayout textLayout = new TextLayout(text, g2.getFont(), g2.getFontRenderContext());
		Rectangle textBounds = textLayout.getBounds().getBounds();
		Point upperLeftToDrawText = Utilities.center(textBounds.getSize(), graphBounds.getBounds().getBounds());
		textBounds.setLocation(upperLeftToDrawText);
		return textBounds;
	}

	private static final int PRIORITY_WIDTH = 20;
	private static final int PRIORITY_HEIGHT = 10;

}
