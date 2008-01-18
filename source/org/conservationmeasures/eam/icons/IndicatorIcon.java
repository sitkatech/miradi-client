/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.Icon;

import org.conservationmeasures.eam.diagram.renderers.TriangleRenderer;
import org.conservationmeasures.eam.main.AppPreferences;

public class IndicatorIcon implements Icon
{
	public int getIconHeight()
	{
		return HEIGHT;
	}

	public int getIconWidth()
	{
		return WIDTH;
	}

	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Rectangle rect = new Rectangle(x+1, y+1, WIDTH-2, HEIGHT-2);
		Polygon triangle = TriangleRenderer.buildTriangle(rect);
		g.setColor(AppPreferences.INDICATOR_COLOR);
		g.fillPolygon(triangle);
		g.setColor(Color.BLACK);
		g.drawPolygon(triangle);
	}
	
	static final int WIDTH = 16;
	static final int HEIGHT = 16;

}
