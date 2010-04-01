/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.Icon;

import org.miradi.diagram.renderers.TriangleRenderer;

public abstract class AbstractTriangleIcon implements Icon
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
		Rectangle rect = createRectangle(x, y);
		Polygon triangle = TriangleRenderer.buildTriangle(rect);
		g.setColor(getFillColor());
		g.fillPolygon(triangle);
		g.setColor(Color.BLACK);
		g.drawPolygon(triangle);
	}

	protected Rectangle createRectangle(int x, int y)
	{
		return new Rectangle(x+1, y+1, WIDTH-2, HEIGHT-2);
	}

	abstract protected Color getFillColor();
	
	static final int WIDTH = 16;
	static final int HEIGHT = 16;
}
