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
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.VertexView;


public class RectangleRenderer extends FactorRenderer
{
	Dimension getInsetDimension()
	{
		return new Dimension(0, 0);
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
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
	
		if(node != null && node.getComment().length() > 0)
			drawCommentTriangle(g2, rect, color);
		
		g2.setPaint(oldPaint);
	}

	private void drawCommentTriangle(Graphics2D g2, Rectangle rect, Color color)
	{
		int triangleInset = 15;
		Polygon triangle = new Polygon();
		triangle.addPoint(getWidth() - triangleInset, 0);
		triangle.addPoint(getWidth(), 0);
		triangle.addPoint(getWidth(), triangleInset);
		triangle.addPoint(getWidth() - triangleInset, 0);
		setPaint(g2, rect, Color.CYAN);
		g2.fill(triangle);
		
		setPaint(g2, rect, Color.BLACK);
		g2.drawPolygon(triangle);
	}

	public void drawBorder(Graphics2D g2, Rectangle rect, Color color)
	{
		Color oldColor = g2.getColor();
		g2.setColor(color);
		g2.drawRect(rect.x, rect.y, rect.width, rect.height);
		g2.setColor(oldColor);
	}

	/*
	 * This method has a different copyright than the rest of this file,
	 * because it was copied directly from jgraph's VertexRenderer class:
	 * 
	 * Copyright (c) 2001-2004 Gaudenz Alder
	 *   
 	 */

	/**
	 * Returns the intersection of the bounding rectangle and the straight line
	 * between the source and the specified point p. The specified point is
	 * expected not to intersect the bounds.
	 */
	public Point2D getPerimeterPoint(VertexView view, Point2D source, Point2D p) 
	{
		return getPerimeterPoint(p, view.getBounds());
	}

	public Point2D getPerimeterPoint(Point2D p, Rectangle2D bounds)
	{
		double x = bounds.getX();
		double y = bounds.getY();
		double width = bounds.getWidth();
		double height = bounds.getHeight();
		double xCenter = x + width / 2;
		double yCenter = y + height / 2;
		double dx = p.getX() - xCenter; // Compute Angle
		double dy = p.getY() - yCenter;
		double alpha = Math.atan2(dy, dx);
		double xout = 0, yout = 0;
		double pi = Math.PI;
		double pi2 = Math.PI / 2.0;
		double beta = pi2 - alpha;
		double t = Math.atan2(height, width);
		if (alpha < -pi + t || alpha > pi - t) { // Left edge
			xout = x;
			yout = yCenter - width * Math.tan(alpha) / 2;
		} else if (alpha < -t) { // Top Edge
			yout = y;
			xout = xCenter - height * Math.tan(beta) / 2;
		} else if (alpha < t) { // Right Edge
			xout = x + width;
			yout = yCenter + width * Math.tan(alpha) / 2;
		} else { // Bottom Edge
			yout = y + height;
			xout = xCenter + height * Math.tan(beta) / 2;
		}
		return new Point2D.Double(xout, yout);
	}
	
}
