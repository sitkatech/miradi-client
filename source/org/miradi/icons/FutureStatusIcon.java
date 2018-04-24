/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.diagram.renderers.FactorRenderer;
import org.miradi.diagram.renderers.TriangleRenderer;

public class FutureStatusIcon extends AbstractMiradiIcon
{
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Rectangle rect = new Rectangle(x + 1, y - 1, getIconWidth()-2, getIconHeight()-2);
		Polygon triangle = TriangleRenderer.buildTriangle(rect);
		
		g.setColor(getFutureStatusIconFillColor());
		g.fillPolygon(triangle);
		
		g.setColor(getBorderColor());
		g.drawPolygon(triangle);
		
		g.setColor(getFutureStatusIconFillColor());
		g.fillRect(x, y + (getIconHeight() / 2 ), getIconWidth(), getIconHeight()/3);
		
		g.setColor(getBorderColor());
		g.drawRect(x, y + (getIconHeight() / 2 ), getIconWidth(), getIconHeight()/3);

	}

	private Color getBorderColor()
	{
		return Color.BLACK;
	}

	private Color getFutureStatusIconFillColor()
	{
		return FactorRenderer.FUTURE_STATUS_COLOR;
	}
}
