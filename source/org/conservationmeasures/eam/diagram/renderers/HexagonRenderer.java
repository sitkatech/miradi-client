/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;

public class HexagonRenderer extends FactorRenderer
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
		fillRawShape(g, rect, color);
	}
	
	public void fillRawShape(Graphics g, Rectangle rect, Color color)
	{
		Graphics2D g2 = (Graphics2D)g;
		Paint oldPaint = g2.getPaint();
		setPaint(g2, rect, color);
		g.fillPolygon(buildHexagon(rect));
		g2.setPaint(oldPaint);
	}

	public void drawBorder(Graphics2D g2, Rectangle rect, Color color)
	{
		Stroke originalStroke = g2.getStroke();
		
		setPaint(g2, rect, color);
		if(isDraft())
		{
			BasicStroke dashedStroke = getDashedStroke((BasicStroke)originalStroke);
			g2.setStroke(dashedStroke);
		}
		g2.drawPolygon(buildHexagon(rect));
		
		g2.setStroke(originalStroke);
	}

	boolean isDraft()
	{
		return node.isStatusDraft();
	}

	BasicStroke getDashedStroke(BasicStroke originalStroke)
	{
		BasicStroke defaultStroke = new BasicStroke();
		float[] dashes = {2, 7};
		BasicStroke dashedStroke = new BasicStroke(
				originalStroke.getLineWidth(), defaultStroke.getEndCap(), 
				defaultStroke.getLineJoin(), defaultStroke.getMiterLimit(),
				dashes, 0);
		return dashedStroke;
	}

	public static Polygon buildHexagon(Rectangle rect)
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

